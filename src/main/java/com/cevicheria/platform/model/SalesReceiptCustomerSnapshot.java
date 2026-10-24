package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.id.SalesReceiptCustomerSnapshotId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.MapsId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalesReceiptCustomerSnapshot {

    @EmbeddedId
    private SalesReceiptCustomerSnapshotId id;

    private String customerName;

    private String documentType;

    private String documentNumber;

    private String address;

    @MapsId("businessId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @MapsId("branchId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @MapsId("salesReceiptId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sales_receipt_id")
    private SalesReceipt salesReceipt;

    public SalesReceiptCustomerSnapshot(
            Business business,
            Branch branch,
            SalesReceipt salesReceipt,
            String customerName,
            String documentType,
            String documentNumber,
            String address) {
        this.id = new SalesReceiptCustomerSnapshotId(null, null, null);
        changeBusiness(business);
        changeBranch(branch);
        changeSalesReceipt(salesReceipt);
        changeCustomerName(customerName);
        changeDocumentType(documentType);
        changeDocumentNumber(documentNumber);
        changeAddress(address);
        if (business.getId() != null && branch.getBusiness() != null
                && branch.getBusiness().getId() != null
                && !business.getId().equals(branch.getBusiness().getId())) {
            throw new InvalidEntityDataException("El local no pertenece al negocio seleccionado");
        }
        validateDocumentPair(this.documentType, this.documentNumber);
    }

    private void changeBusiness(Business business) {
        if (business == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        if (business.getId() == null) {
            throw new InvalidEntityDataException("El negocio debe estar registrado antes de realizar la asociación");
        }
        this.business = business;
        this.id = new SalesReceiptCustomerSnapshotId(business.getId(), this.id.getBranchId(), this.id.getSalesReceiptId());
    }

    private void changeBranch(Branch branch) {
        if (branch == null) {
            throw new InvalidEntityDataException("El local es obligatorio");
        }
        if (branch.getId() == null) {
            throw new InvalidEntityDataException("El local debe estar registrado antes de realizar la asociación");
        }
        this.branch = branch;
        this.id = new SalesReceiptCustomerSnapshotId(this.id.getBusinessId(), branch.getId(), this.id.getSalesReceiptId());
    }

    private void changeSalesReceipt(SalesReceipt salesReceipt) {
        if (salesReceipt == null) {
            throw new InvalidEntityDataException("El comprobante de venta es obligatorio");
        }
        if (salesReceipt.getId() == null) {
            throw new InvalidEntityDataException("El comprobante de venta debe estar registrado antes de realizar la asociación");
        }
        this.salesReceipt = salesReceipt;
        this.id = new SalesReceiptCustomerSnapshotId(this.id.getBusinessId(), this.id.getBranchId(), salesReceipt.getId());
    }

    private void changeCustomerName(String customerName) {
        if (customerName == null) {
            throw new InvalidEntityDataException("El nombre del cliente es obligatorio");
        }
        if (customerName.isBlank()) {
            throw new InvalidEntityDataException("El nombre del cliente no puede estar vacío");
        }
        String normalizedValue = customerName.trim();
        if (normalizedValue.length() > 180) {
            throw new InvalidEntityDataException("El nombre del cliente no puede superar 180 caracteres");
        }
        this.customerName = normalizedValue;
    }

    private void changeDocumentType(String documentType) {
        if (documentType == null || documentType.isBlank()) {
            this.documentType = null;
            return;
        }
        String normalizedValue = documentType.trim();
        if (normalizedValue.length() > 10) {
            throw new InvalidEntityDataException("El tipo de documento no puede superar 10 caracteres");
        }
        this.documentType = normalizedValue;
    }

    private void changeDocumentNumber(String documentNumber) {
        if (documentNumber == null || documentNumber.isBlank()) {
            this.documentNumber = null;
            return;
        }
        String normalizedValue = documentNumber.trim();
        if (normalizedValue.length() > 20) {
            throw new InvalidEntityDataException("El número de documento no puede superar 20 caracteres");
        }
        this.documentNumber = normalizedValue;
    }

    private void changeAddress(String address) {
        if (address == null || address.isBlank()) {
            this.address = null;
            return;
        }
        String normalizedValue = address.trim();
        if (normalizedValue.length() > 250) {
            throw new InvalidEntityDataException("La dirección no puede superar 250 caracteres");
        }
        this.address = normalizedValue;
    }

    private static void validateDocumentPair(String type, String number) {
        if ((type == null) != (number == null)) {
            throw new InvalidEntityDataException("Tipo y numero de documento deben estar ambos presentes o ambos ausentes");
        }
    }
}
