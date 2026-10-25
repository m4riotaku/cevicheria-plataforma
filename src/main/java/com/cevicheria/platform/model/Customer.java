package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    private String name;

    private String documentType;

    private String documentNumber;

    private String phone;

    private String email;

    private LocalDate birthDate;

    private boolean active = true;

    private String preferences;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<CustomerAddress> addresses = new ArrayList<>();

    public Customer(Business business, String name) {
        changeBusiness(business);
        changeName(name);
    }

    private void changeBusiness(Business value) {
        if (value == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        this.business = value;
    }

    public void changeName(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El nombre del cliente es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El nombre del cliente no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 180) {
            throw new InvalidEntityDataException("El nombre del cliente no puede superar 180 caracteres");
        }
        this.name = normalized;
    }

    private void changeDocumentType(String value) {
        if (value == null || value.isBlank()) {
            this.documentType = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 10) {
            throw new InvalidEntityDataException("El tipo de documento no puede superar 10 caracteres");
        }
        this.documentType = normalized;
    }

    private void changeDocumentNumber(String value) {
        if (value == null || value.isBlank()) {
            this.documentNumber = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 20) {
            throw new InvalidEntityDataException("El número de documento no puede superar 20 caracteres");
        }
        this.documentNumber = normalized;
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

    public void changeBirthDate(LocalDate value) {
        if (value != null && value.isAfter(LocalDate.now())) {
            throw new InvalidEntityDataException("La fecha de nacimiento no puede ser futura");
        }
        this.birthDate = value;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void changePreferences(String value) {
        if (value == null || value.isBlank()) {
            this.preferences = null;
            return;
        }
        String normalized = value.trim();
        this.preferences = normalized;
    }

    public void changeDocument(String documentType, String documentNumber) {
        if (documentType == null && documentNumber == null) {
            this.documentType = null;
            this.documentNumber = null;
            return;
        }
        if (documentType == null || documentType.isBlank()) {
            throw new InvalidEntityDataException("El tipo de documento es obligatorio cuando se registra su número");
        }
        if (documentNumber == null || documentNumber.isBlank()) {
            throw new InvalidEntityDataException("El número de documento es obligatorio cuando se registra su tipo");
        }
        if (documentType.trim().length() > 10) {
            throw new InvalidEntityDataException("El tipo de documento no puede superar 10 caracteres");
        }
        if (documentNumber.trim().length() > 20) {
            throw new InvalidEntityDataException("El número de documento no puede superar 20 caracteres");
        }
        changeDocumentType(documentType);
        changeDocumentNumber(documentNumber);
    }

    public List<CustomerAddress> getAddresses() {
        return List.copyOf(this.addresses);
    }
}