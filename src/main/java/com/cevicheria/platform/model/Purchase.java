package com.cevicheria.platform.model;

import com.cevicheria.platform.model.enums.PurchaseStatus;
import com.cevicheria.platform.model.enums.PurchaseType;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_purchase_id")
    private Purchase sourcePurchase;

    @Enumerated(EnumType.STRING)
    private PurchaseType type;

    private String series;

    private String number;

    private LocalDate recordDate;

    private LocalDate expirationDate;

    private String currencyCode = "PEN";

    private BigDecimal subtotal = BigDecimal.ZERO;

    private BigDecimal tax = BigDecimal.ZERO;

    private BigDecimal total = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private PurchaseStatus status = PurchaseStatus.BORRADOR;

    private String notes;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;
}
