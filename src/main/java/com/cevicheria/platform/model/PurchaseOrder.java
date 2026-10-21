package com.cevicheria.platform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.cevicheria.platform.model.enums.PurchaseOrderStatus;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    private String number;

    private LocalDate recordDate;

    private LocalDate expectedDeliveryDate;

    private String currencyCode = "PEN";

    @Enumerated(EnumType.STRING)
    private PurchaseOrderStatus status = PurchaseOrderStatus.BORRADOR;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_user_business_id")
    private UserBusiness approvedByUserBusiness;

    private LocalDateTime approvedAt;

    private BigDecimal total = BigDecimal.ZERO;

    private String notes;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;
}
