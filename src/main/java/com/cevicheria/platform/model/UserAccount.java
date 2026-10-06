package com.cevicheria.platform.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserAccount
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    private String email;

    private @Getter(value = AccessLevel.NONE) String passwordHash;

    private boolean active;

    private LocalDateTime lastAccessAt;

    private Integer sessionVersion = 1;

    private byte[] passwordResetTokenHash;

    private LocalDateTime passwordResetTokenExpiresAt;

    private LocalDateTime createdAt;
}
