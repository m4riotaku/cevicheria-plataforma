package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.enums.DocumentStates;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class ComplianceDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "previous_document_id")
    private ComplianceDocument previousDocument;

    private String type;

    private String number;

    private LocalDate issueDate;

    private LocalDate expirationDate;

    private LocalDate reviewDate;

    private String fileUrl;

    @Enumerated(EnumType.STRING)
    private DocumentStates status = DocumentStates.VIGENTE;

    private String notes;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public ComplianceDocument(Business business, Employee employee, Supplier supplier, Branch branch, String type) {
        changeBusiness(business);
        changeType(type);
        int targets = (employee != null ? 1 : 0) + (supplier != null ? 1 : 0) + (branch != null ? 1 : 0);
        if (targets == 0) {
            throw new InvalidEntityDataException("Debes indicar el empleado, proveedor o local del documento");
        }
        if (targets > 1) {
            throw new InvalidEntityDataException("El documento solo puede pertenecer a un empleado, proveedor o local");
        }
        changeEmployee(employee);
        changeSupplier(supplier);
        changeBranch(branch);
    }

    private void changeBusiness(Business value) {
        if (value == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        this.business = value;
    }

    private void changeEmployee(Employee value) {
        this.employee = value;
    }

    private void changeSupplier(Supplier value) {
        this.supplier = value;
    }

    private void changeBranch(Branch value) {
        this.branch = value;
    }

    public void changePreviousDocument(ComplianceDocument value) {
        this.previousDocument = value;
    }

    public void changeType(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El tipo es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El tipo no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 80) {
            throw new InvalidEntityDataException("El tipo no puede superar 80 caracteres");
        }
        this.type = normalized;
    }

    public void changeNumber(String value) {
        if (value == null || value.isBlank()) {
            this.number = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 80) {
            throw new InvalidEntityDataException("El número no puede superar 80 caracteres");
        }
        this.number = normalized;
    }

    private void changeIssueDate(LocalDate value) {
        this.issueDate = value;
    }

    public void changeExpirationDate(LocalDate value) {
        this.expirationDate = value;
    }

    public void changeReviewDate(LocalDate value) {
        this.reviewDate = value;
    }

    public void changeFileUrl(String value) {
        if (value == null || value.isBlank()) {
            this.fileUrl = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 1000) {
            throw new InvalidEntityDataException("El file url no puede superar 1000 caracteres");
        }
        this.fileUrl = normalized;
    }

    public void changeStatus(DocumentStates value) {
        this.status = value;
    }

    public void changeNotes(String value) {
        if (value == null || value.isBlank()) {
            this.notes = null;
            return;
        }
        String normalized = value.trim();
        this.notes = normalized;
    }
}