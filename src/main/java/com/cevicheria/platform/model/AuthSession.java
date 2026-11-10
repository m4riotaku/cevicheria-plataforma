package com.cevicheria.platform.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.Arrays;

/** La sesión persiste solamente el verificador del refresh token, nunca el JWT. */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "auth_session")
public class AuthSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false, unique = true, length = 36)
    private String sessionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_account_id", nullable = false)
    private UserAccount userAccount;

    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "refresh_token_hash", nullable = false, columnDefinition = "BINARY(32)")
    private byte[] refreshTokenHash;

    @Column(name = "session_version", nullable = false)
    private Integer sessionVersion;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;

    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;

    public AuthSession(String sessionId, UserAccount user, byte[] hash,
                       LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.sessionId = sessionId;
        this.userAccount = user;
        this.refreshTokenHash = Arrays.copyOf(hash, hash.length);
        this.sessionVersion = user.getSessionVersion();
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public boolean isUsable(LocalDateTime now) {
        return revokedAt == null && expiresAt.isAfter(now);
    }

    public void rotate(byte[] newHash, LocalDateTime now) {
        this.refreshTokenHash = Arrays.copyOf(newHash, newHash.length);
        this.lastUsedAt = now;
    }

    public void revoke(LocalDateTime now) {
        if (revokedAt == null) this.revokedAt = now;
    }
}
