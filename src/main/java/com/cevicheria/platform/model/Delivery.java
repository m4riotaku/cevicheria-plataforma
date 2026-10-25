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
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id")
    private CustomerOrder order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "delivery_zone_id")
    private DeliveryZone deliveryZone;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "driver_employee_id")
    private Employee driverEmployee;

    private String driverName;

    private String driverPhone;

    private String address;

    private BigDecimal deliveryFeeSnapshot = new BigDecimal("0.00");

    private Integer estimatedMinutes;

    private BigDecimal cashOnDeliveryAmount = new BigDecimal("0.00");

    private LocalDateTime dispatchedAt;

    private LocalDateTime deliveredAt;

    private String status = "PENDIENTE";

    private String addressReference;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public Delivery(Business business, Branch branch, CustomerOrder order, DeliveryZone deliveryZone, String driverName, String address, Integer estimatedMinutes) {
        changeBusiness(business);
        changeBranch(branch);
        changeOrder(order);
        changeDeliveryZone(deliveryZone);
        changeDriverName(driverName);
        changeAddress(address);
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

    private void changeOrder(CustomerOrder value) {
        if (value == null) {
            throw new InvalidEntityDataException("El pedido es obligatorio");
        }
        this.order = value;
    }

    private void changeDeliveryZone(DeliveryZone value) {
        if (value == null) {
            throw new InvalidEntityDataException("El delivery zone es obligatorio");
        }
        this.deliveryZone = value;
    }

    public void changeDriverEmployee(Employee value) {
        this.driverEmployee = value;
    }

    public void changeDriverName(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El driver nombre es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El driver nombre no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 150) {
            throw new InvalidEntityDataException("El driver nombre no puede superar 150 caracteres");
        }
        this.driverName = normalized;
    }

    public void changeDriverPhone(String value) {
        if (value == null || value.isBlank()) {
            this.driverPhone = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 30) {
            throw new InvalidEntityDataException("El driver phone no puede superar 30 caracteres");
        }
        this.driverPhone = normalized;
    }

    public void changeAddress(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("La dirección es obligatoria");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("La dirección no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 300) {
            throw new InvalidEntityDataException("La dirección no puede superar 300 caracteres");
        }
        this.address = normalized;
    }

    public void changeDeliveryFeeSnapshot(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El delivery fee snapshot supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El delivery fee snapshot no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El delivery fee snapshot no puede ser negativo");
        }
        this.deliveryFeeSnapshot = value;
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

    public void changeCashOnDeliveryAmount(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El caja on delivery importe supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El caja on delivery importe no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El caja on delivery importe no puede ser negativo");
        }
        this.cashOnDeliveryAmount = value;
    }

    public void changeDispatchedAt(LocalDateTime value) {
        this.dispatchedAt = value;
    }

    public void changeDeliveredAt(LocalDateTime value) {
        this.deliveredAt = value;
    }

    public void changeStatus(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El estado es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El estado no puede estar vacío");
        }
        String normalized = value.trim().toUpperCase(java.util.Locale.ROOT);
        if (!(normalized.equals("PENDIENTE") || normalized.equals("ASIGNADO") || normalized.equals("EN_RUTA") || normalized.equals("ENTREGADO") || normalized.equals("FALLIDO"))) {
            throw new InvalidEntityDataException("El estado no coincide con los valores permitidos por la base de datos");
        }
        this.status = normalized;
    }

    public void changeAddressReference(String value) {
        if (value == null || value.isBlank()) {
            this.addressReference = null;
            return;
        }
        String normalized = value.trim();
        this.addressReference = normalized;
    }
}