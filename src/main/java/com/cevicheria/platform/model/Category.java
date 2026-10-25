package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.enums.CategoryType;
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
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    private String name;

    private boolean active = true;

    @Enumerated(EnumType.STRING)
    private CategoryType type;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public Category(Business business, String name, CategoryType type) {
        changeBusiness(business);
        changeName(name);
        changeType(type);
    }

    private void changeBusiness(Business value) {
        if (value == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        this.business = value;
    }

    public void changeName(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El nombre de la categoría es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El nombre del categoría no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 120) {
            throw new InvalidEntityDataException("El nombre del categoría no puede superar 120 caracteres");
        }
        this.name = normalized;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void changeType(CategoryType value) {
        if (value == null) {
            throw new InvalidEntityDataException("El tipo es obligatorio");
        }
        this.type = value;
    }
}