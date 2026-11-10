package com.cevicheria.platform.service.impl;

import com.cevicheria.platform.dto.AttemptRequest;
import com.cevicheria.platform.dto.AttemptView;
import com.cevicheria.platform.dto.BillItemRequest;
import com.cevicheria.platform.dto.BillItemView;
import com.cevicheria.platform.dto.BillRequest;
import com.cevicheria.platform.dto.BillView;
import com.cevicheria.platform.dto.CustomerSnapshotRequest;
import com.cevicheria.platform.dto.CustomerSnapshotView;
import com.cevicheria.platform.dto.IssuerRequest;
import com.cevicheria.platform.dto.IssuerView;
import com.cevicheria.platform.dto.ReceiptItemRequest;
import com.cevicheria.platform.dto.ReceiptItemView;
import com.cevicheria.platform.dto.ReceiptRequest;
import com.cevicheria.platform.dto.ReceiptView;
import com.cevicheria.platform.handlerException.BadRequestException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.*;
import com.cevicheria.platform.model.enums.*;
import com.cevicheria.platform.model.id.*;
import com.cevicheria.platform.service.BillingSalesService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BillingSalesServiceImpl implements BillingSalesService {
    private final BillingSupport guard;
    private final EntityManager em;

    @Override
    public List<BillView> bills(Long b, Long br) {
        guard.write(b, br, "COBRAR");
        return guard.byBranch(OrderBill.class, b, br).stream().map(this::billView).toList();
    }
    @Override
    public BillView bill(Long b, Long br, Long id) {
        guard.write(b, br, "COBRAR");
        return billView(guard.local(OrderBill.class, id, b, br,
                OrderBill::getBusiness, OrderBill::getBranch));
    }
    @Override @Transactional
    public BillView createBill(BillRequest r) {
        guard.required(r, "request");
        guard.write(r.businessId(), r.branchId(), "COBRAR");
        CustomerOrder order = guard.local(CustomerOrder.class, r.orderId(), r.businessId(), r.branchId(),
                CustomerOrder::getBusiness, CustomerOrder::getBranch);
        guard.required(r.number(), "number");
        guard.check(r.number() > 0, "El número de la cuenta debe ser positivo");
        long existing = guard.count("select count(b) from OrderBill b where b.order.id = :id and b.status <> :status",
                "id", order.getId(), "status", OrderBillStates.ANULADA);
        guard.conflict(existing > 0, "El pedido ya tiene una cuenta no anulada; la división queda pendiente para una versión posterior");
        OrderBill bill = new OrderBill(guard.business(r.businessId()),
                guard.branch(r.businessId(), r.branchId()), order, r.number());
        if (r.name() != null) bill.changeName(r.name());
        Customer customer = r.customerId() == null ? order.getCustomer() : guard.require(Customer.class, r.customerId());
        if (customer != null) {
            guard.businessScope(customer.getBusiness(), r.businessId());
            bill.assignCustomer(customer);
        }
        bill.changeAmounts(order.getSubtotal(), order.getDiscount(), order.getTax(),
                order.getDeliveryFee(), order.getTotal());
        em.persist(bill);
        em.flush();
        return billView(bill);
    }
    @Override @Transactional
    public void cancelBill(Long b, Long br, Long id) {
        guard.write(b, br, "AUTORIZAR");
        OrderBill bill = localBill(b, br, id);
        guard.check(bill.getStatus() == OrderBillStates.ABIERTA, "Solo puede anularse una cuenta abierta");
        long apps = guard.count("select count(a) from PaymentApplication a where a.orderBill.id = :id and a.status = :status",
                "id", id, "status", PaymentApplicationStates.APLICADA);
        guard.conflict(apps > 0, "La cuenta tiene pagos aplicados");
        long receipts = guard.count("select count(r) from SalesReceipt r where r.orderBill.id = :id and r.status <> :status",
                "id", id, "status", SalesReceiptStates.ANULADO);
        guard.conflict(receipts > 0, "La cuenta tiene un comprobante; no puede anularse");
        bill.changeStatus(OrderBillStates.ANULADA);
    }
    private OrderBill localBill(Long b, Long br, Long id) {
        return guard.local(OrderBill.class, id, b, br, OrderBill::getBusiness, OrderBill::getBranch);
    }
    private BillView billView(OrderBill b) {
        return new BillView(b.getId(), b.getBusiness().getId(), b.getBranch().getId(),
                b.getOrder().getId(), b.getNumber(), b.getStatus(), b.getSubtotal(),
                b.getDiscount(), b.getTax(), b.getDeliveryFee(), b.getTotal());
    }

    @Override
    public List<BillItemView> billItems(Long b, Long br, Long billId) {
        guard.write(b, br, "COBRAR");
        localBill(b, br, billId);
        return em.createQuery("select i from OrderBillItem i where i.orderBill.id = :id", OrderBillItem.class)
                .setParameter("id", billId).getResultList().stream().map(this::billItemView).toList();
    }
    @Override @Transactional
    public BillItemView addBillItem(BillItemRequest r) {
        guard.required(r, "request");
        guard.write(r.businessId(), r.branchId(), "COBRAR");
        OrderBill bill = localBill(r.businessId(), r.branchId(), r.orderBillId());
        guard.check(bill.getStatus() == OrderBillStates.ABIERTA, "La cuenta debe estar abierta");
        OrderItem item = guard.local(OrderItem.class, r.orderItemId(), r.businessId(), r.branchId(),
                OrderItem::getBusiness, OrderItem::getBranch);
        guard.requireSameOrder(item.getOrder().getId(), bill.getOrder().getId());
        guard.required(r.quantity(), "quantity");
        guard.check(r.quantity().compareTo(item.getQuantity()) == 0,
                "Para la demostración se registra la cantidad completa del detalle del pedido");
        guard.check(r.subtotal() != null && r.discount() != null && r.tax() != null
                        && r.total() != null && r.subtotal().compareTo(item.getSubtotal()) == 0
                        && r.discount().compareTo(item.getDiscount()) == 0
                        && r.tax().compareTo(item.getTax()) == 0
                        && r.total().compareTo(item.getTotal()) == 0,
                "Los importes del detalle deben coincidir con los del pedido");
        long exists = guard.count("select count(i) from OrderBillItem i where i.orderBill.id = :b and i.orderItem.id = :i",
                "b", bill.getId(), "i", item.getId());
        guard.conflict(exists > 0, "El detalle ya pertenece a esta cuenta");
        OrderBillItem v = new OrderBillItem(guard.business(r.businessId()),
                guard.branch(r.businessId(), r.branchId()), bill, item, r.quantity());
        guard.amounts(r.subtotal(), r.discount(), r.tax(), BigDecimal.ZERO, r.total());
        v.changeAmounts(r.subtotal(), r.discount(), r.tax(), r.total());
        em.persist(v);
        em.flush();
        return billItemView(v);
    }
    @Override @Transactional
    public void deleteBillItem(Long b, Long br, Long id) {
        guard.write(b, br, "COBRAR");
        OrderBillItem v = guard.local(OrderBillItem.class, id, b, br,
                OrderBillItem::getBusiness, OrderBillItem::getBranch);
        guard.check(v.getOrderBill().getStatus() == OrderBillStates.ABIERTA, "La cuenta debe estar abierta");
        long refs = guard.count("select count(i) from SalesReceiptItem i where i.orderBillItem.id = :id and i.business.id = :b",
                "id", id, "b", b);
        guard.conflict(refs > 0, "El detalle está asociado a un comprobante");
        em.remove(v);
    }
    private BillItemView billItemView(OrderBillItem v) {
        return new BillItemView(v.getId(), v.getBusiness().getId(), v.getBranch().getId(),
                v.getOrderBill().getId(), v.getOrderItem().getId(), v.getQuantity(), v.getTotal());
    }

    @Override
    public List<ReceiptView> receipts(Long b, Long br) {
        guard.write(b, br, "FACTURAR");
        return guard.byBranch(SalesReceipt.class, b, br).stream().map(this::receiptView).toList();
    }
    @Override
    public ReceiptView receipt(Long b, Long br, Long id) {
        guard.write(b, br, "FACTURAR");
        return receiptView(localReceipt(b, br, id));
    }
    @Override @Transactional
    public ReceiptView createReceipt(ReceiptRequest r) {
        guard.required(r, "request");
        guard.write(r.businessId(), r.branchId(), "FACTURAR");
        guard.required(r.receiptSeriesId(), "receiptSeriesId");
        guard.required(r.orderBillId(), "orderBillId");
        OrderBill sourceBill = localBill(r.businessId(), r.branchId(), r.orderBillId());
        guard.check(sourceBill.getStatus() == OrderBillStates.PAGADA,
                "La cuenta debe estar pagada antes de crear el comprobante local");
        long existingReceipts = guard.count(
                "select count(v) from SalesReceipt v where v.orderBill.id = :bill and v.status <> :cancelled",
                "bill", sourceBill.getId(), "cancelled", SalesReceiptStates.ANULADO);
        guard.conflict(existingReceipts > 0, "La cuenta ya tiene un comprobante no anulado");
        ReceiptSeries series = em.find(ReceiptSeries.class, r.receiptSeriesId(), LockModeType.PESSIMISTIC_WRITE);
        if (series == null) throw new ResourceNotFoundException("ReceiptSeries", "ID", r.receiptSeriesId());
        guard.scope(series.getBusiness(), series.getBranch(), r.businessId(), r.branchId());
        guard.check(series.isActive(), "La serie se encuentra inactiva");
        Long next = series.getLastNumber() == null ? 1L : series.getLastNumber() + 1L;
        guard.check(next > 0, "La numeración de la serie llegó a su límite");
        SalesReceipt v = new SalesReceipt(guard.business(r.businessId()),
                guard.branch(r.businessId(), r.branchId()), series, next, LocalDateTime.now());
        v.changeOrderBill(sourceBill);
        v.changeAmounts(sourceBill.getSubtotal(), sourceBill.getDiscount(), sourceBill.getTax(),
                sourceBill.getDeliveryFee(), sourceBill.getTotal());
        if (r.customerId() != null) {
            Customer customer = guard.require(Customer.class, r.customerId());
            guard.businessScope(customer.getBusiness(), r.businessId());
            v.assignCustomer(customer);
        }
        if (r.currencyCode() != null) v.changeCurrencyCode(r.currencyCode());
        em.persist(v);
        series.changeLastNumber(next);
        em.flush();
        return receiptView(v);
    }
    @Override @Transactional
    public ReceiptView issueLocally(Long b, Long br, Long id) {
        guard.write(b, br, "FACTURAR");
        SalesReceipt v = localReceipt(b, br, id);
        guard.check(v.getStatus() == SalesReceiptStates.BORRADOR, "Solo puede finalizarse un borrador");
        guard.check(v.getOrderBill() != null && v.getOrderBill().getStatus() == OrderBillStates.PAGADA,
                "Se requiere una cuenta pagada para finalizar el comprobante");
        long items = guard.count("select count(i) from SalesReceiptItem i where i.salesReceipt.id = :id and i.business.id = :b",
                "id", id, "b", b);
        guard.check(items > 0, "Agrega al menos un detalle al comprobante");
        guard.check(em.find(SalesReceiptIssuerSnapshot.class, new SalesReceiptIssuerSnapshotId(b, br, id)) != null,
                "Falta la información del emisor");
        guard.check(em.find(SalesReceiptCustomerSnapshot.class, new SalesReceiptCustomerSnapshotId(b, br, id)) != null,
                "Falta la información del cliente");
        List<SalesReceiptItem> details = em.createQuery(
                        "select i from SalesReceiptItem i where i.salesReceipt.id = :id", SalesReceiptItem.class)
                .setParameter("id", id).getResultList();
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal tax = BigDecimal.ZERO;
        BigDecimal sum = BigDecimal.ZERO;
        for (SalesReceiptItem item : details) {
            subtotal = subtotal.add(item.getSubtotal());
            discount = discount.add(item.getDiscount());
            tax = tax.add(item.getTax());
            sum = sum.add(item.getTotal());
        }
        if (v.getOrderBill() == null) {
            v.changeAmounts(subtotal, discount, tax, BigDecimal.ZERO, sum);
        } else {
            guard.check(sum.add(v.getDeliveryFee()).compareTo(v.getTotal()) == 0,
                    "El total de los detalles no coincide con el comprobante");
        }
        v.changeStatus(SalesReceiptStates.EMITIDO);
        return receiptView(v);
    }
    private SalesReceipt localReceipt(Long b, Long br, Long id) {
        return guard.local(SalesReceipt.class, id, b, br,
                SalesReceipt::getBusiness, SalesReceipt::getBranch);
    }
    private ReceiptView receiptView(SalesReceipt v) {
        return new ReceiptView(v.getId(), v.getBusiness().getId(), v.getBranch().getId(),
                v.getReceiptSeries().getId(), v.getNumber(),
                v.getOrderBill() == null ? null : v.getOrderBill().getId(), v.getStatus(),
                v.getSubtotal(), v.getDiscount(), v.getTax(), v.getTotal());
    }

    @Override
    public List<ReceiptItemView> receiptItems(Long b, Long br, Long receiptId) {
        guard.write(b, br, "FACTURAR");
        localReceipt(b, br, receiptId);
        return em.createQuery("select i from SalesReceiptItem i where i.salesReceipt.id = :id", SalesReceiptItem.class)
                .setParameter("id", receiptId).getResultList().stream().map(this::receiptItemView).toList();
    }
    @Override @Transactional
    public ReceiptItemView addReceiptItem(ReceiptItemRequest r) {
        guard.required(r, "request");
        guard.write(r.businessId(), r.branchId(), "FACTURAR");
        SalesReceipt receipt = localReceipt(r.businessId(), r.branchId(), r.salesReceiptId());
        guard.check(receipt.getStatus() == SalesReceiptStates.BORRADOR, "El comprobante debe estar en borrador");
        guard.required(r.quantity(), "quantity");
        guard.check(r.quantity().signum() > 0, "La cantidad debe ser positiva");
        guard.check(r.orderBillItemId() != null,
                "El detalle del comprobante debe corresponder a la cuenta del pedido");
        guard.amounts(r.subtotal(), r.discount(), r.tax(), BigDecimal.ZERO, r.total());
        SalesReceiptItem item = new SalesReceiptItem(guard.business(r.businessId()),
                guard.branch(r.businessId(), r.branchId()), receipt, r.description(), r.unit(), r.quantity());
        if (r.orderBillItemId() != null) {
            OrderBillItem billItem = guard.local(OrderBillItem.class, r.orderBillItemId(),
                    r.businessId(), r.branchId(), OrderBillItem::getBusiness, OrderBillItem::getBranch);
            guard.check(receipt.getOrderBill() != null && receipt.getOrderBill().getId().equals(billItem.getOrderBill().getId()),
                    "El detalle de cuenta no corresponde al comprobante");
            guard.check(r.quantity().compareTo(billItem.getQuantity()) == 0
                            && r.subtotal().compareTo(billItem.getSubtotal()) == 0
                            && r.discount().compareTo(billItem.getDiscount()) == 0
                            && r.tax().compareTo(billItem.getTax()) == 0
                            && r.total().compareTo(billItem.getTotal()) == 0,
                    "Los importes del comprobante deben coincidir con el detalle de la cuenta");
            long duplicated = guard.count(
                    "select count(i) from SalesReceiptItem i where i.salesReceipt.id = :receipt and i.orderBillItem.id = :billItem",
                    "receipt", receipt.getId(), "billItem", billItem.getId());
            guard.conflict(duplicated > 0, "Ese detalle ya fue registrado en el comprobante");
            item.assignBillItem(billItem);
        }
        if (r.unitValue() != null) item.changeUnitValue(r.unitValue());
        if (r.taxRate() != null) item.changeTaxRate(r.taxRate());
        if (r.taxTreatmentCode() != null) item.changeTaxTreatmentCode(r.taxTreatmentCode());
        item.changeAmounts(r.subtotal(), r.discount(), r.tax(), r.total());
        em.persist(item);
        em.flush();
        return receiptItemView(item);
    }
    @Override @Transactional
    public void deleteReceiptItem(Long b, Long br, Long id) {
        guard.write(b, br, "FACTURAR");
        SalesReceiptItem item = guard.local(SalesReceiptItem.class, id, b, br,
                SalesReceiptItem::getBusiness, SalesReceiptItem::getBranch);
        guard.check(item.getSalesReceipt().getStatus() == SalesReceiptStates.BORRADOR,
                "Solo se permite editar un comprobante en borrador");
        em.remove(item);
    }
    private ReceiptItemView receiptItemView(SalesReceiptItem v) {
        return new ReceiptItemView(v.getId(), v.getSalesReceipt().getId(), v.getDescription(),
                v.getUnit(), v.getQuantity(), v.getUnitValue(), v.getTotal());
    }

    @Override
    public IssuerView issuer(Long b, Long br, Long receiptId) {
        guard.write(b, br, "FACTURAR");
        localReceipt(b, br, receiptId);
        SalesReceiptIssuerSnapshot v = em.find(SalesReceiptIssuerSnapshot.class,
                new SalesReceiptIssuerSnapshotId(b, br, receiptId));
        if (v == null) throw new ResourceNotFoundException("SalesReceiptIssuerSnapshot", "ID", receiptId);
        return issuerView(v);
    }
    @Override @Transactional
    public IssuerView createIssuer(IssuerRequest r) {
        guard.required(r, "request");
        guard.write(r.businessId(), r.branchId(), "FACTURAR");
        SalesReceipt receipt = localReceipt(r.businessId(), r.branchId(), r.salesReceiptId());
        guard.check(receipt.getStatus() == SalesReceiptStates.BORRADOR, "Solo se edita un borrador");
        guard.conflict(em.find(SalesReceiptIssuerSnapshot.class,
                        new SalesReceiptIssuerSnapshotId(r.businessId(), r.branchId(), r.salesReceiptId())) != null,
                "El emisor ya está registrado");
        SalesReceiptIssuerSnapshot v = new SalesReceiptIssuerSnapshot(guard.business(r.businessId()),
                guard.branch(r.businessId(), r.branchId()), receipt,
                r.legalName(), r.ruc(), r.address(), r.ubigeoCode());
        em.persist(v);
        em.flush();
        return issuerView(v);
    }
    private IssuerView issuerView(SalesReceiptIssuerSnapshot v) {
        return new IssuerView(v.getSalesReceipt().getId(), v.getLegalName(),
                v.getRuc(), v.getAddress(), v.getUbigeoCode());
    }
    @Override
    public CustomerSnapshotView customerSnapshot(Long b, Long br, Long receiptId) {
        guard.write(b, br, "FACTURAR");
        localReceipt(b, br, receiptId);
        SalesReceiptCustomerSnapshot v = em.find(SalesReceiptCustomerSnapshot.class,
                new SalesReceiptCustomerSnapshotId(b, br, receiptId));
        if (v == null) throw new ResourceNotFoundException("SalesReceiptCustomerSnapshot", "ID", receiptId);
        return customerView(v);
    }
    @Override @Transactional
    public CustomerSnapshotView createCustomerSnapshot(CustomerSnapshotRequest r) {
        guard.required(r, "request");
        guard.write(r.businessId(), r.branchId(), "FACTURAR");
        SalesReceipt receipt = localReceipt(r.businessId(), r.branchId(), r.salesReceiptId());
        guard.check(receipt.getStatus() == SalesReceiptStates.BORRADOR, "Solo se edita un borrador");
        guard.conflict(em.find(SalesReceiptCustomerSnapshot.class,
                        new SalesReceiptCustomerSnapshotId(r.businessId(), r.branchId(), r.salesReceiptId())) != null,
                "El cliente ya está registrado");
        SalesReceiptCustomerSnapshot v = new SalesReceiptCustomerSnapshot(guard.business(r.businessId()),
                guard.branch(r.businessId(), r.branchId()), receipt,
                r.customerName(), r.documentType(), r.documentNumber(), r.address());
        em.persist(v);
        em.flush();
        return customerView(v);
    }
    private CustomerSnapshotView customerView(SalesReceiptCustomerSnapshot v) {
        return new CustomerSnapshotView(v.getSalesReceipt().getId(), v.getCustomerName(),
                v.getDocumentType(), v.getDocumentNumber(), v.getAddress());
    }

    @Override
    public List<AttemptView> attempts(Long b, Long br, Long receiptId) {
        guard.write(b, br, "FACTURAR");
        localReceipt(b, br, receiptId);
        return em.createQuery("select a from SalesReceiptIntegrationAttempt a where a.salesReceipt.id = :id", SalesReceiptIntegrationAttempt.class)
                .setParameter("id", receiptId).getResultList().stream().map(this::attemptView).toList();
    }
    @Override @Transactional
    public AttemptView recordLocalAttempt(AttemptRequest r) {
        guard.required(r, "request");
        guard.write(r.businessId(), r.branchId(), "FACTURAR");
        guard.check("LOCAL_TEST".equals(r.provider()), "Solo se admiten registros de simulación LOCAL_TEST");
        SalesReceipt receipt = localReceipt(r.businessId(), r.branchId(), r.salesReceiptId());
        SalesReceiptIntegrationAttempt attempt = new SalesReceiptIntegrationAttempt(guard.business(r.businessId()),
                guard.branch(r.businessId(), r.branchId()), receipt, r.provider(),
                r.externalOperationId(), r.requestReference(), r.responseCode(), r.responseMessage());
        em.persist(attempt);
        em.flush();
        return attemptView(attempt);
    }
    private AttemptView attemptView(SalesReceiptIntegrationAttempt v) {
        return new AttemptView(v.getId(), v.getSalesReceipt().getId(), v.getProvider(),
                v.getResponseCode(), v.getResponseMessage());
    }
}
