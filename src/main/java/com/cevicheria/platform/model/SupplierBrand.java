package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import jakarta.persistence.Entity;
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
public class SupplierBrand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    public SupplierBrand(
            Business business,
            String name) {
        changeBusiness(business);
        changeName(name);
    }

    private void changeBusiness(Business business) {
        if (business == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        this.business = business;
    }

    public void changeName(String name) {
        if (name == null) {
            throw new InvalidEntityDataException("El nombre es obligatorio");
        }
        if (name.isBlank()) {
            throw new InvalidEntityDataException("El nombre no puede estar vacío");
        }
        String normalizedValue = name.trim();
        if (normalizedValue.length() > 120) {
            throw new InvalidEntityDataException("El nombre no puede superar 120 caracteres");
        }
        this.name = normalizedValue;
    }

}
