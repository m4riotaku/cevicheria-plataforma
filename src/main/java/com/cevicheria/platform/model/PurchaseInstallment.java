package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PurchaseInstallment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    private Integer number;

    private LocalDate dueOn;

    private BigDecimal amount = new BigDecimal("0.00");

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public PurchaseInstallment(Business business, Branch branch, Purchase purchase, Integer number, LocalDate dueOn) {
        changeBusiness(business);
        changeBranch(branch);
        changePurchase(purchase);
        changeNumber(number);
        changeDueOn(dueOn);
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

    private void changePurchase(Purchase value) {
        if (value == null) {
            throw new InvalidEntityDataException("El purchase es obligatorio");
        }
        this.purchase = value;
    }

    public void changeNumber(Integer value) {
        if (value == null) {
            throw new InvalidEntityDataException("El número es obligatorio");
        }
        if (value != null && value < 0) {
            throw new InvalidEntityDataException("El número no puede ser negativo");
        }
        if (value != null && value > 65535) {
            throw new InvalidEntityDataException("El número supera el máximo permitido");
        }
        this.number = value;
    }

    public void changeDueOn(LocalDate value) {
        if (value == null) {
            throw new InvalidEntityDataException("El due on es obligatorio");
        }
        this.dueOn = value;
    }

    public void changeAmount(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El importe supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El importe no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El importe no puede ser negativo");
        }
        if (value != null && value.signum() <= 0) {
            throw new InvalidEntityDataException("El importe debe ser mayor que cero");
        }
        this.amount = value;
    }
}