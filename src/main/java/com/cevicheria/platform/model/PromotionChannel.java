package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.enums.SalesChannel;
import com.cevicheria.platform.model.id.PromotionChannelId;
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
public class PromotionChannel {

    @EmbeddedId
    private PromotionChannelId id;

    @MapsId("businessId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @MapsId("promotionId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    public PromotionChannel(
            Business business,
            Promotion promotion,
            SalesChannel salesChannel) {
        this.id = new PromotionChannelId(null, null, null);
        changeBusiness(business);
        changePromotion(promotion);
        changeSalesChannel(salesChannel);
    }

    private void changeBusiness(Business business) {
        if (business == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        if (business.getId() == null) {
            throw new InvalidEntityDataException("El negocio debe estar registrado antes de realizar la asociación");
        }
        this.business = business;
        this.id = new PromotionChannelId(business.getId(), this.id.getPromotionId(), this.id.getSalesChannel());
    }

    private void changePromotion(Promotion promotion) {
        if (promotion == null) {
            throw new InvalidEntityDataException("La promoción es obligatoria");
        }
        if (promotion.getId() == null) {
            throw new InvalidEntityDataException("La promoción debe estar registrada antes de realizar la asociación");
        }
        this.promotion = promotion;
        this.id = new PromotionChannelId(this.id.getBusinessId(), promotion.getId(), this.id.getSalesChannel());
    }

    private void changeSalesChannel(SalesChannel salesChannel) {
        if (salesChannel == null) {
            throw new InvalidEntityDataException("El canal de venta es obligatorio");
        }
        this.id = new PromotionChannelId(this.id.getBusinessId(), this.id.getPromotionId(), salesChannel);
    }
}
