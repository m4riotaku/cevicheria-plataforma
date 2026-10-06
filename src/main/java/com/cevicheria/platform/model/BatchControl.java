package com.cevicheria.platform.model;

import java.math.BigDecimal;
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
@Table(name = "batch_control")
public class BatchControl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long inventoryBatchId;

    @Column(nullable = false, length = 40)
    private String controlType;

    @Column(precision = 7, scale = 2)
    private BigDecimal temperatureCelsius;

    @Column(length = 100)
    private String condition;

    @Column(length = 500)
    private String notes;

    private Long checkedByEmployeeId;

    @Column(nullable = false)
    private LocalDateTime checkedAt;
}