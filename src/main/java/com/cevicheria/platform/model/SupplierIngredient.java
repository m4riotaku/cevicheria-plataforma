package com.cevicheria.platform.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    // TODO: FK real cuando turinovi publique Ingredient
    @Column(name = "ingredient_id", nullable = false)
    private Long ingredientId;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "is_preferred", nullable = false)
    private boolean preferred = false;
}
