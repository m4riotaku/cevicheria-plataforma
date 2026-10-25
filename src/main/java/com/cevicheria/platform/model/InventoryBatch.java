package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.enums.InventoryBatchStatus;
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
public class InventoryBatch {

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

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "purchase_item_id")
    private PurchaseItem purchaseItem;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "source_batch_id")
    private InventoryBatch sourceBatch;

    private String code;

    private LocalDateTime receivedAt;

    private LocalDate dueOn;

    private BigDecimal onHandQuantity = new BigDecimal("0");

    @Enumerated(EnumType.STRING)
    private InventoryBatchStatus status = InventoryBatchStatus.APTO;

    private String blockReason;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public InventoryBatch(Business business, Branch branch, Warehouse warehouse, Ingredient ingredient, String code, LocalDateTime receivedAt) {
        changeBusiness(business);
        changeBranch(branch);
        changeWarehouse(warehouse);
        changeIngredient(ingredient);
        changeCode(code);
        changeReceivedAt(receivedAt);
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

    private void changePurchaseItem(PurchaseItem value) {
        this.purchaseItem = value;
    }

    private void changeSourceBatch(InventoryBatch value) {
        this.sourceBatch = value;
    }

    private void changeCode(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El código es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El código no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 80) {
            throw new InvalidEntityDataException("El código no puede superar 80 caracteres");
        }
        this.code = normalized;
    }

    public void changeReceivedAt(LocalDateTime value) {
        if (value == null) {
            throw new InvalidEntityDataException("El received at es obligatorio");
        }
        this.receivedAt = value;
    }

    public void changeDueOn(LocalDate value) {
        this.dueOn = value;
    }

    public void changeOnHandQuantity(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("999999999999.999999")) > 0) {
            throw new InvalidEntityDataException("La on hand cantidad supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 6) {
            throw new InvalidEntityDataException("La on hand cantidad no admite más de 6 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("La on hand cantidad no puede ser negativo");
        }
        this.onHandQuantity = value;
    }

    public void changeStatus(InventoryBatchStatus value) {
        this.status = value;
    }

    public void changeBlockReason(String value) {
        if (value == null || value.isBlank()) {
            this.blockReason = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 250) {
            throw new InvalidEntityDataException("El block reason no puede superar 250 caracteres");
        }
        this.blockReason = normalized;
    }
}