package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.enums.CashShiftStates;
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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CashShift {

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
    @JoinColumn(name = "cash_register_id")
    private CashRegister cashRegister;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "responsible_employee_id")
    private Employee responsibleEmployee;

    private LocalDateTime openedAt;

    private LocalDateTime closedAt;

    private BigDecimal openingFund = new BigDecimal("0.00");

    private BigDecimal expectedCash = new BigDecimal("0.00");

    private BigDecimal countedCash = new BigDecimal("0.00");

    @Generated(event = {EventType.INSERT, EventType.UPDATE})
    private BigDecimal difference;

    @Enumerated(EnumType.STRING)
    private CashShiftStates status = CashShiftStates.ABIERTO;

    @Generated(event = {EventType.INSERT, EventType.UPDATE})
    private Long openCashRegisterId;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "authorized_by_user_business_id")
    private UserBusiness authorizedByUserBusiness;

    private String notes;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public CashShift(Business business, Branch branch, CashRegister cashRegister, Employee responsibleEmployee, LocalDateTime openedAt) {
        changeBusiness(business);
        changeBranch(branch);
        changeCashRegister(cashRegister);
        changeResponsibleEmployee(responsibleEmployee);
        changeOpenedAt(openedAt);
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

    private void changeCashRegister(CashRegister value) {
        if (value == null) {
            throw new InvalidEntityDataException("El caja register es obligatorio");
        }
        this.cashRegister = value;
    }

    private void changeResponsibleEmployee(Employee value) {
        if (value == null) {
            throw new InvalidEntityDataException("El empleado responsable es obligatorio");
        }
        this.responsibleEmployee = value;
    }

    public void changeOpenedAt(LocalDateTime value) {
        if (value == null) {
            throw new InvalidEntityDataException("El opened at es obligatorio");
        }
        this.openedAt = value;
    }

    private void changeClosedAt(LocalDateTime value) {
        this.closedAt = value;
    }

    public void changeOpeningFund(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El fondo inicial supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El fondo inicial no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El fondo inicial no puede ser negativo");
        }
        this.openingFund = value;
    }

    public void changeExpectedCash(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El efectivo esperado supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El efectivo esperado no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El efectivo esperado no puede ser negativo");
        }
        this.expectedCash = value;
    }

    public void changeCountedCash(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El efectivo contado supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El efectivo contado no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El efectivo contado no puede ser negativo");
        }
        this.countedCash = value;
    }

    private void changeStatus(CashShiftStates value) {
        this.status = value;
    }

    public void changeAuthorizedByUserBusiness(UserBusiness value) {
        this.authorizedByUserBusiness = value;
    }

    public void changeNotes(String value) {
        if (value == null || value.isBlank()) {
            this.notes = null;
            return;
        }
        String normalized = value.trim();
        this.notes = normalized;
    }

    public void closeShift(LocalDateTime closedAt, BigDecimal countedCash) {
        if (this.status != CashShiftStates.ABIERTO) {
            throw new InvalidEntityDataException("El turno de caja ya está cerrado");
        }
        if (closedAt == null) {
            throw new InvalidEntityDataException("La fecha de cierre es obligatoria");
        }
        if (closedAt.isBefore(this.openedAt)) {
            throw new InvalidEntityDataException("El cierre no puede ser anterior a la apertura");
        }
        if (countedCash == null) {
            throw new InvalidEntityDataException("El efectivo contado es obligatorio");
        }
        changeCountedCash(countedCash);
        changeClosedAt(closedAt);
        changeStatus(CashShiftStates.CERRADO);
    }
}