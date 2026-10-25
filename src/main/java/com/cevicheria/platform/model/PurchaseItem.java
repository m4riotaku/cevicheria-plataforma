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
public class PurchaseItem {

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
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "purchase_order_item_id")
    private PurchaseOrderItem purchaseOrderItem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "presentation_id")
    private IngredientPresentation presentation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    private LocalDateTime receivedAt;

    private BigDecimal presentationQuantity;

    private BigDecimal acceptedBaseQuantity;

    private BigDecimal rejectedBaseQuantity = new BigDecimal("0");

    private BigDecimal grossWeight;

    private BigDecimal tareWeight;

    @Generated(event = {EventType.INSERT, EventType.UPDATE})
    private BigDecimal netWeight;

    private BigDecimal temperature;

    private BigDecimal acceptedTotalCost = new BigDecimal("0.00");

    private BigDecimal subtotal = new BigDecimal("0.00");

    private BigDecimal tax = new BigDecimal("0.00");

    private BigDecimal total = new BigDecimal("0.00");

    private String rejectionReason;

    private String evidenceUrl;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public PurchaseItem(Business business, Branch branch, Purchase purchase, Ingredient ingredient, Warehouse warehouse, BigDecimal presentationQuantity, BigDecimal acceptedBaseQuantity) {
        changeBusiness(business);
        changeBranch(branch);
        changePurchase(purchase);
        changeIngredient(ingredient);
        changeWarehouse(warehouse);
        changePresentationQuantity(presentationQuantity);
        changeAcceptedBaseQuantity(acceptedBaseQuantity);
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

    private void changePurchase(Purchase value) {
        if (value == null) {
            throw new InvalidEntityDataException("El purchase es obligatorio");
        }
        this.purchase = value;
    }

    private void changePurchaseOrderItem(PurchaseOrderItem value) {
        this.purchaseOrderItem = value;
    }

    private void changeIngredient(Ingredient value) {
        if (value == null) {
            throw new InvalidEntityDataException("El insumo es obligatorio");
        }
        this.ingredient = value;
    }

    private void changePresentation(IngredientPresentation value) {
        this.presentation = value;
    }

    private void changeWarehouse(Warehouse value) {
        if (value == null) {
            throw new InvalidEntityDataException("El almacén es obligatorio");
        }
        this.warehouse = value;
    }

    public void changeReceivedAt(LocalDateTime value) {
        this.receivedAt = value;
    }

    public void changePresentationQuantity(BigDecimal value) {
        if (value == null) {
            throw new InvalidEntityDataException("La presentation cantidad es obligatoria");
        }
        if (value != null && value.abs().compareTo(new BigDecimal("999999999999.999999")) > 0) {
            throw new InvalidEntityDataException("La presentation cantidad supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 6) {
            throw new InvalidEntityDataException("La presentation cantidad no admite más de 6 decimales");
        }
        if (value != null && value.signum() <= 0) {
            throw new InvalidEntityDataException("La presentation cantidad debe ser mayor que cero");
        }
        this.presentationQuantity = value;
    }

    public void changeAcceptedBaseQuantity(BigDecimal value) {
        if (value == null) {
            throw new InvalidEntityDataException("La accepted base cantidad es obligatoria");
        }
        if (value != null && value.abs().compareTo(new BigDecimal("999999999999.999999")) > 0) {
            throw new InvalidEntityDataException("La accepted base cantidad supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 6) {
            throw new InvalidEntityDataException("La accepted base cantidad no admite más de 6 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("La accepted base cantidad no puede ser negativo");
        }
        this.acceptedBaseQuantity = value;
    }

    public void changeRejectedBaseQuantity(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("999999999999.999999")) > 0) {
            throw new InvalidEntityDataException("La rejected base cantidad supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 6) {
            throw new InvalidEntityDataException("La rejected base cantidad no admite más de 6 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("La rejected base cantidad no puede ser negativo");
        }
        if (value != null && value.signum() <= 0) {
            throw new InvalidEntityDataException("La rejected base cantidad debe ser mayor que cero");
        }
        this.rejectedBaseQuantity = value;
    }

    public void changeGrossWeight(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("999999999999.999999")) > 0) {
            throw new InvalidEntityDataException("El gross weight supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 6) {
            throw new InvalidEntityDataException("El gross weight no admite más de 6 decimales");
        }
        this.grossWeight = value;
    }

    public void changeTareWeight(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("999999999999.999999")) > 0) {
            throw new InvalidEntityDataException("El tare weight supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 6) {
            throw new InvalidEntityDataException("El tare weight no admite más de 6 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El tare weight no puede ser negativo");
        }
        this.tareWeight = value;
    }

    public void changeTemperature(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999.99")) > 0) {
            throw new InvalidEntityDataException("La temperature supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("La temperature no admite más de 2 decimales");
        }
        this.temperature = value;
    }

    public void changeAcceptedTotalCost(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El accepted total cost supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El accepted total cost no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El accepted total cost no puede ser negativo");
        }
        this.acceptedTotalCost = value;
    }

    public void changeSubtotal(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El subtotal supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El subtotal no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El subtotal no puede ser negativo");
        }
        this.subtotal = value;
    }

    public void changeTax(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El impuesto supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El impuesto no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El impuesto no puede ser negativo");
        }
        this.tax = value;
    }

    public void changeTotal(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El total supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El total no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El total no puede ser negativo");
        }
        this.total = value;
    }

    public void changeRejectionReason(String value) {
        if (value == null || value.isBlank()) {
            this.rejectionReason = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 250) {
            throw new InvalidEntityDataException("El rejection reason no puede superar 250 caracteres");
        }
        this.rejectionReason = normalized;
    }

    public void changeEvidenceUrl(String value) {
        if (value == null || value.isBlank()) {
            this.evidenceUrl = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 1000) {
            throw new InvalidEntityDataException("El URL de evidencia no puede superar 1000 caracteres");
        }
        this.evidenceUrl = normalized;
    }
}