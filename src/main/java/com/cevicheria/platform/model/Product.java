package com.cevicheria.platform.model;

import com.cevicheria.platform.model.enums.ProductType;
import com.cevicheria.platform.model.enums.UnitOfMeasure;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "direct_ingredient_id")
    private Ingredient directIngredient;

    private String code;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Enumerated(EnumType.STRING)
    private UnitOfMeasure saleUnit = UnitOfMeasure.UNIT;

    private String imageUrl;

    private String taxCode;

    private String description;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;
}