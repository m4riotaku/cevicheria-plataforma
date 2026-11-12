package com.cevicheria.platform.service.impl;

import com.cevicheria.platform.dto.ApplicationRequest;
import com.cevicheria.platform.dto.ApplicationView;
import com.cevicheria.platform.dto.PaymentRequest;
import com.cevicheria.platform.dto.PaymentView;
import com.cevicheria.platform.handlerException.BadRequestException;
import com.cevicheria.platform.model.CashShift;
import com.cevicheria.platform.model.Customer;
import com.cevicheria.platform.model.FinancialAccount;
import com.cevicheria.platform.model.OrderBill;
import com.cevicheria.platform.model.Payment;
import com.cevicheria.platform.model.PaymentApplication;
import com.cevicheria.platform.model.PurchaseInstallment;
import com.cevicheria.platform.model.Reservation;
import com.cevicheria.platform.model.Supplier;
import com.cevicheria.platform.model.UserBusiness;
import com.cevicheria.platform.model.enums.CashShiftStates;
import com.cevicheria.platform.model.enums.FinancialAccountType;
import com.cevicheria.platform.model.enums.OrderBillStates;
import com.cevicheria.platform.model.enums.OrderStatus;
import com.cevicheria.platform.model.enums.PaymentApplicationStates;
import com.cevicheria.platform.model.enums.PaymentConcept;
import com.cevicheria.platform.model.enums.PaymentDirection;
import com.cevicheria.platform.model.enums.PaymentMethod;
import com.cevicheria.platform.model.enums.PaymentStates;
import com.cevicheria.platform.model.enums.PurchaseStatus;
import com.cevicheria.platform.model.enums.ReservationStatus;
import com.cevicheria.platform.model.enums.SalesReceiptStates;
import com.cevicheria.platform.repository.UserBusinessRepository;
import com.cevicheria.platform.service.BillingPaymentsService;
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
public class BillingPaymentsServiceImpl implements BillingPaymentsService {
    private final BillingSupport guard;
    private final EntityManager em;
    private final UserBusinessRepository userBusinesses;

    @Override
    public List<PaymentView> payments(Long b, Long br) {
        guard.write(b, br, "COBRAR");
        return guard.byBranch(Payment.class, b, br).stream().map(this::paymentView).toList();
    }
    @Override
    public PaymentView payment(Long b, Long br, Long id) {
        guard.write(b, br, "COBRAR");
        return paymentView(guard.local(Payment.class, id, b, br,
                Payment::getBusiness, Payment::getBranch));
    }
    @Override @Transactional
    public PaymentView createPayment(PaymentRequest r) {
        guard.required(r, "request");
        guard.required(r.direction(), "direction");
        guard.required(r.concept(), "concept");
        guard.required(r.paymentMethod(), "paymentMethod");
        guard.check(r.concept() == PaymentConcept.VENTA || r.concept() == PaymentConcept.INCENTIVO
                        || r.concept() == PaymentConcept.COMPRA || r.concept() == PaymentConcept.GARANTIA,
                "Concepto no habilitado para este flujo de pagos");
        guard.check(((r.concept() == PaymentConcept.VENTA || r.concept() == PaymentConcept.GARANTIA) && r.direction() == PaymentDirection.INGRESO)
                        || ((r.concept() == PaymentConcept.INCENTIVO || r.concept() == PaymentConcept.COMPRA) && r.direction() == PaymentDirection.EGRESO),
                "La dirección no coincide con el concepto del pago");
        guard.write(r.businessId(), r.branchId(), r.direction() == PaymentDirection.INGRESO ? "COBRAR" : "AUTORIZAR");
        guard.money(r.amount(), "amount", true);
        guard.money(r.tip() == null ? BigDecimal.ZERO : r.tip(), "tip", false);
        guard.required(r.amountReceived(), "amountReceived");
        guard.required(r.changeAmount(), "changeAmount");
        long keys = guard.count("select count(p) from Payment p where p.business.id = :b and p.operationKey = :key",
                "b", r.businessId(), "key", r.operationKey());
        guard.conflict(keys > 0, "Ya existe un pago con esa operación");
        FinancialAccount account = guard.local(FinancialAccount.class, r.financialAccountId(),
                r.businessId(), r.branchId(), FinancialAccount::getBusiness, FinancialAccount::getBranch);
        guard.check(account.isActive(), "La cuenta financiera está inactiva");
        if (r.paymentMethod() == PaymentMethod.EFECTIVO) {
            guard.check(account.getType() == FinancialAccountType.EFECTIVO, "Efectivo requiere cuenta tipo EFECTIVO");
        } else {
            guard.check(account.getType() != FinancialAccountType.EFECTIVO, "El medio de pago no corresponde a una cuenta de efectivo");
        }
        Payment p = new Payment(guard.business(r.businessId()), guard.branch(r.businessId(), r.branchId()),
                r.operationKey(), r.direction(), r.concept(), r.paymentMethod(), account,
                LocalDateTime.now(), r.amount(), r.tip() == null ? BigDecimal.ZERO : r.tip(),
                r.amountReceived(), r.changeAmount());
        if (r.cashShiftId() != null) {
            CashShift shift = guard.local(CashShift.class, r.cashShiftId(), r.businessId(), r.branchId(),
                    CashShift::getBusiness, CashShift::getBranch);
            guard.check(shift.getStatus() == CashShiftStates.ABIERTO, "El turno de caja está cerrado");
            guard.check(r.paymentMethod() == PaymentMethod.EFECTIVO,
                    "Solo los pagos en efectivo se asocian a turnos de caja");
            guard.check(shift.getCashRegister().getCashAccount().getId().equals(account.getId()),
                    "La cuenta no coincide con el turno de caja");
            p.changeCashShift(shift);
        } else if (r.paymentMethod() == PaymentMethod.EFECTIVO) {
            throw new BadRequestException("El pago en efectivo requiere un turno abierto");
        }
        if (r.customerId() != null) {
            Customer c = guard.require(Customer.class, r.customerId());
            guard.businessScope(c.getBusiness(), r.businessId());
            p.assignCustomer(c);
        }
        if (r.concept() == PaymentConcept.COMPRA) {
            guard.required(r.supplierId(), "supplierId");
            Supplier supplier = guard.require(Supplier.class, r.supplierId());
            guard.businessScope(supplier.getBusiness(), r.businessId());
            guard.check(supplier.isActive(), "El proveedor está inactivo");
            p.changeSupplier(supplier);
        } else guard.check(r.supplierId() == null, "supplierId solo corresponde a pagos de compra");
        if (r.externalReference() != null) p.changeExternalReference(r.externalReference());
        em.persist(p);
        em.flush();
        return paymentView(p);
    }
    @Override @Transactional
    public PaymentView verifyPayment(Long b, Long br, Long id) {
        Payment p = guard.local(Payment.class, id, b, br, Payment::getBusiness, Payment::getBranch);
        guard.write(b, br, p.getDirection() == PaymentDirection.INGRESO ? "COBRAR" : "AUTORIZAR");
        guard.check(p.getStatus() == PaymentStates.PENDIENTE, "Solo se verifica un pago pendiente");
        UserBusiness current = userBusinesses.findByBusinessIdAndUserAccountId(b, guard.userId())
                .filter(UserBusiness::isActive)
                .orElseThrow(() -> new BadRequestException(
                        "El verificador debe pertenecer al negocio"));
        p.verifyPayment(LocalDateTime.now(), current);
        return paymentView(p);
    }
    private PaymentView paymentView(Payment p) {
        return new PaymentView(p.getId(), p.getBusiness().getId(), p.getBranch().getId(),
                p.getOperationKey(), p.getPaymentMethod(), p.getDirection(), p.getStatus(),
                p.getAmount(), p.getTip(), p.getAmountReceived(), p.getChangeAmount());
    }

    @Override
    public List<ApplicationView> applications(Long b, Long br) {
        guard.write(b, br, "COBRAR");
        return guard.byBranch(PaymentApplication.class, b, br).stream().map(this::applicationView).toList();
    }
    @Override
    public ApplicationView application(Long b, Long br, Long id) {
        guard.write(b, br, "COBRAR");
        return applicationView(guard.local(PaymentApplication.class, id, b, br,
                PaymentApplication::getBusiness, PaymentApplication::getBranch));
    }
    @Override @Transactional
    public ApplicationView createApplication(ApplicationRequest r) {
        guard.required(r, "request");
        guard.write(r.businessId(), r.branchId(), "COBRAR");
        guard.money(r.amount(), "amount", true);
        Payment p = guard.local(Payment.class, r.paymentId(), r.businessId(), r.branchId(),
                Payment::getBusiness, Payment::getBranch);
        em.lock(p, LockModeType.PESSIMISTIC_WRITE);
        guard.check(p.getStatus() == PaymentStates.VERIFICADO, "El pago debe estar verificado");
        Long registeredMovement = em.createQuery(
                        "select count(m) from CashMovement m where m.payment.id = :payment", Long.class)
                .setParameter("payment", p.getId()).getSingleResult();
        guard.check(registeredMovement > 0, "Registra primero el movimiento financiero del pago");
        if (r.purchaseInstallmentId() != null) {
            guard.check(p.getDirection() == PaymentDirection.EGRESO,
                    "Las cuotas de compra requieren un pago de egreso");
        } else {
            guard.check(p.getDirection() == PaymentDirection.INGRESO,
                    "Las cuentas de venta y reservas requieren un pago de ingreso");
        }
        int destinations = (r.orderBillId() == null ? 0 : 1) + (r.purchaseInstallmentId() == null ? 0 : 1)
                + (r.reservationId() == null ? 0 : 1);
        guard.check(destinations == 1, "Es necesario indicar exactamente un destino");
        OrderBill bill = null;
        PurchaseInstallment installment = null;
        Reservation reservation = null;
        if (r.orderBillId() != null) {
            bill = guard.local(OrderBill.class, r.orderBillId(), r.businessId(), r.branchId(),
                    OrderBill::getBusiness, OrderBill::getBranch);
            em.lock(bill, LockModeType.PESSIMISTIC_WRITE);
            guard.check(bill.getStatus() == OrderBillStates.ABIERTA, "La cuenta no está abierta");
            guard.check(bill.getOrder().getStatus() != OrderStatus.CERRADO, "El pedido ya está cerrado");
            guard.check(p.getConcept() == PaymentConcept.VENTA, "La cuenta requiere un pago de VENTA");
        }
        if (r.purchaseInstallmentId() != null) {
            installment = guard.local(PurchaseInstallment.class, r.purchaseInstallmentId(), r.businessId(), r.branchId(),
                    PurchaseInstallment::getBusiness, PurchaseInstallment::getBranch);
            em.lock(installment, LockModeType.PESSIMISTIC_WRITE);
            em.lock(installment.getPurchase(), LockModeType.PESSIMISTIC_WRITE);
            guard.check(installment.getPurchase().getStatus() == PurchaseStatus.CONFIRMADA, "La compra debe estar confirmada");
            guard.check(p.getConcept() == PaymentConcept.COMPRA, "La cuota requiere un pago de COMPRA");
            guard.check(p.getSupplier() != null && p.getSupplier().getId().equals(installment.getPurchase().getSupplier().getId()),
                    "El pago pertenece a otro proveedor");
            BigDecimal paid = appliedToInstallment(installment.getId());
            guard.check(paid.add(r.amount()).compareTo(installment.getAmount()) <= 0, "El pago excede el saldo de la cuota");
            BigDecimal purchasePaid = em.createQuery("select coalesce(sum(a.amount),0) from PaymentApplication a "
                            + "where a.purchaseInstallment.purchase.id = :id and a.status = :s", BigDecimal.class)
                    .setParameter("id", installment.getPurchase().getId()).setParameter("s", PaymentApplicationStates.APLICADA).getSingleResult();
            guard.check(purchasePaid.add(r.amount()).compareTo(installment.getPurchase().getTotal()) <= 0,
                    "Las aplicaciones superan el total de la compra");
        }
        if (r.reservationId() != null) {
            reservation = guard.local(Reservation.class, r.reservationId(), r.businessId(), r.branchId(),
                    Reservation::getBusiness, Reservation::getBranch);
            em.lock(reservation, LockModeType.PESSIMISTIC_WRITE);
            guard.check(reservation.getStatus() == ReservationStatus.PENDIENTE || reservation.getStatus() == ReservationStatus.CONFIRMADA,
                    "La reserva no admite garantías en su estado actual");
            guard.check(p.getConcept() == PaymentConcept.GARANTIA, "La reserva requiere un pago de GARANTIA");
            guard.check(appliedToReservation(reservation.getId()).add(r.amount()).compareTo(reservation.getRequiredDeposit()) <= 0,
                    "El pago excede la garantía requerida");
        }
        long unique = guard.count("select count(a) from PaymentApplication a where a.business.id = :b and a.applicationKey = :key",
                "b", r.businessId(), "key", r.applicationKey());
        guard.conflict(unique > 0, "Ya existe una aplicación con esa operación");
        BigDecimal alreadyApplied = em.createQuery(
                        "select coalesce(sum(a.amount), 0) from PaymentApplication a where a.payment.id = :id and a.status = :status", BigDecimal.class)
                .setParameter("id", p.getId()).setParameter("status", PaymentApplicationStates.APLICADA)
                .getSingleResult();
        guard.check(alreadyApplied.add(r.amount()).compareTo(p.getAmount()) <= 0,
                "La suma de aplicaciones supera el importe del pago");
        if (bill != null) {
            BigDecimal paid = em.createQuery(
                            "select coalesce(sum(a.amount), 0) from PaymentApplication a where a.orderBill.id = :id and a.status = :status", BigDecimal.class)
                    .setParameter("id", bill.getId()).setParameter("status", PaymentApplicationStates.APLICADA)
                    .getSingleResult();
            guard.check(paid.add(r.amount()).compareTo(bill.getTotal()) <= 0,
                    "El pago excede el saldo pendiente de la cuenta");
        }
        PaymentApplication application = new PaymentApplication(guard.business(r.businessId()),
                guard.branch(r.businessId(), r.branchId()), p, bill, installment, reservation,
                r.amount(), r.applicationKey());
        em.persist(application);
        em.flush();
        if (bill != null) {
            BigDecimal nowPaid = em.createQuery(
                            "select coalesce(sum(a.amount), 0) from PaymentApplication a where a.orderBill.id = :id and a.status = :status", BigDecimal.class)
                    .setParameter("id", bill.getId()).setParameter("status", PaymentApplicationStates.APLICADA)
                    .getSingleResult();
            if (nowPaid.compareTo(bill.getTotal()) == 0) bill.changeStatus(OrderBillStates.PAGADA);
        }
        if (reservation != null && appliedToReservation(reservation.getId()).compareTo(reservation.getRequiredDeposit()) == 0)
            reservation.changeStatus(ReservationStatus.CONFIRMADA);
        return applicationView(application);
    }
    @Override @Transactional
    public ApplicationView reverseApplication(Long b, Long br, Long id) {
        guard.write(b, br, "AUTORIZAR");
        PaymentApplication app = guard.local(PaymentApplication.class, id, b, br,
                PaymentApplication::getBusiness, PaymentApplication::getBranch);
        em.lock(app.getPayment(), LockModeType.PESSIMISTIC_WRITE);
        em.lock(app, LockModeType.PESSIMISTIC_WRITE);
        guard.check(app.getStatus() == PaymentApplicationStates.APLICADA, "La aplicación ya está revertida");
        if (app.getOrderBill() != null) {
            em.lock(app.getOrderBill(), LockModeType.PESSIMISTIC_WRITE);
            em.refresh(app.getOrderBill().getOrder(), LockModeType.PESSIMISTIC_WRITE);
            guard.conflict(app.getOrderBill().getOrder().getStatus() == OrderStatus.CERRADO,
                    "No se puede revertir un pago de un pedido cerrado");
            long receiptCount = guard.count(
                    "select count(r) from SalesReceipt r where r.orderBill.id = :bill and r.status <> :cancelled",
                    "bill", app.getOrderBill().getId(), "cancelled", SalesReceiptStates.ANULADO);
            guard.conflict(receiptCount > 0,
                    "No se revierte un pago de una cuenta con comprobante; requiere anulación formal");
        }
        if (app.getReservation() != null) {
            em.lock(app.getReservation(), LockModeType.PESSIMISTIC_WRITE);
            guard.conflict(app.getReservation().getStatus() == ReservationStatus.ATENDIDA, "La reserva ya fue atendida");
            if (app.getReservation().getStatus() == ReservationStatus.CONFIRMADA)
                app.getReservation().changeStatus(ReservationStatus.PENDIENTE);
        }
        app.changeStatus(PaymentApplicationStates.REVERTIDA);
        if (app.getOrderBill() != null) app.getOrderBill().changeStatus(OrderBillStates.ABIERTA);
        return applicationView(app);
    }
    private BigDecimal appliedToInstallment(Long id) {
        return em.createQuery("select coalesce(sum(a.amount),0) from PaymentApplication a where a.purchaseInstallment.id = :id and a.status = :s", BigDecimal.class)
                .setParameter("id", id).setParameter("s", PaymentApplicationStates.APLICADA).getSingleResult();
    }
    private BigDecimal appliedToReservation(Long id) {
        return em.createQuery("select coalesce(sum(a.amount),0) from PaymentApplication a where a.reservation.id = :id and a.status = :s", BigDecimal.class)
                .setParameter("id", id).setParameter("s", PaymentApplicationStates.APLICADA).getSingleResult();
    }
    private ApplicationView applicationView(PaymentApplication a) {
        return new ApplicationView(a.getId(), a.getBusiness().getId(), a.getBranch().getId(),
                a.getPayment().getId(), a.getOrderBill() == null ? null : a.getOrderBill().getId(),
                a.getAmount(), a.getStatus(), a.getPurchaseInstallment() == null ? null : a.getPurchaseInstallment().getId(),
                a.getReservation() == null ? null : a.getReservation().getId());
    }
}
