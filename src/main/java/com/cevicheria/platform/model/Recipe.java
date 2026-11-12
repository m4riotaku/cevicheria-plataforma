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
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer version;

    private BigDecimal servings;

    private BigDecimal yieldRatio = new BigDecimal("1");

    private boolean active = true;

    private String instructions;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public Recipe(Business business, Product product, Integer version, BigDecimal servings) {
        changeBusiness(business);
        changeProduct(product);
        changeVersion(version);
        changeServings(servings);
    }

    private void changeBusiness(Business value) {
        if (value == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        this.business = value;
    }

    private void changeProduct(Product value) {
        if (value == null) {
            throw new InvalidEntityDataException("El producto es obligatorio");
        }
        this.product = value;
    }

    public void changeVersion(Integer value) {
        if (value == null) {
            throw new InvalidEntityDataException("El version es obligatorio");
        }
        if (value != null && value < 0) {
            throw new InvalidEntityDataException("El version no puede ser negativo");
        }
        this.version = value;
    }

    public void changeServings(BigDecimal value) {
        if (value == null) {
            throw new InvalidEntityDataException("El servings es obligatorio");
        }
        if (value != null && value.abs().compareTo(new BigDecimal("999999999999.999999")) > 0) {
            throw new InvalidEntityDataException("El servings supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 6) {
            throw new InvalidEntityDataException("El servings no admite más de 6 decimales");
        }
        if (value != null && value.signum() <= 0) {
            throw new InvalidEntityDataException("El servings debe ser mayor que cero");
        }
        this.servings = value;
    }

    public void changeYieldRatio(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("999.999999")) > 0) {
            throw new InvalidEntityDataException("El yield ratio supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 6) {
            throw new InvalidEntityDataException("El yield ratio no admite más de 6 decimales");
        }
        if (value != null && value.signum() <= 0) {
            throw new InvalidEntityDataException("El yield ratio debe ser mayor que cero");
        }
        this.yieldRatio = value;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void changeInstructions(String value) {
        if (value == null || value.isBlank()) {
            this.instructions = null;
            return;
        }
        String normalized = value.trim();
        this.instructions = normalized;
    }
}