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
public class PurchaseOrderItem {

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
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "presentation_id")
    private IngredientPresentation presentation;

    private BigDecimal quantity;

    private BigDecimal estimatedBaseQuantity;

    private BigDecimal unitPrice = new BigDecimal("0.00");

    private BigDecimal total = new BigDecimal("0.00");

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public PurchaseOrderItem(Business business, Branch branch, PurchaseOrder purchaseOrder, Ingredient ingredient, BigDecimal quantity, BigDecimal estimatedBaseQuantity) {
        changeBusiness(business);
        changeBranch(branch);
        changePurchaseOrder(purchaseOrder);
        changeIngredient(ingredient);
        changeQuantity(quantity);
        changeEstimatedBaseQuantity(estimatedBaseQuantity);
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

    private void changePurchaseOrder(PurchaseOrder value) {
        if (value == null) {
            throw new InvalidEntityDataException("El purchase pedido es obligatorio");
        }
        this.purchaseOrder = value;
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

    public void changeQuantity(BigDecimal value) {
        if (value == null) {
            throw new InvalidEntityDataException("La cantidad es obligatoria");
        }
        if (value != null && value.abs().compareTo(new BigDecimal("999999999999.999999")) > 0) {
            throw new InvalidEntityDataException("La cantidad supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 6) {
            throw new InvalidEntityDataException("La cantidad no admite más de 6 decimales");
        }
        if (value != null && value.signum() <= 0) {
            throw new InvalidEntityDataException("La cantidad debe ser mayor que cero");
        }
        this.quantity = value;
    }

    public void changeEstimatedBaseQuantity(BigDecimal value) {
        if (value == null) {
            throw new InvalidEntityDataException("La estimated base cantidad es obligatoria");
        }
        if (value != null && value.abs().compareTo(new BigDecimal("999999999999.999999")) > 0) {
            throw new InvalidEntityDataException("La estimated base cantidad supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 6) {
            throw new InvalidEntityDataException("La estimated base cantidad no admite más de 6 decimales");
        }
        if (value != null && value.signum() <= 0) {
            throw new InvalidEntityDataException("La estimated base cantidad debe ser mayor que cero");
        }
        this.estimatedBaseQuantity = value;
    }

    public void changeUnitPrice(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El precio unitario supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El precio unitario no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El precio unitario no puede ser negativo");
        }
        this.unitPrice = value;
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
}