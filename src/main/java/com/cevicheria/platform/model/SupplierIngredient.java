package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import jakarta.persistence.Entity;
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
public class SupplierIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "presentation_id")
    private IngredientPresentation presentation;

    private String brand;

    private BigDecimal referencePrice = new BigDecimal("0.00");

    private boolean preferred = false;

    private boolean active = true;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public SupplierIngredient(Business business, Supplier supplier, Ingredient ingredient) {
        changeBusiness(business);
        changeSupplier(supplier);
        changeIngredient(ingredient);
    }

    private void changeBusiness(Business value) {
        if (value == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        this.business = value;
    }

    private void changeSupplier(Supplier value) {
        if (value == null) {
            throw new InvalidEntityDataException("El proveedor es obligatorio");
        }
        this.supplier = value;
    }

    private void changeIngredient(Ingredient value) {
        if (value == null) {
            throw new InvalidEntityDataException("El insumo es obligatorio");
        }
        this.ingredient = value;
    }

    public void changePresentation(IngredientPresentation value) {
        this.presentation = value;
    }

    public void changeBrand(String value) {
        if (value == null || value.isBlank()) {
            this.brand = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 80) {
            throw new InvalidEntityDataException("El brand no puede superar 80 caracteres");
        }
        this.brand = normalized;
    }

    public void changeReferencePrice(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El precio de referencia supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El precio de referencia no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El precio de referencia no puede ser negativo");
        }
        this.referencePrice = value;
    }

    public void enablePreferred() {
        this.preferred = true;
    }

    public void disablePreferred() {
        this.preferred = false;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }
}