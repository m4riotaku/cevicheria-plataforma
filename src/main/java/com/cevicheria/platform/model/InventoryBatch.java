package com.cevicheria.platform.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "inventory_batch")
public class InventoryBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long ingredientId;

    @Column(nullable = false)
    private Long warehouseId;

    @Column(length = 100)
    private String batchCode;

    @Column(nullable = false, precision = 14, scale = 4)
    private BigDecimal receivedQuantity;

    @Column(nullable = false, precision = 14, scale = 4)
    private BigDecimal remainingQuantity;

    @Column(nullable = false, precision = 14, scale = 4)
    private BigDecimal unitCost;

    @Column(nullable = false)
    private LocalDateTime receivedAt;

    private LocalDate expirationDate;

    @Column(nullable = false, length = 30)
    private String status;
}