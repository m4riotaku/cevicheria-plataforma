package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.id.OrderItemRecipeSnapshotId;
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
public class OrderItemRecipeSnapshot {

    @EmbeddedId
    private OrderItemRecipeSnapshotId id;

    private Integer recipeVersion;

    private BigDecimal servings;

    private BigDecimal yieldRatio;

    private String instructionsSnapshot;

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

    public OrderItemRecipeSnapshot(
            Business business,
            Branch branch,
            OrderItem orderItem,
            Integer recipeVersion,
            BigDecimal servings,
            BigDecimal yieldRatio,
            String instructionsSnapshot) {
        this.id = new OrderItemRecipeSnapshotId(null, null, null);
        changeBusiness(business);
        changeBranch(branch);
        changeOrderItem(orderItem);
        changeRecipeVersion(recipeVersion);
        changeServings(servings);
        changeYieldRatio(yieldRatio);
        changeInstructionsSnapshot(instructionsSnapshot);
        if (business.getId() != null && branch.getBusiness() != null
                && branch.getBusiness().getId() != null
                && !business.getId().equals(branch.getBusiness().getId())) {
            throw new InvalidEntityDataException("El local no pertenece al negocio seleccionado");
        }
        validateYieldRatio();
    }

    private void changeBusiness(Business business) {
        if (business == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        if (business.getId() == null) {
            throw new InvalidEntityDataException("El negocio debe estar registrado antes de realizar la asociación");
        }
        this.business = business;
        this.id = new OrderItemRecipeSnapshotId(business.getId(), this.id.getBranchId(), this.id.getOrderItemId());
    }

    private void changeBranch(Branch branch) {
        if (branch == null) {
            throw new InvalidEntityDataException("El local es obligatorio");
        }
        if (branch.getId() == null) {
            throw new InvalidEntityDataException("El local debe estar registrado antes de realizar la asociación");
        }
        this.branch = branch;
        this.id = new OrderItemRecipeSnapshotId(this.id.getBusinessId(), branch.getId(), this.id.getOrderItemId());
    }

    private void changeOrderItem(OrderItem orderItem) {
        if (orderItem == null) {
            throw new InvalidEntityDataException("El detalle del pedido es obligatorio");
        }
        if (orderItem.getId() == null) {
            throw new InvalidEntityDataException("El detalle del pedido debe estar registrado antes de realizar la asociación");
        }
        this.orderItem = orderItem;
        this.id = new OrderItemRecipeSnapshotId(this.id.getBusinessId(), this.id.getBranchId(), orderItem.getId());
    }

    private void changeRecipeVersion(Integer recipeVersion) {
        if (recipeVersion == null) {
            throw new InvalidEntityDataException("La versión de receta es obligatoria");
        }
        if (recipeVersion < 1) {
            throw new InvalidEntityDataException("La versión de receta debe ser mayor que cero");
        }
        this.recipeVersion = recipeVersion;
    }

    private void changeServings(BigDecimal servings) {
        if (servings == null) {
            throw new InvalidEntityDataException("Las porciones son obligatorias");
        }
        if (servings.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidEntityDataException("Las porciones debe ser mayor que cero");
        }
        if (servings.abs().compareTo(new BigDecimal("999999999999.999999")) > 0) {
            throw new InvalidEntityDataException("Las porciones excede DECIMAL(18,6)");
        }
        if (servings.stripTrailingZeros().scale() > 6) {
            throw new InvalidEntityDataException("Las porciones admite hasta 6 decimales");
        }
        this.servings = servings.setScale(6);
    }

    private void changeYieldRatio(BigDecimal yieldRatio) {
        if (yieldRatio == null) {
            this.yieldRatio = null;
            return;
        }
        if (yieldRatio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidEntityDataException("El rendimiento de la receta debe ser mayor que cero");
        }
        if (yieldRatio.abs().compareTo(new BigDecimal("999.999999")) > 0) {
            throw new InvalidEntityDataException("El rendimiento de la receta excede DECIMAL(9,6)");
        }
        if (yieldRatio.stripTrailingZeros().scale() > 6) {
            throw new InvalidEntityDataException("El rendimiento de la receta admite hasta 6 decimales");
        }
        if (yieldRatio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidEntityDataException("El rendimiento debe ser mayor que cero");
        }
        if (yieldRatio.compareTo(BigDecimal.ONE) > 0) {
            throw new InvalidEntityDataException("El rendimiento no puede superar 1");
        }
        this.yieldRatio = yieldRatio.setScale(6);
    }

    private void changeInstructionsSnapshot(String instructionsSnapshot) {
        if (instructionsSnapshot == null || instructionsSnapshot.isBlank()) {
            this.instructionsSnapshot = null;
            return;
        }
        String normalizedValue = instructionsSnapshot.trim();
        this.instructionsSnapshot = normalizedValue;
    }

    private void validateYieldRatio() {
        if (this.yieldRatio != null && this.yieldRatio.compareTo(BigDecimal.ONE) > 0) {
            throw new InvalidEntityDataException("El rendimiento no puede ser superior a 1");
        }
    }
}
