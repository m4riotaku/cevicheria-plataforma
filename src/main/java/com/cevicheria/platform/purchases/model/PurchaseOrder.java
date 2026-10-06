package com.cevicheria.platform.purchases.model;

import com.cevicheria.platform.purchases.model.enums.PurchaseOrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "purchase_order", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"business_id", "branch_id", "number"})
})
public class PurchaseOrder {

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
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    // TODO: FK real cuando turinovi publique Warehouse
    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(nullable = false, length = 40)
    private String number; // ej: OC-0001

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "expected_delivery_date")
    private LocalDate expectedDeliveryDate;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode = "PEN";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PurchaseOrderStatus status = PurchaseOrderStatus.BORRADOR;

    // TODO: FK real cuando MarcoGZRo publique UserBusiness
    @Column(name = "approved_by_user_business_id")
    private Long approvedByUserBusinessId;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
