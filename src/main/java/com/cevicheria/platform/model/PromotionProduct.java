package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.id.PromotionProductId;
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
public class PromotionProduct {

    @EmbeddedId
    private PromotionProductId id;

    @MapsId("businessId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @MapsId("promotionId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    public PromotionProduct(
            Business business,
            Promotion promotion,
            Product product) {
        this.id = new PromotionProductId(null, null, null);
        changeBusiness(business);
        changePromotion(promotion);
        changeProduct(product);
    }

    private void changeBusiness(Business business) {
        if (business == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        if (business.getId() == null) {
            throw new InvalidEntityDataException("El negocio debe estar registrado antes de realizar la asociación");
        }
        this.business = business;
        this.id = new PromotionProductId(business.getId(), this.id.getPromotionId(), this.id.getProductId());
    }

    private void changePromotion(Promotion promotion) {
        if (promotion == null) {
            throw new InvalidEntityDataException("La promoción es obligatoria");
        }
        if (promotion.getId() == null) {
            throw new InvalidEntityDataException("La promoción debe estar registrada antes de realizar la asociación");
        }
        this.promotion = promotion;
        this.id = new PromotionProductId(this.id.getBusinessId(), promotion.getId(), this.id.getProductId());
    }

    private void changeProduct(Product product) {
        if (product == null) {
            throw new InvalidEntityDataException("El producto es obligatorio");
        }
        if (product.getId() == null) {
            throw new InvalidEntityDataException("El producto debe estar registrado antes de realizar la asociación");
        }
        this.product = product;
        this.id = new PromotionProductId(this.id.getBusinessId(), this.id.getPromotionId(), product.getId());
    }
}
