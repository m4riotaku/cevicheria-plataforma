package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import jakarta.persistence.Entity;
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
public class CashMovement {

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
    @JoinColumn(name = "financial_account_id")
    private FinancialAccount financialAccount;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "cash_shift_id")
    private CashShift cashShift;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "source_movement_id")
    private CashMovement sourceMovement;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "responsible_employee_id")
    private Employee responsibleEmployee;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "authorized_by_user_business_id")
    private UserBusiness authorizedByUserBusiness;

    private String type;

    private LocalDateTime recordDate;

    private BigDecimal amount = new BigDecimal("0.00");

    private String concept;

    private String evidenceUrl;

    private String operationKey;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public CashMovement(Business business, Branch branch, FinancialAccount financialAccount, Employee responsibleEmployee, String type, LocalDateTime recordDate, String concept, String operationKey) {
        changeBusiness(business);
        changeBranch(branch);
        changeFinancialAccount(financialAccount);
        changeResponsibleEmployee(responsibleEmployee);
        changeType(type);
        changeRecordDate(recordDate);
        changeConcept(concept);
        changeOperationKey(operationKey);
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

    private void changeFinancialAccount(FinancialAccount value) {
        if (value == null) {
            throw new InvalidEntityDataException("El cuenta financiera es obligatorio");
        }
        this.financialAccount = value;
    }

    private void changeCashShift(CashShift value) {
        this.cashShift = value;
    }

    private void changePayment(Payment value) {
        this.payment = value;
    }

    private void changeSourceMovement(CashMovement value) {
        this.sourceMovement = value;
    }

    private void changeResponsibleEmployee(Employee value) {
        if (value == null) {
            throw new InvalidEntityDataException("El empleado responsable es obligatorio");
        }
        this.responsibleEmployee = value;
    }

    private void changeAuthorizedByUserBusiness(UserBusiness value) {
        this.authorizedByUserBusiness = value;
    }

    public void changeType(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El tipo es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El tipo no puede estar vacío");
        }
        String normalized = value.trim().toUpperCase(java.util.Locale.ROOT);
        if (!(normalized.equals("INGRESO") || normalized.equals("EGRESO"))) {
            throw new InvalidEntityDataException("El tipo no coincide con los valores permitidos por la base de datos");
        }
        this.type = normalized;
    }

    private void changeRecordDate(LocalDateTime value) {
        if (value == null) {
            throw new InvalidEntityDataException("La fecha de registro es obligatoria");
        }
        this.recordDate = value;
    }

    public void changeAmount(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El importe supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El importe no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El importe no puede ser negativo");
        }
        if (value != null && value.signum() <= 0) {
            throw new InvalidEntityDataException("El importe debe ser mayor que cero");
        }
        this.amount = value;
    }

    public void changeConcept(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El concept es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El concept no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 180) {
            throw new InvalidEntityDataException("El concept no puede superar 180 caracteres");
        }
        this.concept = normalized;
    }

    public void changeEvidenceUrl(String value) {
        if (value == null || value.isBlank()) {
            this.evidenceUrl = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 1000) {
            throw new InvalidEntityDataException("El URL de evidencia no puede superar 1000 caracteres");
        }
        this.evidenceUrl = normalized;
    }

    public void changeOperationKey(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El identificador de operación es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El identificador de operación no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 64) {
            throw new InvalidEntityDataException("El identificador de operación no puede superar 64 caracteres");
        }
        this.operationKey = normalized;
    }
}