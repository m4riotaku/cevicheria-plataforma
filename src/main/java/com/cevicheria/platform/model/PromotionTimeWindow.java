package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.id.PromotionTimeWindowId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.MapsId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PromotionTimeWindow {

    @EmbeddedId
    private PromotionTimeWindowId id;

    private LocalTime startsAt;

    private LocalTime endsAt;

    @MapsId("businessId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @MapsId("promotionId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    public PromotionTimeWindow(
            Business business,
            Promotion promotion,
            Integer sequenceNumber,
            LocalTime startsAt,
            LocalTime endsAt) {
        this.id = new PromotionTimeWindowId(null, null, null);
        changeBusiness(business);
        changePromotion(promotion);
        changeSequenceNumber(sequenceNumber);
        changeStartsAt(startsAt);
        changeEndsAt(endsAt);
        validateTimeWindow(this.startsAt, this.endsAt);
    }

    private void changeBusiness(Business business) {
        if (business == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        if (business.getId() == null) {
            throw new InvalidEntityDataException("El negocio debe estar registrado antes de realizar la asociación");
        }
        this.business = business;
        this.id = new PromotionTimeWindowId(business.getId(), this.id.getPromotionId(), this.id.getSequenceNumber());
    }

    private void changePromotion(Promotion promotion) {
        if (promotion == null) {
            throw new InvalidEntityDataException("La promoción es obligatoria");
        }
        if (promotion.getId() == null) {
            throw new InvalidEntityDataException("La promoción debe estar registrada antes de realizar la asociación");
        }
        this.promotion = promotion;
        this.id = new PromotionTimeWindowId(this.id.getBusinessId(), promotion.getId(), this.id.getSequenceNumber());
    }

    private void changeSequenceNumber(Integer sequenceNumber) {
        if (sequenceNumber == null) {
            throw new InvalidEntityDataException("El número de secuencia es obligatorio");
        }
        if (sequenceNumber < 1) {
            throw new InvalidEntityDataException("El número de secuencia debe ser mayor que cero");
        }
        if (sequenceNumber > 65535) {
            throw new InvalidEntityDataException("El número de secuencia supera el máximo permitido");
        }
        this.id = new PromotionTimeWindowId(this.id.getBusinessId(), this.id.getPromotionId(), sequenceNumber);
    }

    public void changeStartsAt(LocalTime startsAt) {
        if (startsAt == null) {
            throw new InvalidEntityDataException("La hora inicial es obligatoria");
        }
        if (this.endsAt != null) {
            validateTimeWindow(startsAt, this.endsAt);
        }
        this.startsAt = startsAt;
    }

    public void changeEndsAt(LocalTime endsAt) {
        if (endsAt == null) {
            throw new InvalidEntityDataException("La hora final es obligatoria");
        }
        if (this.startsAt != null) {
            validateTimeWindow(this.startsAt, endsAt);
        }
        this.endsAt = endsAt;
    }

    private static void validateTimeWindow(LocalTime start, LocalTime end) {
        if (start != null && end != null && !start.isBefore(end)) {
            throw new InvalidEntityDataException("El horario de inicio debe ser anterior al horario de fin");
        }
    }
}
