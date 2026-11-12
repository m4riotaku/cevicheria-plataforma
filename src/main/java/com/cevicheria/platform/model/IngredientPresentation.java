package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.enums.IngredientPresentationType;
import jakarta.persistence.Entity;
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
public class IngredientPresentation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    private String name;

    @Enumerated(EnumType.STRING)
    private IngredientPresentationType type;

    private BigDecimal baseConversionFactor;

    private BigDecimal baseTare = new BigDecimal("0");

    private boolean active = true;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public IngredientPresentation(Business business, Ingredient ingredient, String name, IngredientPresentationType type, BigDecimal baseConversionFactor) {
        changeBusiness(business);
        changeIngredient(ingredient);
        changeName(name);
        changeType(type);
        changeBaseConversionFactor(baseConversionFactor);
    }

    private void changeBusiness(Business value) {
        if (value == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        this.business = value;
    }

    private void changeIngredient(Ingredient value) {
        if (value == null) {
            throw new InvalidEntityDataException("El insumo es obligatorio");
        }
        this.ingredient = value;
    }

    public void changeName(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El nombre es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El nombre no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 100) {
            throw new InvalidEntityDataException("El nombre no puede superar 100 caracteres");
        }
        this.name = normalized;
    }

    public void changeType(IngredientPresentationType value) {
        if (value == null) {
            throw new InvalidEntityDataException("El tipo es obligatorio");
        }
        this.type = value;
    }

    public void changeBaseConversionFactor(BigDecimal value) {
        if (value == null) {
            throw new InvalidEntityDataException("El base conversion factor es obligatorio");
        }
        if (value.abs().compareTo(new BigDecimal("999999999999.999999")) > 0) {
            throw new InvalidEntityDataException("El base conversion factor supera el máximo permitido");
        }
        if (value.stripTrailingZeros().scale() > 6) {
            throw new InvalidEntityDataException("El base conversion factor no admite más de 6 decimales");
        }
        if (value.signum() <= 0) {
            throw new InvalidEntityDataException("El base conversion factor debe ser mayor que cero");
        }
        this.baseConversionFactor = value;
    }

    public void changeBaseTare(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("999999999999.999999")) > 0) {
            throw new InvalidEntityDataException("El base tare supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 6) {
            throw new InvalidEntityDataException("El base tare no admite más de 6 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El base tare no puede ser negativo");
        }
        this.baseTare = value;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }
}