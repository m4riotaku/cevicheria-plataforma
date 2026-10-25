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
public class OrderConsumption {

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
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    private BigDecimal baseQuantity;

    private BigDecimal totalCost = new BigDecimal("0.00");

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public OrderConsumption(Business business, Branch branch, OrderItem orderItem, Ingredient ingredient, BigDecimal baseQuantity) {
        changeBusiness(business);
        changeBranch(branch);
        changeOrderItem(orderItem);
        changeIngredient(ingredient);
        changeBaseQuantity(baseQuantity);
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

    private void changeOrderItem(OrderItem value) {
        if (value == null) {
            throw new InvalidEntityDataException("El pedido item es obligatorio");
        }
        this.orderItem = value;
    }

    private void changeIngredient(Ingredient value) {
        if (value == null) {
            throw new InvalidEntityDataException("El insumo es obligatorio");
        }
        this.ingredient = value;
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

    public void changeTotalCost(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El total cost supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El total cost no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El total cost no puede ser negativo");
        }
        this.totalCost = value;
    }
}