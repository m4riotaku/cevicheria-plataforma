package com.cevicheria.platform.service.impl;

import com.cevicheria.platform.dto.IncentiveRequest;
import com.cevicheria.platform.dto.IncentiveView;
import com.cevicheria.platform.dto.SettlementRequest;
import com.cevicheria.platform.dto.SettlementView;
import com.cevicheria.platform.model.*;
import com.cevicheria.platform.model.enums.*;
import com.cevicheria.platform.repository.UserBusinessRepository;
import com.cevicheria.platform.service.BillingStaffService;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BillingStaffServiceImpl implements BillingStaffService {
    private final BillingSupport guard;
    private final EntityManager em;
    private final UserBusinessRepository userBusinesses;

    @Override
    public List<SettlementView> settlements(Long b, Long br) {
        guard.write(b, br, "AUTORIZAR");
        return guard.byBranch(EmployeeSettlement.class, b, br).stream().map(this::settlementView).toList();
    }
    @Override
    public SettlementView settlement(Long b, Long br, Long id) {
        guard.write(b, br, "AUTORIZAR");
        return settlementView(localSettlement(b, br, id));
    }
    @Override @Transactional
    public SettlementView createSettlement(SettlementRequest r) {
        guard.required(r, "request");
        guard.write(r.businessId(), r.branchId(), "AUTORIZAR");
        guard.required(r.periodStart(), "periodStart");
        guard.required(r.periodEnd(), "periodEnd");
        guard.check(!r.periodEnd().isBefore(r.periodStart()), "Periodo de liquidación inválido");
        guard.money(r.total(), "total", false);
        Employee employee = guard.require(Employee.class, r.employeeId());
        guard.employeeAtBranch(employee, r.businessId(), r.branchId());
        EmployeeSettlement v = new EmployeeSettlement(guard.business(r.businessId()),
                guard.branch(r.businessId(), r.branchId()), employee, r.periodStart(), r.periodEnd());
        v.changeTotal(r.total());
        em.persist(v);
        em.flush();
        return settlementView(v);
    }
    @Override @Transactional
    public SettlementView approveSettlement(Long b, Long br, Long id) {
        guard.write(b, br, "AUTORIZAR");
        EmployeeSettlement v = localSettlement(b, br, id);
        guard.check(v.getStatus() == EmployeeSettlementStates.BORRADOR, "La liquidación ya no es borrador");
        BigDecimal sum = em.createQuery(
                        "select coalesce(sum(i.amount), 0) from EmployeeIncentive i where i.settlement.id = :id", BigDecimal.class)
                .setParameter("id", id).getSingleResult();
        guard.check(sum.compareTo(v.getTotal()) == 0 && sum.signum() > 0,
                "Los incentivos vinculados deben sumar exactamente el total de la liquidación");
        UserBusiness member = userBusinesses.findByBusinessIdAndUserAccountId(b, guard.userId())
                .filter(UserBusiness::isActive)
                .orElseThrow(() -> new com.cevicheria.platform.handlerException.BadRequestException(
                        "El aprobador debe pertenecer al negocio"));
        v.changeApprovedByUserBusiness(member);
        v.changeStatus(EmployeeSettlementStates.APROBADA);
        return settlementView(v);
    }
    @Override @Transactional
    public SettlementView paySettlement(Long b, Long br, Long id, Long paymentId) {
        guard.write(b, br, "AUTORIZAR");
        EmployeeSettlement v = localSettlement(b, br, id);
        guard.check(v.getStatus() == EmployeeSettlementStates.APROBADA,
                "La liquidación debe estar aprobada");
        Payment payment = guard.local(Payment.class, paymentId, b, br,
                Payment::getBusiness, Payment::getBranch);
        guard.check(payment.getStatus() == PaymentStates.VERIFICADO
                        && payment.getDirection() == PaymentDirection.EGRESO
                        && payment.getConcept() == PaymentConcept.INCENTIVO
                        && payment.getAmount().compareTo(v.getTotal()) == 0,
                "Se requiere un egreso de incentivo verificado por el importe exacto");
        long uses = guard.count("select count(s) from EmployeeSettlement s where s.payment.id = :id and s.business.id = :b",
                "id", paymentId, "b", b);
        guard.conflict(uses > 0, "El pago ya se utilizó en otra liquidación");
        v.assignPayment(payment);
        v.changeStatus(EmployeeSettlementStates.PAGADA);
        return settlementView(v);
    }
    private EmployeeSettlement localSettlement(Long b, Long br, Long id) {
        return guard.local(EmployeeSettlement.class, id, b, br,
                EmployeeSettlement::getBusiness, EmployeeSettlement::getBranch);
    }
    private SettlementView settlementView(EmployeeSettlement v) {
        return new SettlementView(v.getId(), v.getBusiness().getId(), v.getBranch().getId(),
                v.getEmployee().getId(), v.getPeriodStart(), v.getPeriodEnd(),
                v.getTotal(), v.getStatus());
    }

    @Override
    public List<IncentiveView> incentives(Long b, Long br) {
        guard.write(b, br, "AUTORIZAR");
        return guard.byBranch(EmployeeIncentive.class, b, br).stream().map(this::incentiveView).toList();
    }
    @Override
    public IncentiveView incentive(Long b, Long br, Long id) {
        guard.write(b, br, "AUTORIZAR");
        return incentiveView(guard.local(EmployeeIncentive.class, id, b, br,
                EmployeeIncentive::getBusiness, EmployeeIncentive::getBranch));
    }
    @Override @Transactional
    public IncentiveView createIncentive(IncentiveRequest r) {
        guard.required(r, "request");
        guard.write(r.businessId(), r.branchId(), "AUTORIZAR");
        guard.money(r.amount(), "amount", true);
        guard.required(r.type(), "type");
        guard.required(r.criterion(), "criterion");
        guard.required(r.recordDate(), "recordDate");
        guard.check(r.operationKey() != null && !r.operationKey().isBlank() && r.operationKey().length() <= 100,
                "La clave de operación es obligatoria (máximo 100 caracteres)");
        long existing = guard.count("select count(i) from EmployeeIncentive i where i.business.id = :b and i.operationKey = :key",
                "b", r.businessId(), "key", r.operationKey());
        guard.conflict(existing > 0, "Ya existe un incentivo con esta clave");
        Employee employee = guard.require(Employee.class, r.employeeId());
        guard.employeeAtBranch(employee, r.businessId(), r.branchId());
        EmployeeIncentive value = new EmployeeIncentive(guard.business(r.businessId()),
                guard.branch(r.businessId(), r.branchId()), employee,
                r.type(), r.recordDate(), r.amount(), r.criterion(), r.operationKey());
        value.setNotes(r.notes());
        if (r.type() == EmployeeIncentiveType.PROPINA) {
            guard.check(r.sourcePaymentId() != null && r.orderItemId() == null,
                    "Una propina necesita sourcePaymentId y no orderItemId");
        } else {
            guard.check(r.orderItemId() != null && r.sourcePaymentId() == null,
                    "Una comisión necesita orderItemId y no sourcePaymentId");
            OrderItem item = guard.local(OrderItem.class, r.orderItemId(), r.businessId(), r.branchId(),
                    OrderItem::getBusiness, OrderItem::getBranch);
            value.setOrderItem(item);
        }
        if (r.sourcePaymentId() != null) {
            Payment p = guard.local(Payment.class, r.sourcePaymentId(), r.businessId(), r.branchId(),
                    Payment::getBusiness, Payment::getBranch);
            guard.check(p.getStatus() == PaymentStates.VERIFICADO
                            && p.getDirection() == PaymentDirection.INGRESO,
                    "La propina requiere un ingreso verificado");
            BigDecimal alreadyAssigned = em.createQuery(
                            "select coalesce(sum(i.amount), 0) from EmployeeIncentive i where i.sourcePayment.id = :payment and i.type = :type",
                            BigDecimal.class)
                    .setParameter("payment", p.getId())
                    .setParameter("type", EmployeeIncentiveType.PROPINA)
                    .getSingleResult();
            guard.check(alreadyAssigned.add(r.amount()).compareTo(p.getTip()) <= 0,
                    "El total de propinas asignadas supera la propina del pago");
            value.setSourcePayment(p);
        }
        if (r.commissionRuleId() != null) {
            CommissionRule rule = guard.require(CommissionRule.class, r.commissionRuleId());
            guard.businessScope(rule.getBusiness(), r.businessId());
            value.setCommissionRule(rule);
        }
        if (r.settlementId() != null) {
            EmployeeSettlement settlement = localSettlement(r.businessId(), r.branchId(), r.settlementId());
            guard.check(settlement.getStatus() == EmployeeSettlementStates.BORRADOR,
                    "La liquidación debe estar en borrador");
            guard.check(settlement.getEmployee().getId().equals(employee.getId()),
                    "El incentivo pertenece a otro empleado");
            guard.check(!r.recordDate().isBefore(settlement.getPeriodStart())
                            && !r.recordDate().isAfter(settlement.getPeriodEnd()),
                    "El incentivo no está dentro del periodo de liquidación");
            value.setSettlement(settlement);
        }
        em.persist(value);
        em.flush();
        return incentiveView(value);
    }
    private IncentiveView incentiveView(EmployeeIncentive v) {
        return new IncentiveView(v.getId(), v.getBusiness().getId(), v.getBranch().getId(),
                v.getEmployee().getId(), v.getType(), v.getAmount(), v.getCriterion(), v.getOperationKey());
    }
}
