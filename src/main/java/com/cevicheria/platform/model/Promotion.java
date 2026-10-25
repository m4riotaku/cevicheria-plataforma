package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.enums.PromotionType;
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    private String name;

    private boolean active = true;

    @Enumerated(EnumType.STRING)
    private PromotionType type;

    private BigDecimal valueAmount = new BigDecimal("0.00");

    private Integer priority = 0;

    private boolean stackable = false;

    private LocalDateTime validFrom;

    private LocalDateTime validUntil;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public Promotion(Business business, String name, PromotionType type, LocalDateTime validFrom) {
        changeBusiness(business);
        changeName(name);
        changeType(type);
        changeValidFrom(validFrom);
    }

    private void changeBusiness(Business value) {
        if (value == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        this.business = value;
    }

    public void changeName(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El nombre de la promoción es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El nombre del promoción no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 120) {
            throw new InvalidEntityDataException("El nombre del promoción no puede superar 120 caracteres");
        }
        this.name = normalized;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void changeType(PromotionType value) {
        if (value == null) {
            throw new InvalidEntityDataException("El tipo es obligatorio");
        }
        this.type = value;
    }

    public void changeValueAmount(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El value importe supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El value importe no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El value importe no puede ser negativo");
        }
        this.valueAmount = value;
    }

    public void changePriority(Integer value) {
        this.priority = value;
    }

    public void enableStackable() {
        this.stackable = true;
    }

    public void disableStackable() {
        this.stackable = false;
    }

    private void changeValidFrom(LocalDateTime value) {
        if (value == null) {
            throw new InvalidEntityDataException("El inicio de vigencia es obligatoria");
        }
        this.validFrom = value;
    }

    public void changeValidUntil(LocalDateTime value) {
        this.validUntil = value;
    }


    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "promotion", fetch = FetchType.LAZY)
    private List<PromotionBranch> promotionBranches = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "promotion", fetch = FetchType.LAZY)
    private List<PromotionChannel> promotionChannels = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "promotion", fetch = FetchType.LAZY)
    private List<PromotionProduct> promotionProducts = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "promotion", fetch = FetchType.LAZY)
    private List<PromotionWeekday> promotionWeekdays = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "promotion", fetch = FetchType.LAZY)
    private List<PromotionTimeWindow> promotionTimeWindows = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "promotion", fetch = FetchType.LAZY)
    private List<PromotionRequirement> promotionRequirements = new ArrayList<>();

    public List<PromotionBranch> getPromotionBranches() {
        return List.copyOf(this.promotionBranches);
    }

    public List<PromotionChannel> getPromotionChannels() {
        return List.copyOf(this.promotionChannels);
    }

    public List<PromotionProduct> getPromotionProducts() {
        return List.copyOf(this.promotionProducts);
    }

    public List<PromotionWeekday> getPromotionWeekdays() {
        return List.copyOf(this.promotionWeekdays);
    }

    public List<PromotionTimeWindow> getPromotionTimeWindows() {
        return List.copyOf(this.promotionTimeWindows);
    }

    public List<PromotionRequirement> getPromotionRequirements() {
        return List.copyOf(this.promotionRequirements);
    }
}