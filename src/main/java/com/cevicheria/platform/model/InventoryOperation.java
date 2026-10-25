package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.enums.InventoryOperationStatus;
import com.cevicheria.platform.model.enums.InventoryOperationType;
import com.cevicheria.platform.model.enums.InventoryReturnResolution;
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InventoryOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Enumerated(EnumType.STRING)
    private InventoryOperationType type;

    private LocalDateTime recordDate;

    private String operationKey;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "source_operation_id")
    private InventoryOperation sourceOperation;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "order_id")
    private CustomerOrder order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "responsible_employee_id")
    private Employee responsibleEmployee;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "authorized_by_user_business_id")
    private UserBusiness authorizedByUserBusiness;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "destination_branch_id")
    private Branch destinationBranch;

    private LocalDateTime cutoffAt;

    @Enumerated(EnumType.STRING)
    private InventoryOperationStatus status = InventoryOperationStatus.BORRADOR;

    @Enumerated(EnumType.STRING)
    private InventoryReturnResolution returnResolution;

    private String reason;

    private String evidenceUrl;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public InventoryOperation(Business business, Branch branch, InventoryOperationType type, LocalDateTime recordDate, String operationKey, Employee responsibleEmployee) {
        changeBusiness(business);
        changeBranch(branch);
        changeType(type);
        changeRecordDate(recordDate);
        changeOperationKey(operationKey);
        changeResponsibleEmployee(responsibleEmployee);
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

    public void changeType(InventoryOperationType value) {
        if (value == null) {
            throw new InvalidEntityDataException("El tipo es obligatorio");
        }
        this.type = value;
    }

    private void changeRecordDate(LocalDateTime value) {
        if (value == null) {
            throw new InvalidEntityDataException("La fecha de registro es obligatoria");
        }
        this.recordDate = value;
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

    private void changeSourceOperation(InventoryOperation value) {
        this.sourceOperation = value;
    }

    private void changePurchase(Purchase value) {
        this.purchase = value;
    }

    private void changeOrder(CustomerOrder value) {
        this.order = value;
    }

    private void changeResponsibleEmployee(Employee value) {
        if (value == null) {
            throw new InvalidEntityDataException("El empleado responsable es obligatorio");
        }
        this.responsibleEmployee = value;
    }

    public void changeAuthorizedByUserBusiness(UserBusiness value) {
        this.authorizedByUserBusiness = value;
    }

    public void changeDestinationBranch(Branch value) {
        this.destinationBranch = value;
    }

    public void changeCutoffAt(LocalDateTime value) {
        this.cutoffAt = value;
    }

    public void changeStatus(InventoryOperationStatus value) {
        this.status = value;
    }

    public void changeReturnResolution(InventoryReturnResolution value) {
        this.returnResolution = value;
    }

    public void changeReason(String value) {
        if (value == null || value.isBlank()) {
            this.reason = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 250) {
            throw new InvalidEntityDataException("El reason no puede superar 250 caracteres");
        }
        this.reason = normalized;
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


    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "operation", fetch = FetchType.LAZY)
    private List<InventoryOperationDetail> operationDetails = new ArrayList<>();

    public List<InventoryOperationDetail> getOperationDetails() {
        return List.copyOf(this.operationDetails);
    }
}