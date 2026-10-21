package com.cevicheria.platform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import com.cevicheria.platform.model.enums.ReservationStatus;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.generator.EventType;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dining_table_id")
    private DiningTable diningTable;

    private String contactName;

    private String phone;

    private LocalDateTime startsAt;

    private LocalDateTime endsAt;

    private Integer guestCount;

    private Integer toleranceMinutes = 15;

    private BigDecimal requiredDeposit = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.PENDIENTE;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> cancellationPolicy;

    private String notes;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;
}
