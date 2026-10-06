package com.cevicheria.platform.purchases.model;

import com.cevicheria.platform.purchases.model.enums.PurchaseStatus;
import com.cevicheria.platform.purchases.model.enums.PurchaseType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "purchase", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"business_id", "supplier_id", "type", "series", "number"})
})
public class Purchase {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder; // null en compras directas sin OC

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_purchase_id")
    private Purchase sourcePurchase; // requerido en notas de crédito/débito

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PurchaseType type;

    @Column(nullable = false, length = 20)
    private String series; // ej: F001, B001

    @Column(nullable = false, length = 30)
    private String number;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode = "PEN";

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal tax = BigDecimal.ZERO;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private PurchaseStatus status = PurchaseStatus.BORRADOR;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
