package com.cevicheria.platform.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.generator.EventType;

import com.cevicheria.platform.model.enums.PaymentConcept;
import com.cevicheria.platform.model.enums.PaymentDirection;
import com.cevicheria.platform.model.enums.PaymentMethod;
import com.cevicheria.platform.model.enums.PaymentStates;

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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Payment
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

    private String operationKey;

    @Enumerated(EnumType.STRING)
    private PaymentDirection direction;

    @Enumerated(EnumType.STRING)
    private PaymentConcept concept;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "financial_account_id")
    private FinancialAccount financialAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cash_shift_id")
    private CashShift cashShift;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_payment_id")
    private Payment sourcePayment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime recordDate;

    private String currencyCode = "PEN";

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount = new BigDecimal("0.00");

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal tip = new BigDecimal("0.00");

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amountReceived;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal changeAmount = new BigDecimal("0.00");

    private String externalReference;

    private String evidenceUrl;

    @Enumerated(EnumType.STRING)
    private PaymentStates status = PaymentStates.PENDIENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by_user_business_id")
    private UserBusiness verifiedBy;

    private LocalDateTime verifiedAt;

    @CurrentTimestamp(event = EventType.INSERT)
    @Setter(AccessLevel.NONE)
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

}
