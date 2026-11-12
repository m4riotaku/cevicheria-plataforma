package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.enums.PlatformRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.security.MessageDigest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAccount
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private String email;

    private String passwordHash;

    private boolean active = true;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private PlatformRole platformRole = PlatformRole.USER;

    private LocalDateTime lastAccessAt;

    private Integer sessionVersion = 1;

    @Getter(AccessLevel.NONE)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "access_token_hash", columnDefinition = "BINARY(32)")
    private byte[] accessTokenHash;

    private byte[] passwordResetTokenHash;

    private LocalDateTime passwordResetTokenExpiresAt;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public UserAccount(String userName, String email, String passwordHash)
    {
        changeUserName(userName);
        changeEmail(email);
        changePasswordHash(passwordHash);
    }

    public void changeUserName(String newUserName)
    {
        if(newUserName == null || newUserName.isBlank())
        {
            throw new InvalidEntityDataException("El nombre de usuario es obligatorio");
        }

        if(newUserName.trim().length() > 30)
        {
            throw new InvalidEntityDataException("El nombre de usuario no puede exceder de los 30 caracteres");
        }

        userName = newUserName;
    }

    public void changeEmail(String value)
    {
        if (value == null)
        {
            throw new InvalidEntityDataException("El correo electrónico es obligatoria");
        }

        if (value.isBlank())
        {
            throw new InvalidEntityDataException("El correo electrónico no puede estar vacío");
        }

        String normalized = value.trim();

        if (normalized.length() > 150)
        {
            throw new InvalidEntityDataException("El correo electrónico no puede superar 150 caracteres");
        }

        if (!normalized.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))
        {
            throw new InvalidEntityDataException("El correo electrónico no tiene un formato válido");
        }

        this.email = normalized;
    }

    public void changePasswordHash(String value)
    {
        if (value == null || value.isBlank())
        {
            this.passwordHash = null;
            return;
        }

        String normalized = value.trim();
        if (normalized.length() > 255)
        {
            throw new InvalidEntityDataException("El hash de contraseña no puede superar 255 caracteres");
        }

        this.passwordHash = normalized;
    }

    public void activate()
    {
        if (this.passwordHash == null || this.passwordHash.isBlank())
        {
            throw new InvalidEntityDataException("La contraseña debe estar configurada antes de activar la cuenta");
        }

        this.active = true;
    }

    public void deactivate()
    {
        this.active = false;
    }

    public void changePlatformRole(PlatformRole role)
    {
        if (role == null)
        {
            throw new InvalidEntityDataException("El rol de plataforma es obligatorio");
        }

        this.platformRole = role;
    }

    public void changeLastAccessAt(LocalDateTime value)
    {
        this.lastAccessAt = value;
    }

    public void changeSessionVersion(Integer value)
    {
        if (value != null && value < 0)
        {
            throw new InvalidEntityDataException("El session version no puede ser negativo");
        }

        this.sessionVersion = value;
    }

    public void saveAccessTokenHash(byte[] hash)
    {
        if (hash == null || hash.length != 32)
        {
            throw new InvalidEntityDataException("El hash del JWT debe contener 32 bytes");
        }
        this.accessTokenHash = hash.clone();
    }

    public boolean hasAccessTokenHash(byte[] hash)
    {
        return this.accessTokenHash != null && hash != null
                && MessageDigest.isEqual(this.accessTokenHash, hash);
    }

    public void invalidateSessions()
    {
        this.accessTokenHash = null;
        if (this.sessionVersion == null || this.sessionVersion <= 0)
        {
            this.sessionVersion = 1;
        }
        else if (this.sessionVersion == Integer.MAX_VALUE)
        {
            throw new InvalidEntityDataException("Se agotó la numeración de sesiones");
        }
        else
        {
            this.sessionVersion++;
        }
    }

    public void changePasswordResetTokenHash(byte[] value)
    {
        if (value != null && value.length != 32)
        {
            throw new InvalidEntityDataException("El newPassword reset token hash debe contener 32 bytes");
        }

        this.passwordResetTokenHash = value == null ? null : value.clone();
    }

    public void changePasswordResetTokenExpiresAt(LocalDateTime value)
    {
        this.passwordResetTokenExpiresAt = value;
    }
}