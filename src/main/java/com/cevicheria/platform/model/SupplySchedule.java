package com.cevicheria.platform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "supply_schedule")
public class SupplySchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: FK real cuando M4riotaku publique Business
    @Column(name = "business_id", nullable = false)
    private Long businessId;

    // TODO: FK real cuando M4riotaku publique Branch
    @Column(name = "branch_id", nullable = false)
    private Long branchId;

    // TODO: FK real cuando turinovi publique Warehouse
    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    // TODO: FK real cuando turinovi publique Ingredient
    @Column(name = "ingredient_id", nullable = false)
    private Long ingredientId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alternate_supplier_id")
    private Supplier alternateSupplier; // respaldo

    @Column(name = "base_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal baseQuantity;

    @Column(name = "frequency_days", nullable = false)
    private Integer frequencyDays; // en días, ej: 7 = semanal

    @Column(name = "next_date", nullable = false)
    private LocalDate nextDate;

    @Column(nullable = false)
    private boolean active = true;
}
