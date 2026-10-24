package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.id.SupplierCategoryLinkId;
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
public class SupplierCategoryLink {

    @EmbeddedId
    private SupplierCategoryLinkId id;

    @MapsId("businessId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @MapsId("supplierId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @MapsId("supplierCategoryId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_category_id")
    private SupplierCategory supplierCategory;

    public SupplierCategoryLink(
            Business business,
            Supplier supplier,
            SupplierCategory supplierCategory) {
        this.id = new SupplierCategoryLinkId(null, null, null);
        changeBusiness(business);
        changeSupplier(supplier);
        changeSupplierCategory(supplierCategory);
    }

    private void changeBusiness(Business business) {
        if (business == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        if (business.getId() == null) {
            throw new InvalidEntityDataException("El negocio debe estar registrado antes de realizar la asociación");
        }
        this.business = business;
        this.id = new SupplierCategoryLinkId(business.getId(), this.id.getSupplierId(), this.id.getSupplierCategoryId());
    }

    private void changeSupplier(Supplier supplier) {
        if (supplier == null) {
            throw new InvalidEntityDataException("El proveedor es obligatorio");
        }
        if (supplier.getId() == null) {
            throw new InvalidEntityDataException("El proveedor debe estar registrado antes de realizar la asociación");
        }
        this.supplier = supplier;
        this.id = new SupplierCategoryLinkId(this.id.getBusinessId(), supplier.getId(), this.id.getSupplierCategoryId());
    }

    private void changeSupplierCategory(SupplierCategory supplierCategory) {
        if (supplierCategory == null) {
            throw new InvalidEntityDataException("La categoría del proveedor es obligatoria");
        }
        if (supplierCategory.getId() == null) {
            throw new InvalidEntityDataException("La categoría del proveedor debe estar registrada antes de realizar la asociación");
        }
        this.supplierCategory = supplierCategory;
        this.id = new SupplierCategoryLinkId(this.id.getBusinessId(), this.id.getSupplierId(), supplierCategory.getId());
    }
}
