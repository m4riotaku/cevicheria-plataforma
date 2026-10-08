package com.cevicheria.platform.model;

import com.cevicheria.platform.model.enums.DeliveryStatus;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: FK real cuando M4riotaku publique Business
    @Column(name = "business_id", nullable = false)
    private Long businessId;

    // TODO: FK real cuando M4riotaku publique Branch
    @Column(name = "branch_id", nullable = false)
    private Long branchId;

    // TODO: FK real cuando M4riotaku publique CustomerOrder
    @Column(name = "customer_order_id", nullable = false)
    private Long customerOrderId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "delivery_zone_id", nullable = false)
    private DeliveryZone deliveryZone;

    // TODO: FK real cuando MarcoGZRo publique Employee
    @Column(name = "driver_employee_id")
    private Long driverEmployeeId;

    @Column(name = "delivery_address", nullable = false, columnDefinition = "TEXT")
    private String deliveryAddress;

    @Column(name = "delivery_fee", nullable = false, precision = 18, scale = 2)
    private BigDecimal deliveryFee = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DeliveryStatus status = DeliveryStatus.PENDIENTE;

    @Column(name = "dispatched_at")
    private LocalDateTime dispatchedAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    // requerido si status = NO_ENTREGADO
    @Column(name = "failure_reason", length = 250)
    private String failureReason;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
