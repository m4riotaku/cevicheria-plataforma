package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.enums.EmployeeSettlementStates;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployeeSettlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private LocalDate periodStart;

    private LocalDate periodEnd;

    private BigDecimal total = new BigDecimal("0.00");

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Enumerated(EnumType.STRING)
    private EmployeeSettlementStates status = EmployeeSettlementStates.BORRADOR;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "approved_by_user_business_id")
    private UserBusiness approvedByUserBusiness;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public EmployeeSettlement(Business business, Branch branch, Employee employee, LocalDate periodStart, LocalDate periodEnd) {
        changeBusiness(business);
        changeBranch(branch);
        changeEmployee(employee);
        changePeriodStart(periodStart);
        changePeriodEnd(periodEnd);
    }

    private void changeBusiness(Business value) {
        if (value == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        this.business = value;
    }

    private void changeBranch(Branch value) {
        if (value == null) {
            throw new InvalidEntityDataException("El local es obligatorio");
        }
        this.branch = value;
    }

    private void changeEmployee(Employee value) {
        if (value == null) {
            throw new InvalidEntityDataException("El empleado es obligatorio");
        }
        this.employee = value;
    }

    public void changePeriodStart(LocalDate value) {
        if (value == null) {
            throw new InvalidEntityDataException("El period start es obligatorio");
        }
        this.periodStart = value;
    }

    public void changePeriodEnd(LocalDate value) {
        if (value == null) {
            throw new InvalidEntityDataException("El period end es obligatorio");
        }
        this.periodEnd = value;
    }

    public void changeTotal(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El total supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El total no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El total no puede ser negativo");
        }
        this.total = value;
    }

    private void changePayment(Payment value) {
        this.payment = value;
    }

    public void changeStatus(EmployeeSettlementStates value) {
        this.status = value;
    }

    public void changeApprovedByUserBusiness(UserBusiness value) {
        this.approvedByUserBusiness = value;
    }
}