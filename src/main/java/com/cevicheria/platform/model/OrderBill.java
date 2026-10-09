package com.cevicheria.platform.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.generator.EventType;

import com.cevicheria.platform.model.enums.OrderBillCriterion;
import com.cevicheria.platform.model.enums.OrderBillStates;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class OrderBill
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private CustomerOrder order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private Integer number;

    private String name;

    @Enumerated(EnumType.STRING)
    private OrderBillCriterion criterion = OrderBillCriterion.COMPLETA;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal subtotal = new BigDecimal("0.00");

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal discount = new BigDecimal("0.00");

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal tax = new BigDecimal("0.00");

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal deliveryFee = new BigDecimal("0.00");

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal total = new BigDecimal("0.00");

    @Enumerated(EnumType.STRING)
    private OrderBillStates status = OrderBillStates.ABIERTA;

    @CurrentTimestamp(event = EventType.INSERT)
    @Setter(AccessLevel.NONE)
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
