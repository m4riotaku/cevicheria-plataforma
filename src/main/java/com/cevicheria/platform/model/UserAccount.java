package com.cevicheria.platform.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserAccount
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    private String email;

    private @Getter(value = AccessLevel.NONE) String passwordHash;

    private boolean active = false;

    private LocalDateTime lastAccessAt;

    private Integer sessionVersion = 1;

    private byte[] passwordResetTokenHash;

    private LocalDateTime passwordResetTokenExpiresAt;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
