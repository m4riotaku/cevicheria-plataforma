package com.cevicheria.platform.purchases.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "purchase_installment", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"business_id", "branch_id", "purchase_id", "number"})
})
public class PurchaseInstallment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "business_id", nullable = false)
    private Long businessId;

    @Column(name = "branch_id", nullable = false)
    private Long branchId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;

    @Column(nullable = false)
    private Integer number; // Número de cuota (1, 2, 3...)

    @Column(name = "due_on", nullable = false)
    private LocalDate dueOn; // Fecha de vencimiento de la cuota

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount = BigDecimal.ZERO; // Monto de la cuota
}
