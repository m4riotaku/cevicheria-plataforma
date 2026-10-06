package com.cevicheria.platform.purchases.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "purchase_order_item")
public class PurchaseOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: FK real cuando M4riotaku publique Business
    @Column(name = "business_id", nullable = false)
    private Long businessId;

    // TODO: FK real cuando M4riotaku publique Branch
    @Column(name = "branch_id", nullable = false)
    private Long branchId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrder purchaseOrder;

    // TODO: FK real cuando turinovi publique Ingredient
    @Column(name = "ingredient_id", nullable = false)
    private Long ingredientId;

    // TODO: FK real cuando turinovi publique IngredientPresentation
    @Column(name = "presentation_id")
    private Long presentationId;

    @Column(nullable = false, precision = 18, scale = 6)
    private BigDecimal quantity;

    // cantidad estimada en unidad base (kg, l, und) luego de convertir la presentación
    @Column(name = "estimated_base_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal estimatedBaseQuantity;

    @Column(name = "unit_price", nullable = false, precision = 18, scale = 2)
    private BigDecimal unitPrice = BigDecimal.ZERO;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;
}
