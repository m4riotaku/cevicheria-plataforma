package com.cevicheria.platform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.cevicheria.platform.model.enums.ReservationStatus;

@Getter
@Setter
@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: FK real cuando M4riotaku publique Business
    @Column(name = "business_id", nullable = false)
    private Long businessId;

    // TODO: FK real cuando M4riotaku publique Branch
    @Column(name = "branch_id", nullable = false)
    private Long branchId;

    // TODO: FK real cuando v0oidzx publique Customer
    @Column(name = "customer_id")
    private Long customerId;

    // TODO: FK real cuando M4riotaku publique DiningTable
    @Column(name = "dining_table_id")
    private Long diningTableId; // se asigna al llegar si no se reservó mesa específica

    // TODO: FK real cuando M4riotaku publique CustomerOrder
    @Column(name = "customer_order_id")
    private Long customerOrderId;

    @Column(name = "scheduled_at", nullable = false)
    private LocalDateTime scheduledAt;

    @Column(name = "party_size", nullable = false)
    private Integer partySize;

    @Column(name = "deposit_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal depositAmount = BigDecimal.ZERO;

    @Column(name = "deposit_consumed", nullable = false)
    private boolean depositConsumed = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservationStatus status = ReservationStatus.PENDIENTE;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
