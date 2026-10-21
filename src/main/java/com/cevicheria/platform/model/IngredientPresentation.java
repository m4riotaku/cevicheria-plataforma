package com.cevicheria.platform.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.cevicheria.platform.model.enums.IngredientPresentationType;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Getter
@Setter
@Entity
@Table(name = "ingredient_presentation")
public class IngredientPresentation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    private String name;

    @Enumerated(EnumType.STRING)
    private IngredientPresentationType type;

    private BigDecimal baseConversionFactor;

    private BigDecimal baseTare = BigDecimal.ZERO;

    private boolean active = true;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;
}