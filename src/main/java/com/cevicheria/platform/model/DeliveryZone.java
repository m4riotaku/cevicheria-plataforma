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
public class DeliveryZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    private String name;

    private boolean active = true;

    private BigDecimal fee = new BigDecimal("0.00");

    private BigDecimal minimumOrderAmount = new BigDecimal("0.00");

    private Integer estimatedMinutes;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public DeliveryZone(Business business, Branch branch, String name, Integer estimatedMinutes) {
        changeBusiness(business);
        changeBranch(branch);
        changeName(name);
        changeEstimatedMinutes(estimatedMinutes);
    }

    private void changeBusiness(Business value) {
        if (value == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        this.business = value;
    }

    private void changeBranch(Branch value) {
        if (value == null) {
            throw new InvalidEntityDataException("El local es obligatorio");
        }
        this.branch = value;
    }

    public void changeName(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El nombre de la zona de reparto es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El nombre del zona de reparto no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 120) {
            throw new InvalidEntityDataException("El nombre del zona de reparto no puede superar 120 caracteres");
        }
        this.name = normalized;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void changeFee(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El tarifa supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El tarifa no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El tarifa no puede ser negativo");
        }
        this.fee = value;
    }

    public void changeMinimumOrderAmount(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El minimum pedido importe supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El minimum pedido importe no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El minimum pedido importe no puede ser negativo");
        }
        this.minimumOrderAmount = value;
    }

    public void changeEstimatedMinutes(Integer value) {
        if (value == null) {
            throw new InvalidEntityDataException("El estimated minutes es obligatorio");
        }
        if (value != null && value < 0) {
            throw new InvalidEntityDataException("El estimated minutes no puede ser negativo");
        }
        if (value != null && value > 65535) {
            throw new InvalidEntityDataException("El estimated minutes supera el máximo permitido");
        }
        this.estimatedMinutes = value;
    }
}