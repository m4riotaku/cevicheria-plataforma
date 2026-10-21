package com.cevicheria.platform.model;

import com.cevicheria.platform.model.enums.DeliveryStatus;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id")
    private CustomerOrder order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "delivery_zone_id")
    private DeliveryZone deliveryZone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_employee_id")
    private Employee driverEmployee;

    private String driverName;

    private String driverPhone;

    private String address;

    private BigDecimal deliveryFeeSnapshot = BigDecimal.ZERO;

    private Integer estimatedMinutes;

    private BigDecimal cashOnDeliveryAmount = BigDecimal.ZERO;

    private LocalDateTime dispatchedAt;

    private LocalDateTime deliveredAt;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status = DeliveryStatus.PENDIENTE;

    private String addressReference;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;
}
