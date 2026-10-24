package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.id.SalesReceiptIssuerSnapshotId;
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
public class SalesReceiptIssuerSnapshot {

    @EmbeddedId
    private SalesReceiptIssuerSnapshotId id;

    private String legalName;

    private String ruc;

    private String address;

    private String ubigeoCode;

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

    public SalesReceiptIssuerSnapshot(
            Business business,
            Branch branch,
            SalesReceipt salesReceipt,
            String legalName,
            String ruc,
            String address,
            String ubigeoCode) {
        this.id = new SalesReceiptIssuerSnapshotId(null, null, null);
        changeBusiness(business);
        changeBranch(branch);
        changeSalesReceipt(salesReceipt);
        changeLegalName(legalName);
        changeRuc(ruc);
        changeAddress(address);
        changeUbigeoCode(ubigeoCode);
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
        this.id = new SalesReceiptIssuerSnapshotId(business.getId(), this.id.getBranchId(), this.id.getSalesReceiptId());
    }

    private void changeBranch(Branch branch) {
        if (branch == null) {
            throw new InvalidEntityDataException("El local es obligatorio");
        }
        if (branch.getId() == null) {
            throw new InvalidEntityDataException("El local debe estar registrado antes de realizar la asociación");
        }
        this.branch = branch;
        this.id = new SalesReceiptIssuerSnapshotId(this.id.getBusinessId(), branch.getId(), this.id.getSalesReceiptId());
    }

    private void changeSalesReceipt(SalesReceipt salesReceipt) {
        if (salesReceipt == null) {
            throw new InvalidEntityDataException("El comprobante de venta es obligatorio");
        }
        if (salesReceipt.getId() == null) {
            throw new InvalidEntityDataException("El comprobante de venta debe estar registrado antes de realizar la asociación");
        }
        this.salesReceipt = salesReceipt;
        this.id = new SalesReceiptIssuerSnapshotId(this.id.getBusinessId(), this.id.getBranchId(), salesReceipt.getId());
    }

    private void changeLegalName(String legalName) {
        if (legalName == null) {
            throw new InvalidEntityDataException("La razón social es obligatoria");
        }
        if (legalName.isBlank()) {
            throw new InvalidEntityDataException("La razón social no puede estar vacío");
        }
        String normalizedValue = legalName.trim();
        if (normalizedValue.length() > 200) {
            throw new InvalidEntityDataException("La razón social no puede superar 200 caracteres");
        }
        this.legalName = normalizedValue;
    }

    private void changeRuc(String ruc) {
        if (ruc == null || ruc.isBlank()) {
            this.ruc = null;
            return;
        }
        String normalizedValue = ruc.trim();
        if (normalizedValue.length() > 11) {
            throw new InvalidEntityDataException("El RUC no puede superar 11 caracteres");
        }
        if (!normalizedValue.matches("[0-9]{11}")) {
            throw new InvalidEntityDataException("El RUC debe contener 11 dígitos");
        }
        this.ruc = normalizedValue;
    }

    private void changeAddress(String address) {
        if (address == null) {
            throw new InvalidEntityDataException("La dirección es obligatoria");
        }
        if (address.isBlank()) {
            throw new InvalidEntityDataException("La dirección no puede estar vacío");
        }
        String normalizedValue = address.trim();
        if (normalizedValue.length() > 250) {
            throw new InvalidEntityDataException("La dirección no puede superar 250 caracteres");
        }
        this.address = normalizedValue;
    }

    private void changeUbigeoCode(String ubigeoCode) {
        if (ubigeoCode == null || ubigeoCode.isBlank()) {
            this.ubigeoCode = null;
            return;
        }
        String normalizedValue = ubigeoCode.trim();
        if (normalizedValue.length() > 6) {
            throw new InvalidEntityDataException("El ubigeo no puede superar 6 caracteres");
        }
        if (!normalizedValue.matches("[0-9]{6}")) {
            throw new InvalidEntityDataException("El ubigeo debe contener 6 dígitos");
        }
        this.ubigeoCode = normalizedValue;
    }
}
