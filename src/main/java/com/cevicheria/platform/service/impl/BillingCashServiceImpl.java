package com.cevicheria.platform.service.impl;

import com.cevicheria.platform.dto.MovementRequest;
import com.cevicheria.platform.dto.MovementView;
import com.cevicheria.platform.dto.ShiftCloseRequest;
import com.cevicheria.platform.dto.ShiftRequest;
import com.cevicheria.platform.dto.ShiftView;
import com.cevicheria.platform.model.*;
import com.cevicheria.platform.model.enums.CashShiftStates;
import com.cevicheria.platform.model.enums.PaymentStates;
import com.cevicheria.platform.model.enums.PaymentMethod;
import com.cevicheria.platform.service.BillingCashService;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BillingCashServiceImpl implements BillingCashService {
    private final BillingSupport guard;
    private final EntityManager em;

    @Override
    public List<ShiftView> shifts(Long b, Long br) {
        guard.write(b, br, "CAJA");
        return guard.byBranch(CashShift.class, b, br).stream().map(this::shiftView).toList();
    }
    @Override
    public ShiftView shift(Long b, Long br, Long id) {
        guard.write(b, br, "CAJA");
        return shiftView(guard.local(CashShift.class, id, b, br,
                CashShift::getBusiness, CashShift::getBranch));
    }
    @Override @Transactional
    public ShiftView open(ShiftRequest r) {
        guard.required(r, "request");
        guard.write(r.businessId(), r.branchId(), "CAJA");
        CashRegister register = guard.local(CashRegister.class, r.cashRegisterId(),
                r.businessId(), r.branchId(), CashRegister::getBusiness, CashRegister::getBranch);
        guard.check(register.isActive(), "La caja está inactiva");
        Employee employee = guard.require(Employee.class, r.responsibleEmployeeId());
        guard.employeeAtBranch(employee, r.businessId(), r.branchId());
        long openShifts = guard.count("select count(s) from CashShift s where s.cashRegister.id = :reg and s.status = :state",
                "reg", register.getId(), "state", CashShiftStates.ABIERTO);
        guard.conflict(openShifts > 0, "Esta caja ya tiene un turno abierto");
        CashShift shift = new CashShift(guard.business(r.businessId()),
                guard.branch(r.businessId(), r.branchId()), register, employee, LocalDateTime.now());
        if (r.openingFund() != null) {
            guard.money(r.openingFund(), "openingFund", false);
            shift.changeOpeningFund(r.openingFund());
            shift.changeExpectedCash(r.openingFund());
        }
        shift.changeNotes(r.notes());
        em.persist(shift);
        em.flush();
        return shiftView(shift);
    }
    @Override @Transactional
    public ShiftView close(Long b, Long br, Long id, ShiftCloseRequest r) {
        guard.required(r, "request");
        guard.write(b, br, "CAJA");
        guard.money(r.countedCash(), "countedCash", false);
        CashShift shift = guard.local(CashShift.class, id, b, br,
                CashShift::getBusiness, CashShift::getBranch);
        guard.check(shift.getStatus() == CashShiftStates.ABIERTO, "El turno ya está cerrado");
        Long missing = em.createQuery(
                        "select count(p) from Payment p where p.cashShift.id = :shift "
                                + "and p.status = :status and p.paymentMethod = :method "
                                + "and not exists (select m.id from CashMovement m where m.payment.id = p.id)", Long.class)
                .setParameter("shift", shift.getId())
                .setParameter("status", PaymentStates.VERIFICADO)
                .setParameter("method", PaymentMethod.EFECTIVO)
                .getSingleResult();
        guard.check(missing == 0, "Hay cobros en efectivo verificados sin movimiento de caja");
        List<CashMovement> movements = em.createQuery(
                        "select m from CashMovement m where m.cashShift.id = :shift "
                                + "and m.financialAccount.id = :account", CashMovement.class)
                .setParameter("shift", shift.getId())
                .setParameter("account", shift.getCashRegister().getCashAccount().getId())
                .getResultList();
        BigDecimal expected = shift.getOpeningFund();
        for (CashMovement m : movements) {
            expected = "INGRESO".equals(m.getType()) ? expected.add(m.getAmount())
                    : expected.subtract(m.getAmount());
        }
        guard.check(expected.signum() >= 0, "Los movimientos superan el saldo inicial de caja");
        shift.changeExpectedCash(expected);
        shift.changeNotes(r.notes());
        shift.closeShift(LocalDateTime.now(), r.countedCash());
        em.flush();
        em.refresh(shift);
        return shiftView(shift);
    }
    private ShiftView shiftView(CashShift s) {
        return new ShiftView(s.getId(), s.getBusiness().getId(), s.getBranch().getId(),
                s.getCashRegister().getId(), s.getResponsibleEmployee().getId(), s.getStatus(),
                s.getOpenedAt(), s.getClosedAt(), s.getOpeningFund(), s.getExpectedCash(),
                s.getCountedCash(), s.getDifference());
    }

    @Override
    public List<MovementView> movements(Long b, Long br) {
        guard.write(b, br, "CAJA");
        return guard.byBranch(CashMovement.class, b, br).stream().map(this::movementView).toList();
    }
    @Override
    public MovementView movement(Long b, Long br, Long id) {
        guard.write(b, br, "CAJA");
        return movementView(guard.local(CashMovement.class, id, b, br,
                CashMovement::getBusiness, CashMovement::getBranch));
    }
    @Override @Transactional
    public MovementView recordMovement(MovementRequest r) {
        guard.required(r, "request");
        guard.write(r.businessId(), r.branchId(), "CAJA");
        guard.money(r.amount(), "amount", true);
        FinancialAccount account = guard.local(FinancialAccount.class, r.financialAccountId(),
                r.businessId(), r.branchId(), FinancialAccount::getBusiness, FinancialAccount::getBranch);
        guard.check(account.isActive(), "La cuenta financiera está inactiva");
        Employee employee = guard.require(Employee.class, r.responsibleEmployeeId());
        guard.employeeAtBranch(employee, r.businessId(), r.branchId());
        long keyExists = guard.count(
                "select count(m) from CashMovement m where m.business.id = :b and m.operationKey = :key",
                "b", r.businessId(), "key", r.operationKey());
        guard.conflict(keyExists > 0, "Ya existe un movimiento con esa clave");
        CashMovement v = new CashMovement(guard.business(r.businessId()),
                guard.branch(r.businessId(), r.branchId()), account, employee, r.type(),
                LocalDateTime.now(), r.concept(), r.operationKey());
        v.changeAmount(r.amount());
        if (r.cashShiftId() != null) {
            CashShift shift = guard.local(CashShift.class, r.cashShiftId(), r.businessId(), r.branchId(),
                    CashShift::getBusiness, CashShift::getBranch);
            guard.check(shift.getStatus() == CashShiftStates.ABIERTO, "No puedes registrar movimientos en un turno cerrado");
            guard.check(shift.getCashRegister().getCashAccount().getId().equals(account.getId()),
                    "La cuenta financiera no corresponde a la caja del turno");
            v.assignCashShift(shift);
        }
        if (r.paymentId() != null) {
            Payment p = guard.local(Payment.class, r.paymentId(), r.businessId(), r.branchId(),
                    Payment::getBusiness, Payment::getBranch);
            guard.check(p.getStatus() == PaymentStates.VERIFICADO,
                    "Solo se registran movimientos de pagos verificados");
            guard.check(p.getFinancialAccount().getId().equals(account.getId()),
                    "La cuenta financiera no coincide con el pago");
            guard.check(p.getDirection().name().equalsIgnoreCase(r.type()),
                    "El tipo de movimiento no coincide con el pago");
            guard.check(r.amount().compareTo(p.getAmount().add(p.getTip())) == 0,
                    "El movimiento debe registrar el importe del pago más su propina");
            long repeated = em.createQuery(
                            "select count(m) from CashMovement m where m.payment.id = :payment", Long.class)
                    .setParameter("payment", p.getId()).getSingleResult();
            guard.conflict(repeated > 0, "El pago ya tiene un movimiento financiero");
            if (p.getPaymentMethod() == PaymentMethod.EFECTIVO) {
                guard.check(r.cashShiftId() != null && p.getCashShift() != null
                                && p.getCashShift().getId().equals(r.cashShiftId()),
                        "El movimiento en efectivo debe usar el turno de caja del pago");
            } else {
                guard.check(r.cashShiftId() == null,
                        "Los pagos bancarios no deben afectar un turno de efectivo");
            }
            v.assignPayment(p);
        }
        em.persist(v);
        em.flush();
        return movementView(v);
    }
    private MovementView movementView(CashMovement v) {
        return new MovementView(v.getId(), v.getBusiness().getId(), v.getBranch().getId(),
                v.getFinancialAccount().getId(), v.getCashShift() == null ? null : v.getCashShift().getId(),
                v.getType(), v.getAmount(), v.getConcept(), v.getOperationKey());
    }
}
