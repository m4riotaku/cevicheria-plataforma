package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SecurityPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String description;

    public SecurityPermission(
            String code,
            String description) {
        changeCode(code);
        changeDescription(description);
    }

    private void changeCode(String code) {
        if (code == null) {
            throw new InvalidEntityDataException("El código es obligatorio");
        }
        if (code.isBlank()) {
            throw new InvalidEntityDataException("El código no puede estar vacío");
        }
        String normalizedValue = code.trim();
        if (normalizedValue.length() > 100) {
            throw new InvalidEntityDataException("El código no puede superar 100 caracteres");
        }
        this.code = normalizedValue;
    }

    public void changeDescription(String description) {
        if (description == null || description.isBlank()) {
            this.description = null;
            return;
        }
        String normalizedValue = description.trim();
        if (normalizedValue.length() > 250) {
            throw new InvalidEntityDataException("La descripción no puede superar 250 caracteres");
        }
        this.description = normalizedValue;
    }

}
