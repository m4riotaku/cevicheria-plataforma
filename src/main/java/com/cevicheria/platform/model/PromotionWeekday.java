package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.id.PromotionWeekdayId;
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
public class PromotionWeekday {

    @EmbeddedId
    private PromotionWeekdayId id;

    @MapsId("businessId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @MapsId("promotionId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    public PromotionWeekday(
            Business business,
            Promotion promotion,
            Integer weekday) {
        this.id = new PromotionWeekdayId(null, null, null);
        changeBusiness(business);
        changePromotion(promotion);
        changeWeekday(weekday);
    }

    private void changeBusiness(Business business) {
        if (business == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        if (business.getId() == null) {
            throw new InvalidEntityDataException("El negocio debe estar registrado antes de realizar la asociación");
        }
        this.business = business;
        this.id = new PromotionWeekdayId(business.getId(), this.id.getPromotionId(), this.id.getWeekday());
    }

    private void changePromotion(Promotion promotion) {
        if (promotion == null) {
            throw new InvalidEntityDataException("La promoción es obligatoria");
        }
        if (promotion.getId() == null) {
            throw new InvalidEntityDataException("La promoción debe estar registrada antes de realizar la asociación");
        }
        this.promotion = promotion;
        this.id = new PromotionWeekdayId(this.id.getBusinessId(), promotion.getId(), this.id.getWeekday());
    }

    private void changeWeekday(Integer weekday) {
        if (weekday == null) {
            throw new InvalidEntityDataException("El día de la semana es obligatorio");
        }
        if (weekday < 1) {
            throw new InvalidEntityDataException("El dia de semana debe ser mayor o igual a 1");
        }
        if (weekday > 7) {
            throw new InvalidEntityDataException("El dia de semana debe ser menor o igual a 7");
        }
        this.id = new PromotionWeekdayId(this.id.getBusinessId(), this.id.getPromotionId(), weekday);
    }
}
