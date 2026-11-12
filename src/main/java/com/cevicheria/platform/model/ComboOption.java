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
public class ComboOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "combo_product_id")
    private Product comboProduct;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "option_product_id")
    private Product optionProduct;

    private Integer positionNumber;

    private String positionName;

    private BigDecimal fraction = new BigDecimal("1");

    private BigDecimal surcharge = new BigDecimal("0.00");

    private boolean active = true;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public ComboOption(Business business, Product comboProduct, Product optionProduct, Integer positionNumber, String positionName) {
        changeBusiness(business);
        changeComboProduct(comboProduct);
        changeOptionProduct(optionProduct);
        changePositionNumber(positionNumber);
        changePositionName(positionName);
    }

    private void changeBusiness(Business value) {
        if (value == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        this.business = value;
    }

    private void changeComboProduct(Product value) {
        if (value == null) {
            throw new InvalidEntityDataException("El combo producto es obligatorio");
        }
        this.comboProduct = value;
    }

    private void changeOptionProduct(Product value) {
        if (value == null) {
            throw new InvalidEntityDataException("El option producto es obligatorio");
        }
        this.optionProduct = value;
    }

    public void changePositionNumber(Integer value) {
        if (value == null) {
            throw new InvalidEntityDataException("El position número es obligatorio");
        }
        if (value != null && value < 0) {
            throw new InvalidEntityDataException("El position número no puede ser negativo");
        }
        if (value != null && value > 65535) {
            throw new InvalidEntityDataException("El position número supera el máximo permitido");
        }
        this.positionNumber = value;
    }

    public void changePositionName(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El position nombre es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El position nombre no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 80) {
            throw new InvalidEntityDataException("El position nombre no puede superar 80 caracteres");
        }
        this.positionName = normalized;
    }

    public void changeFraction(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("999999999999.999999")) > 0) {
            throw new InvalidEntityDataException("El fraction supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 6) {
            throw new InvalidEntityDataException("El fraction no admite más de 6 decimales");
        }
        if (value != null && value.signum() <= 0) {
            throw new InvalidEntityDataException("El fraction debe ser mayor que cero");
        }
        this.fraction = value;
    }

    public void changeSurcharge(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El surcharge supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El surcharge no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El surcharge no puede ser negativo");
        }
        this.surcharge = value;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }
}