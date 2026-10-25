package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    private String name;

    private String documentType;

    private String documentNumber;

    private String contactName;

    private String phone;

    private String email;

    private String address;

    private Integer creditDays = 0;

    private boolean active = true;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public Supplier(Business business, String name, String documentType, String documentNumber) {
        changeBusiness(business);
        changeName(name);
        changeDocumentType(documentType);
        changeDocumentNumber(documentNumber);
    }

    private void changeBusiness(Business value) {
        if (value == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        this.business = value;
    }

    public void changeName(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El nombre del proveedor es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El nombre del proveedor no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 180) {
            throw new InvalidEntityDataException("El nombre del proveedor no puede superar 180 caracteres");
        }
        this.name = normalized;
    }

    public void changeDocumentType(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El tipo de documento es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El tipo de documento no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 10) {
            throw new InvalidEntityDataException("El tipo de documento no puede superar 10 caracteres");
        }
        this.documentType = normalized;
    }

    public void changeDocumentNumber(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El número de documento es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El número de documento no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 20) {
            throw new InvalidEntityDataException("El número de documento no puede superar 20 caracteres");
        }
        this.documentNumber = normalized;
    }

    public void changeContactName(String value) {
        if (value == null || value.isBlank()) {
            this.contactName = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 120) {
            throw new InvalidEntityDataException("El contact nombre no puede superar 120 caracteres");
        }
        this.contactName = normalized;
    }

    public void changePhone(String value) {
        if (value == null || value.isBlank()) {
            this.phone = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 30) {
            throw new InvalidEntityDataException("El teléfono no puede superar 30 caracteres");
        }
        this.phone = normalized;
    }

    public void changeEmail(String value) {
        if (value == null || value.isBlank()) {
            this.email = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 150) {
            throw new InvalidEntityDataException("El correo electrónico no puede superar 150 caracteres");
        }
        if (!normalized.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new InvalidEntityDataException("El correo electrónico no tiene un formato válido");
        }
        this.email = normalized;
    }

    public void changeAddress(String value) {
        if (value == null || value.isBlank()) {
            this.address = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 250) {
            throw new InvalidEntityDataException("La dirección no puede superar 250 caracteres");
        }
        this.address = normalized;
    }

    public void changeCreditDays(Integer value) {
        if (value != null && value < 0) {
            throw new InvalidEntityDataException("El credit days no puede ser negativo");
        }
        if (value != null && value > 65535) {
            throw new InvalidEntityDataException("El credit days supera el máximo permitido");
        }
        this.creditDays = value;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }


    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
    private List<SupplierCategoryLink> categoryLinks = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
    private List<SupplierBrandLink> brandLinks = new ArrayList<>();

    public List<SupplierCategoryLink> getCategoryLinks() {
        return List.copyOf(this.categoryLinks);
    }

    public List<SupplierBrandLink> getBrandLinks() {
        return List.copyOf(this.brandLinks);
    }
}