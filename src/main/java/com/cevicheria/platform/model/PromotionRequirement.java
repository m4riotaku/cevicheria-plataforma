package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.id.PromotionRequirementId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.MapsId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PromotionRequirement {

    @EmbeddedId
    private PromotionRequirementId id;

    private BigDecimal minimumOrderAmount;

    private BigDecimal minimumQuantity;

    private Integer maximumUsesPerCustomer;

    private boolean firstPurchaseOnly = false;

    @MapsId("businessId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @MapsId("promotionId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    public PromotionRequirement(
            Business business,
            Promotion promotion,
            BigDecimal minimumOrderAmount,
            BigDecimal minimumQuantity,
            Integer maximumUsesPerCustomer) {
        this.id = new PromotionRequirementId(null, null);
        changeBusiness(business);
        changePromotion(promotion);
        changeMinimumOrderAmount(minimumOrderAmount);
        changeMinimumQuantity(minimumQuantity);
        changeMaximumUsesPerCustomer(maximumUsesPerCustomer);
    }

    private void changeBusiness(Business business) {
        if (business == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        if (business.getId() == null) {
            throw new InvalidEntityDataException("El negocio debe estar registrado antes de realizar la asociación");
        }
        this.business = business;
        this.id = new PromotionRequirementId(business.getId(), this.id.getPromotionId());
    }

    private void changePromotion(Promotion promotion) {
        if (promotion == null) {
            throw new InvalidEntityDataException("La promoción es obligatoria");
        }
        if (promotion.getId() == null) {
            throw new InvalidEntityDataException("La promoción debe estar registrada antes de realizar la asociación");
        }
        this.promotion = promotion;
        this.id = new PromotionRequirementId(this.id.getBusinessId(), promotion.getId());
    }

    public void changeMinimumOrderAmount(BigDecimal minimumOrderAmount) {
        if (minimumOrderAmount == null) {
            this.minimumOrderAmount = null;
            return;
        }
        if (minimumOrderAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidEntityDataException("El importe mínimo del pedido debe ser cero o positivo");
        }
        if (minimumOrderAmount.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El importe mínimo del pedido excede DECIMAL(18,2)");
        }
        if (minimumOrderAmount.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El importe mínimo del pedido admite hasta 2 decimales");
        }
        this.minimumOrderAmount = minimumOrderAmount.setScale(2);
    }

    public void changeMinimumQuantity(BigDecimal minimumQuantity) {
        if (minimumQuantity == null) {
            this.minimumQuantity = null;
            return;
        }
        if (minimumQuantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidEntityDataException("La cantidad mínima debe ser mayor que cero");
        }
        if (minimumQuantity.abs().compareTo(new BigDecimal("999999999999.999999")) > 0) {
            throw new InvalidEntityDataException("La cantidad mínima excede DECIMAL(18,6)");
        }
        if (minimumQuantity.stripTrailingZeros().scale() > 6) {
            throw new InvalidEntityDataException("La cantidad mínima admite hasta 6 decimales");
        }
        this.minimumQuantity = minimumQuantity.setScale(6);
    }

    public void changeMaximumUsesPerCustomer(Integer maximumUsesPerCustomer) {
        if (maximumUsesPerCustomer == null) {
            this.maximumUsesPerCustomer = null;
            return;
        }
        if (maximumUsesPerCustomer < 1) {
            throw new InvalidEntityDataException("La cantidad máxima de usos por cliente debe ser mayor que cero");
        }
        this.maximumUsesPerCustomer = maximumUsesPerCustomer;
    }

    public void requireFirstPurchase() {
        this.firstPurchaseOnly = true;
    }

    public void allowRepeatPurchases() {
        this.firstPurchaseOnly = false;
    }
}
