package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.id.BranchConfigurationId;
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
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BranchConfiguration {

    @EmbeddedId
    private BranchConfigurationId id;

    private boolean saleByWeightEnabled = false;

    private boolean stockAlertEnabled = false;

    private Integer expirationAlertDays;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    @MapsId("businessId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @MapsId("branchId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    public BranchConfiguration(Business business, Branch branch, Integer expirationAlertDays)
    {
        this.id = new BranchConfigurationId(null, null);
        changeBusiness(business);
        changeBranch(branch);
        changeExpirationAlertDays(expirationAlertDays);
        if (business.getId() != null && branch.getBusiness() != null && branch.getBusiness().getId() != null && !business.getId().equals(branch.getBusiness().getId()))
        {
            throw new InvalidEntityDataException("El local no pertenece al negocio seleccionado");
        }
    }

    private void changeBusiness(Business business)
    {
        if (business == null)
        {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        if (business.getId() == null)
        {
            throw new InvalidEntityDataException("El negocio debe estar registrado antes de realizar la asociación");
        }
        this.business = business;
        this.id = new BranchConfigurationId(business.getId(), this.id.getBranchId());
    }

    private void changeBranch(Branch branch) {
        if (branch == null) {
            throw new InvalidEntityDataException("El local es obligatorio");
        }
        if (branch.getId() == null) {
            throw new InvalidEntityDataException("El local debe estar registrado antes de realizar la asociación");
        }
        this.branch = branch;
        this.id = new BranchConfigurationId(this.id.getBusinessId(), branch.getId());
    }

    public void changeExpirationAlertDays(Integer expirationAlertDays) {
        if (expirationAlertDays == null) {
            this.expirationAlertDays = null;
            return;
        }
        if (expirationAlertDays < 0) {
            throw new InvalidEntityDataException("Los días de anticipación de alerta debe ser cero o positivo");
        }
        if (expirationAlertDays > 65535) {
            throw new InvalidEntityDataException("Los días de anticipación de alerta supera el máximo permitido");
        }
        this.expirationAlertDays = expirationAlertDays;
    }

    public void enableSaleByWeight() {
        this.saleByWeightEnabled = true;
    }

    public void disableSaleByWeight() {
        this.saleByWeightEnabled = false;
    }

    public void enableStockAlert() {
        this.stockAlertEnabled = true;
    }

    public void disableStockAlert() {
        this.stockAlertEnabled = false;
    }
}
