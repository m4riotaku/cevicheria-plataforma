package com.cevicheria.platform.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.cevicheria.platform.model.enums.InventoryMovementType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Getter
@Setter
@Entity
@Table(name = "inventory_movement")
public class InventoryMovement {

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
    @JoinColumn(name = "operation_id")
    private InventoryOperation operation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "batch_id")
    private InventoryBatch batch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_item_id")
    private PurchaseItem purchaseItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_consumption_id")
    private OrderConsumption orderConsumption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_movement_id")
    private InventoryMovement sourceMovement;

    private Integer sequenceNumber;

    @Enumerated(EnumType.STRING)
    private InventoryMovementType type;

    private BigDecimal quantity = BigDecimal.ZERO;

    private BigDecimal unitCost = BigDecimal.ZERO;

    @Generated(event = {EventType.INSERT, EventType.UPDATE})
    private BigDecimal valueAmount;

    private BigDecimal expectedQuantity;

    private BigDecimal countedQuantity;

    private LocalDateTime recordDate;

    private String concept;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;
}