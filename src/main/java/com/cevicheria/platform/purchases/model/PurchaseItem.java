package com.cevicheria.platform.purchases.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "purchase_item")
public class PurchaseItem {

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
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_item_id")
    private PurchaseOrderItem purchaseOrderItem;

    // TODO: FK real cuando turinovi publique Ingredient
    @Column(name = "ingredient_id", nullable = false)
    private Long ingredientId;

    // TODO: FK real cuando turinovi publique IngredientPresentation
    @Column(name = "presentation_id")
    private Long presentationId;

    // TODO: FK real cuando turinovi publique Warehouse
    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "received_at")
    private LocalDateTime receivedAt;

    @Column(name = "presentation_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal presentationQuantity;

    // cantidad en unidad base que pasa al stock
    @Column(name = "accepted_base_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal acceptedBaseQuantity;

    @Column(name = "rejected_base_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal rejectedBaseQuantity = BigDecimal.ZERO;

    @Column(name = "gross_weight", precision = 18, scale = 6)
    private BigDecimal grossWeight; // con envase

    @Column(name = "tare_weight", precision = 18, scale = 6)
    private BigDecimal tareWeight;

    @Column(name = "temperature", precision = 6, scale = 2)
    private BigDecimal temperature; // cadena de frío

    @Column(name = "accepted_total_cost", nullable = false, precision = 18, scale = 2)
    private BigDecimal acceptedTotalCost = BigDecimal.ZERO;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal tax = BigDecimal.ZERO;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    // obligatorio si rejectedBaseQuantity > 0
    @Column(name = "rejection_reason", length = 250)
    private String rejectionReason;

    @Column(name = "evidence_url", length = 1000)
    private String evidenceUrl;
}
