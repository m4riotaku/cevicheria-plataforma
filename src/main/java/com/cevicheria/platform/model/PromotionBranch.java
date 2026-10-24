package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.id.PromotionBranchId;
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
public class PromotionBranch {

    @EmbeddedId
    private PromotionBranchId id;

    @MapsId("businessId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @MapsId("promotionId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @MapsId("branchId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    public PromotionBranch(
            Business business,
            Promotion promotion,
            Branch branch) {
        this.id = new PromotionBranchId(null, null, null);
        changeBusiness(business);
        changePromotion(promotion);
        changeBranch(branch);
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
        this.id = new PromotionBranchId(business.getId(), this.id.getPromotionId(), this.id.getBranchId());
    }

    private void changePromotion(Promotion promotion) {
        if (promotion == null) {
            throw new InvalidEntityDataException("La promoción es obligatoria");
        }
        if (promotion.getId() == null) {
            throw new InvalidEntityDataException("La promoción debe estar registrada antes de realizar la asociación");
        }
        this.promotion = promotion;
        this.id = new PromotionBranchId(this.id.getBusinessId(), promotion.getId(), this.id.getBranchId());
    }

    private void changeBranch(Branch branch) {
        if (branch == null) {
            throw new InvalidEntityDataException("El local es obligatorio");
        }
        if (branch.getId() == null) {
            throw new InvalidEntityDataException("El local debe estar registrado antes de realizar la asociación");
        }
        this.branch = branch;
        this.id = new PromotionBranchId(this.id.getBusinessId(), this.id.getPromotionId(), branch.getId());
    }
}
