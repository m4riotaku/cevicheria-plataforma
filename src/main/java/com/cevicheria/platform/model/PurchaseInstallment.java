package com.cevicheria.platform.model;

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
@Table(name = "purchase_installment", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"business_id", "branch_id", "purchase_id", "number"})
})
public class PurchaseInstallment {

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
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    private Integer number;

    private LocalDate dueOn;

    private BigDecimal amount = BigDecimal.ZERO;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;
}
