package com.cevicheria.platform.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.cevicheria.platform.model.enums.InventoryBatchStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Getter
@Setter
@Entity
@Table(name = "inventory_batch")
public class InventoryBatch {

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
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_item_id")
    private PurchaseItem purchaseItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_batch_id")
    private InventoryBatch sourceBatch;

    private String code;

    private LocalDateTime receivedAt;

    private LocalDate dueOn;

    private BigDecimal onHandQuantity = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private InventoryBatchStatus status = InventoryBatchStatus.APTO;

    private String blockReason;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;
}