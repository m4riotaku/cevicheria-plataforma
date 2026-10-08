package com.cevicheria.platform.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.cevicheria.platform.model.enums.OrderItemDestination;
import com.cevicheria.platform.model.enums.OrderItemStatus;

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
public class OrderItem {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "order_id",
        referencedColumnName = "id"
    )
    private CustomerOrder order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "parent_order_item_id",
        referencedColumnName = "id"
    )
    private OrderItem parentOrderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "product_id",
        referencedColumnName = "id"
    )
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "recipe_id",
        referencedColumnName = "id"
    )
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "preparation_area_id",
        referencedColumnName = "id"
    )
    private PreparationArea preparationArea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "original_waiter_id",
        referencedColumnName = "id"
    )
    private Employee originalWaiter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "promotion_id",
        referencedColumnName = "id"
    )
    private Promotion promotion;

    @Column(nullable = false, length = 200)
    private String description;

    @Column(nullable = false, precision = 18, scale = 6)
    private BigDecimal quantity;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal unitPrice = BigDecimal.ZERO;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal tax = BigDecimal.ZERO;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(nullable = false, precision = 7, scale = 4)
    private BigDecimal taxRate = new BigDecimal("0.18");

    @Column(nullable = false, length = 5)
    private String taxTreatmentCode = "10";

    @Column(nullable = false, length = 10)
    private String saleUnit = "UND";

    @Column(nullable = false)
    private Boolean comboComponent = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderItemDestination destination = OrderItemDestination.VENTA;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderItemStatus status = OrderItemStatus.PENDIENTE;

    private LocalDateTime confirmedAt;

    private LocalDateTime preparationStartedAt;

    private LocalDateTime readyAt;

    private LocalDateTime deliveredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "prepared_by_employee_id",
        referencedColumnName = "id"
    )
    private Employee preparedByEmployee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "authorized_by_user_business_id",
        referencedColumnName = "id"
    )
    private UserBusiness authorizedByUserBusiness;

    @Column(length = 250)
    private String cancellationReason;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    private String modifiers;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    private String recipeSnapshot;

    private String preparationNote;

    @CurrentTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
