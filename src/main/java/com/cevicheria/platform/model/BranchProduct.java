package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.enums.SalesChannel;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BranchProduct {

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
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "preparation_area_id")
    private PreparationArea preparationArea;

    @Enumerated(EnumType.STRING)
    private SalesChannel salesChannel;

    private BigDecimal price = new BigDecimal("0.00");

    private String taxTreatmentCode;

    private boolean visible = true;

    private boolean blocked = false;

    private String blockReason;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public BranchProduct(Business business, Branch branch, Product product, SalesChannel salesChannel) {
        changeBusiness(business);
        changeBranch(branch);
        changeProduct(product);
        changeSalesChannel(salesChannel);
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

    private void changeProduct(Product value) {
        if (value == null) {
            throw new InvalidEntityDataException("El producto es obligatorio");
        }
        this.product = value;
    }

    public void changePreparationArea(PreparationArea value) {
        this.preparationArea = value;
    }

    private void changeSalesChannel(SalesChannel value) {
        if (value == null) {
            throw new InvalidEntityDataException("El canal de venta es obligatorio");
        }
        this.salesChannel = value;
    }

    public void changePrice(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El precio supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El precio no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El precio no puede ser negativo");
        }
        this.price = value;
    }

    public void changeTaxTreatmentCode(String value) {
        if (value == null || value.isBlank()) {
            this.taxTreatmentCode = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 5) {
            throw new InvalidEntityDataException("El tributo treatment código no puede superar 5 caracteres");
        }
        this.taxTreatmentCode = normalized;
    }

    public void enableVisible() {
        this.visible = true;
    }

    public void disableVisible() {
        this.visible = false;
    }

    private void changeBlockReason(String value) {
        if (value == null || value.isBlank()) {
            this.blockReason = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 250) {
            throw new InvalidEntityDataException("El block reason no puede superar 250 caracteres");
        }
        this.blockReason = normalized;
    }

    public void block(String reason) {
        if (reason == null || reason.isBlank()) {
            throw new InvalidEntityDataException("El motivo del bloqueo es obligatorio");
        }
        if (reason.trim().length() > 250) {
            throw new InvalidEntityDataException("El motivo del bloqueo no puede superar 250 caracteres");
        }
        this.blockReason = reason.trim();
        this.blocked = true;
    }

    public void unblock() {
        this.blocked = false;
        this.blockReason = null;
    }
}