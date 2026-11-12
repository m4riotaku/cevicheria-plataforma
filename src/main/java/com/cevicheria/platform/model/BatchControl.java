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
public class BatchControl {

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
    @JoinColumn(name = "batch_id")
    private InventoryBatch batch;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "responsible_employee_id")
    private Employee responsibleEmployee;

    private LocalDateTime recordDate;

    private BigDecimal temperature;

    private String incident;

    private String correctiveAction;

    private String evidenceUrl;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public BatchControl(Business business, Branch branch, InventoryBatch batch, Employee responsibleEmployee, LocalDateTime recordDate) {
        changeBusiness(business);
        changeBranch(branch);
        changeBatch(batch);
        changeResponsibleEmployee(responsibleEmployee);
        changeRecordDate(recordDate);
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

    private void changeBatch(InventoryBatch value) {
        if (value == null) {
            throw new InvalidEntityDataException("El lote es obligatorio");
        }
        this.batch = value;
    }

    private void changeResponsibleEmployee(Employee value) {
        if (value == null) {
            throw new InvalidEntityDataException("El empleado responsable es obligatorio");
        }
        this.responsibleEmployee = value;
    }

    private void changeRecordDate(LocalDateTime value) {
        if (value == null) {
            throw new InvalidEntityDataException("La fecha de registro es obligatoria");
        }
        this.recordDate = value;
    }

    public void changeTemperature(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999.99")) > 0) {
            throw new InvalidEntityDataException("La temperature supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("La temperature no admite más de 2 decimales");
        }
        this.temperature = value;
    }

    public void changeIncident(String value) {
        if (value == null || value.isBlank()) {
            this.incident = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 250) {
            throw new InvalidEntityDataException("El incident no puede superar 250 caracteres");
        }
        this.incident = normalized;
    }

    public void changeCorrectiveAction(String value) {
        if (value == null || value.isBlank()) {
            this.correctiveAction = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 250) {
            throw new InvalidEntityDataException("El corrective action no puede superar 250 caracteres");
        }
        this.correctiveAction = normalized;
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
}
