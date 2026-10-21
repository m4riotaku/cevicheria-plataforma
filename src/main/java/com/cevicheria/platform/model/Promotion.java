package com.cevicheria.platform.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import com.cevicheria.platform.model.enums.PromotionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.generator.EventType;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "promotion")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    private String name;

    private boolean active = true;

    @Enumerated(EnumType.STRING)
    private PromotionType type;

    private BigDecimal valueAmount = BigDecimal.ZERO;

    private Integer priority = 0;

    private boolean stackable = false;

    private LocalDateTime validFrom;

    private LocalDateTime validUntil;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> conditions;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;
}