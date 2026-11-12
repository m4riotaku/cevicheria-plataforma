package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import jakarta.persistence.Entity;
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
public class ReceiptSeries {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    private String type;

    private String series;

    private Long lastNumber = 0L;

    private boolean active = true;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public ReceiptSeries(Business business, Branch branch, String type, String series) {
        changeBusiness(business);
        changeBranch(branch);
        changeType(type);
        changeSeries(series);
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

    public void changeType(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El tipo es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El tipo no puede estar vacío");
        }
        String normalized = value.trim().toUpperCase(java.util.Locale.ROOT);
        if (!(normalized.equals("BOLETA") || normalized.equals("TICKET") || normalized.equals("FACTURA") || normalized.equals("NOTA_CREDITO") || normalized.equals("NOTA_DEBITO"))) {
            throw new InvalidEntityDataException("El tipo no coincide con los valores permitidos por la base de datos");
        }
        this.type = normalized;
    }

    public void changeSeries(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El series es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El series no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 10) {
            throw new InvalidEntityDataException("El series no puede superar 10 caracteres");
        }
        this.series = normalized;
    }

    public void changeLastNumber(Long value) {
        if (value != null && value < 0) {
            throw new InvalidEntityDataException("El last número no puede ser negativo");
        }
        this.lastNumber = value;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }
}