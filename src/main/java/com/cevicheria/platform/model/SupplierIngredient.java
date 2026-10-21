package com.cevicheria.platform.model;


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
@Table(name = "supplier_ingredient", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"supplier_id", "ingredient_id"})
})
public class SupplierIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "presentation_id")
    private IngredientPresentation presentation;

    private String brand;

    private BigDecimal referencePrice = BigDecimal.ZERO;

    private boolean preferred = false;

    private boolean active = true;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;
}
