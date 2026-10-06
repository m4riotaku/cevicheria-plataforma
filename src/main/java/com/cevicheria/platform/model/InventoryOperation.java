package com.cevicheria.platform.model;

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
@Table(name = "inventory_operation")
public class InventoryOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long businessId;

    private Long sourceWarehouseId;

    private Long destinationWarehouseId;

    @Column(nullable = false, length = 40)
    private String operationType;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(length = 100)
    private String referenceCode;

    @Column(length = 500)
    private String reason;

    private Long createdByEmployeeId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime completedAt;
}