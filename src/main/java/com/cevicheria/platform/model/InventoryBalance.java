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
public class InventoryBalance {

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

    private BigDecimal quantity = new BigDecimal("0");

    private BigDecimal averageCost = new BigDecimal("0");

    private BigDecimal minimumStock = new BigDecimal("0");

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public InventoryBalance(Business business, Branch branch, Warehouse warehouse, Ingredient ingredient) {
        changeBusiness(business);
        changeBranch(branch);
        changeWarehouse(warehouse);
        changeIngredient(ingredient);
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

    public void changeQuantity(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("999999999999.999999")) > 0) {
            throw new InvalidEntityDataException("La cantidad supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 6) {
            throw new InvalidEntityDataException("La cantidad no admite más de 6 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("La cantidad no puede ser negativo");
        }
        this.quantity = value;
    }

    public void changeAverageCost(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("99999999999999.999999")) > 0) {
            throw new InvalidEntityDataException("El average cost supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 6) {
            throw new InvalidEntityDataException("El average cost no admite más de 6 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El average cost no puede ser negativo");
        }
        this.averageCost = value;
    }

    public void changeMinimumStock(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("999999999999.999999")) > 0) {
            throw new InvalidEntityDataException("El minimum stock supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 6) {
            throw new InvalidEntityDataException("El minimum stock no admite más de 6 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El minimum stock no puede ser negativo");
        }
        this.minimumStock = value;
    }
}