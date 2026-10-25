package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.enums.ProductType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    private String name;

    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "direct_ingredient_id")
    private Ingredient directIngredient;

    private String code;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    private String saleUnit = "UND";

    private String imageUrl;

    private String taxCode;

    private String description;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public Product(Business business, String name, String code, ProductType type) {
        this(business, name, code, type, null);
    }

    public Product(Business business, String name, String code, ProductType type, Ingredient directIngredient) {
        changeBusiness(business);
        changeName(name);
        changeCode(code);
        changeType(type);
        changeDirectIngredient(directIngredient);
    }

    private void changeBusiness(Business value) {
        if (value == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        this.business = value;
    }

    public void changeName(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El nombre del producto es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El nombre del producto no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 120) {
            throw new InvalidEntityDataException("El nombre del producto no puede superar 120 caracteres");
        }
        this.name = normalized;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void changeCategory(Category value) {
        this.category = value;
    }

    public void changeDirectIngredient(Ingredient value) {
        if (value == null && this.type == ProductType.DIRECTO) {
            throw new InvalidEntityDataException("Los productos directos requieren un insumo asociado");
        }
        this.directIngredient = value;
    }

    public void changeCode(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El código es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El código no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 40) {
            throw new InvalidEntityDataException("El código no puede superar 40 caracteres");
        }
        this.code = normalized;
    }

    private void changeType(ProductType value) {
        if (value == null) {
            throw new InvalidEntityDataException("El tipo es obligatorio");
        }
        this.type = value;
    }

    public void changeSaleUnit(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El unidad de venta es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El unidad de venta no puede estar vacío");
        }
        String normalized = value.trim().toUpperCase(java.util.Locale.ROOT);
        if (!(normalized.equals("UND") || normalized.equals("KG") || normalized.equals("L"))) {
            throw new InvalidEntityDataException("El unidad de venta no coincide con los valores permitidos por la base de datos");
        }
        this.saleUnit = normalized;
    }

    public void changeImageUrl(String value) {
        if (value == null || value.isBlank()) {
            this.imageUrl = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 1000) {
            throw new InvalidEntityDataException("El URL de imagen no puede superar 1000 caracteres");
        }
        this.imageUrl = normalized;
    }

    public void changeTaxCode(String value) {
        if (value == null || value.isBlank()) {
            this.taxCode = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 20) {
            throw new InvalidEntityDataException("El tributo código no puede superar 20 caracteres");
        }
        this.taxCode = normalized;
    }

    public void changeDescription(String value) {
        if (value == null || value.isBlank()) {
            this.description = null;
            return;
        }
        String normalized = value.trim();
        this.description = normalized;
    }
}