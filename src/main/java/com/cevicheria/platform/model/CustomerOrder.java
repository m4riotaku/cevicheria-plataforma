package com.cevicheria.platform.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.cevicheria.platform.model.enums.OrderSource;
import com.cevicheria.platform.model.enums.OrderStatus;
import com.cevicheria.platform.model.enums.SalesChannel;

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
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "business_id",
        referencedColumnName = "id"
    )
    private Business business;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "branch_id",
        referencedColumnName = "id"
    )
    private Branch branch;

    @Column(nullable = false, length = 40)
    private String number;

    @Column(nullable = false, length = 64)
    private String requestKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "customer_id",
        referencedColumnName = "id"
    )
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "dining_table_id",
        referencedColumnName = "id"
    )
    private DiningTable diningTable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "reservation_id",
        referencedColumnName = "id"
    )
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "waiter_id",
        referencedColumnName = "id"
    )
    private Employee waiter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "merged_into_order_id",
        referencedColumnName = "id"
    )
    private CustomerOrder mergedIntoOrder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SalesChannel salesChannel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderSource source = OrderSource.PERSONAL;

    @Column(nullable = false)
    private LocalDateTime recordDate;

    @Column(nullable = false)
    private Integer guestCount = 1;

    private String contactName;

    private String contactPhone;

    private String deliveryAddress;

    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(32)")
    private byte[] trackingTokenHash;

    @Column(nullable = false, length = 3)
    private String currencyCode = "PEN";

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal tax = BigDecimal.ZERO;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal deliveryFee = BigDecimal.ZERO;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.SOLICITADO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "authorized_by_user_business_id",
        referencedColumnName = "id"
    )
    private UserBusiness authorizedByUserBusiness;

    @Column(length = 250)
    private String changeReason;

    private String notes;

    @CurrentTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
