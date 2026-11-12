package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private boolean active = true;

    private String legalName;

    private String ruc;

    private String currencyCode = "PEN";

    private String businessFormat;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public Business(String name, String legalName, String ruc, String businessFormat) {
        changeName(name);
        changeLegalName(legalName);
        changeRuc(ruc);
        changeBusinessFormat(businessFormat);
    }

    public void changeName(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El nombre del negocio es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El nombre del negocio no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 120) {
            throw new InvalidEntityDataException("El nombre del negocio no puede superar 120 caracteres");
        }
        this.name = normalized;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void changeLegalName(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("La razón social es obligatoria");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("La razón social no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 200) {
            throw new InvalidEntityDataException("La razón social no puede superar 200 caracteres");
        }
        this.legalName = normalized;
    }

    public void changeRuc(String value) {
        if (value == null || value.isBlank()) {
            this.ruc = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() != 11) {
            throw new InvalidEntityDataException("El RUC debe contener exactamente 11 caracteres");
        }
        if (!normalized.matches("[0-9]+")) {
            throw new InvalidEntityDataException("El RUC solo admite dígitos numéricos");
        }
        this.ruc = normalized;
    }

    public void changeCurrencyCode(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("La moneda del negocio es obligatoria");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El código de moneda no puede estar vacío");
        }
        String normalized = value.trim().toUpperCase(java.util.Locale.ROOT);
        if (normalized.length() != 3) {
            throw new InvalidEntityDataException("El código de moneda debe tener exactamente 3 caracteres");
        }
        if (!normalized.matches("[A-Z]{3}")) {
            throw new InvalidEntityDataException("El código de moneda solo puede contener letras");
        }
        this.currencyCode = normalized;
    }

    public void changeBusinessFormat(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El formato del negocio es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El formato del negocio no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 80) {
            throw new InvalidEntityDataException("El formato del negocio no puede superar 80 caracteres");
        }
        this.businessFormat = normalized;
    }
}