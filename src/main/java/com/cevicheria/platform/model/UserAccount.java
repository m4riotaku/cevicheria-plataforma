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
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String passwordHash;

    private boolean active = false;

    private LocalDateTime lastAccessAt;

    private Integer sessionVersion = 1;

    private byte[] passwordResetTokenHash;

    private LocalDateTime passwordResetTokenExpiresAt;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public UserAccount(String email) {
        changeEmail(email);
    }

    public void changeEmail(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El correo electrónico es obligatoria");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El correo electrónico no puede estar vacío");
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

    public void changePasswordHash(String value) {
        if (value == null || value.isBlank()) {
            this.passwordHash = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 255) {
            throw new InvalidEntityDataException("El hash de contraseña no puede superar 255 caracteres");
        }
        this.passwordHash = normalized;
    }

    public void activate() {
        if (this.passwordHash == null || this.passwordHash.isBlank()) {
            throw new InvalidEntityDataException("La contraseña debe estar configurada antes de activar la cuenta");
        }
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void changeLastAccessAt(LocalDateTime value) {
        this.lastAccessAt = value;
    }

    public void changeSessionVersion(Integer value) {
        if (value != null && value < 0) {
            throw new InvalidEntityDataException("El session version no puede ser negativo");
        }
        this.sessionVersion = value;
    }

    public void changePasswordResetTokenHash(byte[] value) {
        if (value != null && value.length != 32) {
            throw new InvalidEntityDataException("El password reset token hash debe contener 32 bytes");
        }
        this.passwordResetTokenHash = value == null ? null : value.clone();
    }

    public void changePasswordResetTokenExpiresAt(LocalDateTime value) {
        this.passwordResetTokenExpiresAt = value;
    }
}