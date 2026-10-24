package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.id.InventoryOperationDetailId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.MapsId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InventoryOperationDetail {

    @EmbeddedId
    private InventoryOperationDetailId id;

    private String documentReference;

    private String transportProvider;

    private String vehiclePlate;

    private LocalDateTime expectedArrivalAt;

    private String additionalNotes;

    @MapsId("businessId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @MapsId("branchId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @MapsId("operationId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "operation_id")
    private InventoryOperation operation;

    public InventoryOperationDetail(
            Business business,
            Branch branch,
            InventoryOperation operation,
            String documentReference,
            String transportProvider,
            String vehiclePlate,
            LocalDateTime expectedArrivalAt,
            String additionalNotes) {
        this.id = new InventoryOperationDetailId(null, null, null);
        changeBusiness(business);
        changeBranch(branch);
        changeOperation(operation);
        changeDocumentReference(documentReference);
        changeTransportProvider(transportProvider);
        changeVehiclePlate(vehiclePlate);
        changeExpectedArrivalAt(expectedArrivalAt);
        changeAdditionalNotes(additionalNotes);
        if (business.getId() != null && branch.getBusiness() != null
                && branch.getBusiness().getId() != null
                && !business.getId().equals(branch.getBusiness().getId())) {
            throw new InvalidEntityDataException("El local no pertenece al negocio seleccionado");
        }
    }

    private void changeBusiness(Business business) {
        if (business == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        if (business.getId() == null) {
            throw new InvalidEntityDataException("El negocio debe estar registrado antes de realizar la asociación");
        }
        this.business = business;
        this.id = new InventoryOperationDetailId(business.getId(), this.id.getBranchId(), this.id.getOperationId());
    }

    private void changeBranch(Branch branch) {
        if (branch == null) {
            throw new InvalidEntityDataException("El local es obligatorio");
        }
        if (branch.getId() == null) {
            throw new InvalidEntityDataException("El local debe estar registrado antes de realizar la asociación");
        }
        this.branch = branch;
        this.id = new InventoryOperationDetailId(this.id.getBusinessId(), branch.getId(), this.id.getOperationId());
    }

    private void changeOperation(InventoryOperation operation) {
        if (operation == null) {
            throw new InvalidEntityDataException("La operación de inventario es obligatoria");
        }
        if (operation.getId() == null) {
            throw new InvalidEntityDataException("La operación de inventario debe estar registrada antes de realizar la asociación");
        }
        this.operation = operation;
        this.id = new InventoryOperationDetailId(this.id.getBusinessId(), this.id.getBranchId(), operation.getId());
    }

    public void changeDocumentReference(String documentReference) {
        if (documentReference == null || documentReference.isBlank()) {
            this.documentReference = null;
            return;
        }
        String normalizedValue = documentReference.trim();
        if (normalizedValue.length() > 100) {
            throw new InvalidEntityDataException("El documento de referencia no puede superar 100 caracteres");
        }
        this.documentReference = normalizedValue;
    }

    public void changeTransportProvider(String transportProvider) {
        if (transportProvider == null || transportProvider.isBlank()) {
            this.transportProvider = null;
            return;
        }
        String normalizedValue = transportProvider.trim();
        if (normalizedValue.length() > 150) {
            throw new InvalidEntityDataException("El proveedor de transporte no puede superar 150 caracteres");
        }
        this.transportProvider = normalizedValue;
    }

    public void changeVehiclePlate(String vehiclePlate) {
        if (vehiclePlate == null || vehiclePlate.isBlank()) {
            this.vehiclePlate = null;
            return;
        }
        String normalizedValue = vehiclePlate.trim();
        if (normalizedValue.length() > 20) {
            throw new InvalidEntityDataException("La placa del vehículo no puede superar 20 caracteres");
        }
        this.vehiclePlate = normalizedValue;
    }

    public void changeExpectedArrivalAt(LocalDateTime expectedArrivalAt) {
        this.expectedArrivalAt = expectedArrivalAt;
    }

    public void changeAdditionalNotes(String additionalNotes) {
        if (additionalNotes == null || additionalNotes.isBlank()) {
            this.additionalNotes = null;
            return;
        }
        String normalizedValue = additionalNotes.trim();
        this.additionalNotes = normalizedValue;
    }
}
