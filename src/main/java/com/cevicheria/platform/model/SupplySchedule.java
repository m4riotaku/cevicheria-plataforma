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
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SupplySchedule {

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
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "alternate_supplier_id")
    private Supplier alternateSupplier;

    private BigDecimal baseQuantity;

    private Integer frequencyDays;

    private LocalDate nextDate;

    private boolean active = true;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public SupplySchedule(Business business, Branch branch, Warehouse warehouse, Ingredient ingredient, Supplier supplier, BigDecimal baseQuantity, Integer frequencyDays, LocalDate nextDate) {
        changeBusiness(business);
        changeBranch(branch);
        changeWarehouse(warehouse);
        changeIngredient(ingredient);
        changeSupplier(supplier);
        changeBaseQuantity(baseQuantity);
        changeFrequencyDays(frequencyDays);
        changeNextDate(nextDate);
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

    private void changeWarehouse(Warehouse value) {
        if (value == null) {
            throw new InvalidEntityDataException("El almacén es obligatorio");
        }
        this.warehouse = value;
    }

    private void changeIngredient(Ingredient value) {
        if (value == null) {
            throw new InvalidEntityDataException("El insumo es obligatorio");
        }
        this.ingredient = value;
    }

    private void changeSupplier(Supplier value) {
        if (value == null) {
            throw new InvalidEntityDataException("El proveedor es obligatorio");
        }
        this.supplier = value;
    }

    public void changeAlternateSupplier(Supplier value) {
        this.alternateSupplier = value;
    }

    public void changeBaseQuantity(BigDecimal value) {
        if (value == null) {
            throw new InvalidEntityDataException("La base cantidad es obligatoria");
        }
        if (value != null && value.abs().compareTo(new BigDecimal("999999999999.999999")) > 0) {
            throw new InvalidEntityDataException("La base cantidad supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 6) {
            throw new InvalidEntityDataException("La base cantidad no admite más de 6 decimales");
        }
        if (value != null && value.signum() <= 0) {
            throw new InvalidEntityDataException("La base cantidad debe ser mayor que cero");
        }
        this.baseQuantity = value;
    }

    public void changeFrequencyDays(Integer value) {
        if (value == null) {
            throw new InvalidEntityDataException("El frequency days es obligatorio");
        }
        if (value != null && value < 0) {
            throw new InvalidEntityDataException("El frequency days no puede ser negativo");
        }
        if (value != null && value > 65535) {
            throw new InvalidEntityDataException("El frequency days supera el máximo permitido");
        }
        if (value != null && value <= 0) {
            throw new InvalidEntityDataException("El frequency days debe ser mayor que cero");
        }
        this.frequencyDays = value;
    }

    public void changeNextDate(LocalDate value) {
        if (value == null) {
            throw new InvalidEntityDataException("La next fecha es obligatoria");
        }
        this.nextDate = value;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }
}