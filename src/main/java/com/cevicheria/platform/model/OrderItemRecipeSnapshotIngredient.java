package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.id.OrderItemRecipeSnapshotIngredientId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.MapsId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemRecipeSnapshotIngredient {

    @EmbeddedId
    private OrderItemRecipeSnapshotIngredientId id;

    private String ingredientNameSnapshot;

    private String baseUnitSnapshot;

    private BigDecimal baseQuantityPerServing;

    @MapsId("businessId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @MapsId("branchId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @MapsId("orderItemId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    @MapsId("ingredientId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    public OrderItemRecipeSnapshotIngredient(
            Business business,
            Branch branch,
            OrderItem orderItem,
            Ingredient ingredient,
            String ingredientNameSnapshot,
            String baseUnitSnapshot,
            BigDecimal baseQuantityPerServing) {
        this.id = new OrderItemRecipeSnapshotIngredientId(null, null, null, null);
        changeBusiness(business);
        changeBranch(branch);
        changeOrderItem(orderItem);
        changeIngredient(ingredient);
        changeIngredientNameSnapshot(ingredientNameSnapshot);
        changeBaseUnitSnapshot(baseUnitSnapshot);
        changeBaseQuantityPerServing(baseQuantityPerServing);
        if (business.getId() != null && branch.getBusiness() != null
                && branch.getBusiness().getId() != null
                && !business.getId().equals(branch.getBusiness().getId())) {
            throw new InvalidEntityDataException("El local no pertenece al negocio seleccionado");
        }
    }

    private void changeBusiness(Business business) {
        if (business == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        if (business.getId() == null) {
            throw new InvalidEntityDataException("El negocio debe estar registrado antes de realizar la asociación");
        }
        this.business = business;
        this.id = new OrderItemRecipeSnapshotIngredientId(business.getId(), this.id.getBranchId(), this.id.getOrderItemId(), this.id.getIngredientId());
    }

    private void changeBranch(Branch branch) {
        if (branch == null) {
            throw new InvalidEntityDataException("El local es obligatorio");
        }
        if (branch.getId() == null) {
            throw new InvalidEntityDataException("El local debe estar registrado antes de realizar la asociación");
        }
        this.branch = branch;
        this.id = new OrderItemRecipeSnapshotIngredientId(this.id.getBusinessId(), branch.getId(), this.id.getOrderItemId(), this.id.getIngredientId());
    }

    private void changeOrderItem(OrderItem orderItem) {
        if (orderItem == null) {
            throw new InvalidEntityDataException("El detalle del pedido es obligatorio");
        }
        if (orderItem.getId() == null) {
            throw new InvalidEntityDataException("El detalle del pedido debe estar registrado antes de realizar la asociación");
        }
        this.orderItem = orderItem;
        this.id = new OrderItemRecipeSnapshotIngredientId(this.id.getBusinessId(), this.id.getBranchId(), orderItem.getId(), this.id.getIngredientId());
    }

    private void changeIngredient(Ingredient ingredient) {
        if (ingredient == null) {
            throw new InvalidEntityDataException("El ingrediente es obligatorio");
        }
        if (ingredient.getId() == null) {
            throw new InvalidEntityDataException("El ingrediente debe estar registrado antes de realizar la asociación");
        }
        this.ingredient = ingredient;
        this.id = new OrderItemRecipeSnapshotIngredientId(this.id.getBusinessId(), this.id.getBranchId(), this.id.getOrderItemId(), ingredient.getId());
    }

    private void changeIngredientNameSnapshot(String ingredientNameSnapshot) {
        if (ingredientNameSnapshot == null) {
            throw new InvalidEntityDataException("El nombre histórico del ingrediente es obligatorio");
        }
        if (ingredientNameSnapshot.isBlank()) {
            throw new InvalidEntityDataException("El nombre histórico del ingrediente no puede estar vacío");
        }
        String normalizedValue = ingredientNameSnapshot.trim();
        if (normalizedValue.length() > 120) {
            throw new InvalidEntityDataException("El nombre histórico del ingrediente no puede superar 120 caracteres");
        }
        this.ingredientNameSnapshot = normalizedValue;
    }

    private void changeBaseUnitSnapshot(String baseUnitSnapshot) {
        if (baseUnitSnapshot == null) {
            throw new InvalidEntityDataException("La unidad base de la receta es obligatoria");
        }
        if (baseUnitSnapshot.isBlank()) {
            throw new InvalidEntityDataException("La unidad base de la receta no puede estar vacío");
        }
        String normalizedValue = baseUnitSnapshot.trim();
        if (normalizedValue.length() > 10) {
            throw new InvalidEntityDataException("La unidad base de la receta no puede superar 10 caracteres");
        }
        if (!normalizedValue.equals("UND") && !normalizedValue.equals("KG") && !normalizedValue.equals("L")) {
            throw new InvalidEntityDataException("La unidad debe ser UND, KG o L");
        }
        this.baseUnitSnapshot = normalizedValue;
    }

    private void changeBaseQuantityPerServing(BigDecimal baseQuantityPerServing) {
        if (baseQuantityPerServing == null) {
            throw new InvalidEntityDataException("La cantidad base por porción es obligatoria");
        }
        if (baseQuantityPerServing.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidEntityDataException("La cantidad base por porción debe ser mayor que cero");
        }
        if (baseQuantityPerServing.abs().compareTo(new BigDecimal("999999999999.999999")) > 0) {
            throw new InvalidEntityDataException("La cantidad base por porción excede DECIMAL(18,6)");
        }
        if (baseQuantityPerServing.stripTrailingZeros().scale() > 6) {
            throw new InvalidEntityDataException("La cantidad base por porción admite hasta 6 decimales");
        }
        this.baseQuantityPerServing = baseQuantityPerServing.setScale(6);
    }
}
