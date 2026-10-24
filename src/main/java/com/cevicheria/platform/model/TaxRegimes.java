package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.enums.TaxRegimesCode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TaxRegimes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TaxRegimesCode code;

    private String name;

    private String description;

    private boolean isActive = true;

    public TaxRegimes(String code, String name, String description) {
        changeCode(code);
        changeName(name);
        changeDescription(description);
    }

    private void changeCode(String code) {
        if (code == null || code.isBlank()) {
            throw new InvalidEntityDataException("El código del régimen tributario es obligatorio");
        }

        String normalizedCode = code.trim().toUpperCase(Locale.ROOT);

        if (!TaxRegimesCode.isValid(normalizedCode)) {
            throw new InvalidEntityDataException("El código del régimen tributario no es válido");
        }

        this.code = TaxRegimesCode.valueOf(normalizedCode);
    }

    private void changeName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidEntityDataException("El nombre del régimen tributario es obligatorio");
        }

        String normalizedName = name.trim();

        if (normalizedName.length() > 120) {
            throw new InvalidEntityDataException("El nombre del régimen tributario no puede superar los 120 caracteres");
        }

        this.name = normalizedName;
    }

    private void changeDescription(String description) {
        if (description == null || description.isBlank()) {
            this.description = null;
            return;
        }

        String normalizedDescription = description.trim();

        if (normalizedDescription.length() > 500) {
            throw new InvalidEntityDataException("La descripción no puede superar los 500 caracteres");
        }

        this.description = normalizedDescription;
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }
}
