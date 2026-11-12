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
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    private String name;

    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "category_id")
    private Category category;

    private String code;

    private String baseUnit;

    private boolean perishable = true;

    private BigDecimal minTemperature;

    private BigDecimal maxTemperature;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public Ingredient(Business business, String name, String code, String baseUnit) {
        changeBusiness(business);
        changeName(name);
        changeCode(code);
        changeBaseUnit(baseUnit);
    }

    private void changeBusiness(Business value) {
        if (value == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        this.business = value;
    }

    public void changeName(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El nombre del insumo es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El nombre del insumo no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 120) {
            throw new InvalidEntityDataException("El nombre del insumo no puede superar 120 caracteres");
        }
        this.name = normalized;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void changeCategory(Category value) {
        this.category = value;
    }

    public void changeCode(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El código es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El código no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 40) {
            throw new InvalidEntityDataException("El código no puede superar 40 caracteres");
        }
        this.code = normalized;
    }

    public void changeBaseUnit(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El unidad base es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El unidad base no puede estar vacío");
        }
        String normalized = value.trim().toUpperCase(java.util.Locale.ROOT);
        if (!(normalized.equals("KG") || normalized.equals("L") || normalized.equals("UND"))) {
            throw new InvalidEntityDataException("El unidad base no coincide con los valores permitidos por la base de datos");
        }
        this.baseUnit = normalized;
    }

    public void enablePerishable() {
        this.perishable = true;
    }

    public void disablePerishable() {
        this.perishable = false;
    }

    public void changeMinTemperature(BigDecimal value) {
        if (value != null && this.maxTemperature != null && value.compareTo(this.maxTemperature) > 0) {
            throw new InvalidEntityDataException("La temperatura mínima no puede superar la temperatura máxima");
        }
        if (value != null && value.abs().compareTo(new BigDecimal("9999.99")) > 0) {
            throw new InvalidEntityDataException("El min temperature supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El min temperature no admite más de 2 decimales");
        }
        this.minTemperature = value;
    }

    public void changeMaxTemperature(BigDecimal value) {
        if (value != null && this.minTemperature != null && value.compareTo(this.minTemperature) < 0) {
            throw new InvalidEntityDataException("La temperatura máxima no puede ser inferior a la mínima");
        }
        if (value != null && value.abs().compareTo(new BigDecimal("9999.99")) > 0) {
            throw new InvalidEntityDataException("El max temperature supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El max temperature no admite más de 2 decimales");
        }
        this.maxTemperature = value;
    }
}