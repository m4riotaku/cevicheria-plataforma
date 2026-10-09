package com.cevicheria.platform.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.generator.EventType;
import org.hibernate.type.SqlTypes;

import com.cevicheria.platform.model.enums.SalesReceiptStates;

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
public class SalesReceipt
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receipt_series_id", nullable = false)
    private ReceiptSeries receiptSeries;

    @Column(nullable = false)
    private Long number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_bill_id")
    private OrderBill orderBill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_receipt_id")
    private SalesReceipt originalReceipt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(nullable = false)
    private LocalDateTime recordDate;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "json")
    private Map<String, Object> issuerSnapshot;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "json")
    private Map<String, Object> customerSnapshot;

    @Column(nullable = false, length = 3, columnDefinition = "char(3)")
    private String currencyCode = "PEN";

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

    private String adjustmentReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorized_by_user_business_id")
    private UserBusiness authorizedBy;

    @Enumerated(EnumType.STRING)
    private SalesReceiptStates status = SalesReceiptStates.BORRADOR;

    private String submissionTicket;

    private String responseCode;

    private String responseMessage;

    private String xmlUrl;

    private String cdrUrl;

    private String pdfUrl;

    private Integer submissionAttempts = 0;

    private LocalDateTime lastSubmissionAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<String, Object> integrationData;

    @CurrentTimestamp(event = EventType.INSERT)
    @Setter(AccessLevel.NONE)
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
