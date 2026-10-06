package com.cevicheria.platform.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ingredient_presentation")
public class IngredientPresentation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long ingredientId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 30)
    private String purchaseUnit;

    @Column(nullable = false, precision = 14, scale = 4)
    private BigDecimal conversionFactor;

    @Column(nullable = false)
    private boolean defaultPresentation;

    @Column(nullable = false)
    private boolean active = true;
}