package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.enums.DiningTableStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class DiningTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    private String number;

    private String zone;

    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private DiningTableStatus status = DiningTableStatus.DISPONIBLE;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public DiningTable(Business business, Branch branch, String number, Integer capacity) {
        changeBusiness(business);
        changeBranch(branch);
        changeNumber(number);
        changeCapacity(capacity);
    }

    private void changeBusiness(Business value) {
        if (value == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        this.business = value;
    }

    private void changeBranch(Branch value) {
        if (value == null) {
            throw new InvalidEntityDataException("El local es obligatorio");
        }
        this.branch = value;
    }

    public void changeNumber(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El número es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El número no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 20) {
            throw new InvalidEntityDataException("El número no puede superar 20 caracteres");
        }
        this.number = normalized;
    }

    public void changeZone(String value) {
        if (value == null || value.isBlank()) {
            this.zone = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 80) {
            throw new InvalidEntityDataException("El zone no puede superar 80 caracteres");
        }
        this.zone = normalized;
    }

    public void changeCapacity(Integer value) {
        if (value == null) {
            throw new InvalidEntityDataException("El capacity es obligatorio");
        }
        if (value != null && value < 0) {
            throw new InvalidEntityDataException("El capacity no puede ser negativo");
        }
        if (value != null && value > 65535) {
            throw new InvalidEntityDataException("El capacity supera el máximo permitido");
        }
        if (value != null && value <= 0) {
            throw new InvalidEntityDataException("El capacity debe ser mayor que cero");
        }
        this.capacity = value;
    }

    public void changeStatus(DiningTableStatus value) {
        this.status = value;
    }
}