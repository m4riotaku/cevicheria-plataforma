package com.cevicheria.platform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.generator.EventType;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "delivery_zone", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"business_id", "branch_id", "name"})
})
public class DeliveryZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    private String name;

    private boolean active = true;

    private BigDecimal fee = BigDecimal.ZERO;

    private BigDecimal minimumOrderAmount = BigDecimal.ZERO;

    private Integer estimatedMinutes;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> coverage;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> schedule;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;
}
