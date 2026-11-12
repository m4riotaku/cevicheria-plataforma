package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.id.SupplierBrandLinkId;
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
public class SupplierBrandLink {

    @EmbeddedId
    private SupplierBrandLinkId id;

    @MapsId("businessId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @MapsId("supplierId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @MapsId("supplierBrandId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_brand_id")
    private SupplierBrand supplierBrand;

    public SupplierBrandLink(
            Business business,
            Supplier supplier,
            SupplierBrand supplierBrand) {
        this.id = new SupplierBrandLinkId(null, null, null);
        changeBusiness(business);
        changeSupplier(supplier);
        changeSupplierBrand(supplierBrand);
    }

    private void changeBusiness(Business business) {
        if (business == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        if (business.getId() == null) {
            throw new InvalidEntityDataException("El negocio debe estar registrado antes de realizar la asociación");
        }
        this.business = business;
        this.id = new SupplierBrandLinkId(business.getId(), this.id.getSupplierId(), this.id.getSupplierBrandId());
    }

    private void changeSupplier(Supplier supplier) {
        if (supplier == null) {
            throw new InvalidEntityDataException("El proveedor es obligatorio");
        }
        if (supplier.getId() == null) {
            throw new InvalidEntityDataException("El proveedor debe estar registrado antes de realizar la asociación");
        }
        this.supplier = supplier;
        this.id = new SupplierBrandLinkId(this.id.getBusinessId(), supplier.getId(), this.id.getSupplierBrandId());
    }

    private void changeSupplierBrand(SupplierBrand supplierBrand) {
        if (supplierBrand == null) {
            throw new InvalidEntityDataException("La marca del proveedor es obligatoria");
        }
        if (supplierBrand.getId() == null) {
            throw new InvalidEntityDataException("La marca del proveedor debe estar registrada antes de realizar la asociación");
        }
        this.supplierBrand = supplierBrand;
        this.id = new SupplierBrandLinkId(this.id.getBusinessId(), this.id.getSupplierId(), supplierBrand.getId());
    }
}
