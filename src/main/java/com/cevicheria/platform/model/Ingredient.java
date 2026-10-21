package com.cevicheria.platform.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.cevicheria.platform.model.enums.UnitOfMeasure;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Getter
@Setter
@Entity
@Table(name = "ingredient")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    private String name;

    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private String code;

    @Enumerated(EnumType.STRING)
    private UnitOfMeasure baseUnit;

    private boolean perishable = true;

    private BigDecimal minTemperature;

    private BigDecimal maxTemperature;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;
}