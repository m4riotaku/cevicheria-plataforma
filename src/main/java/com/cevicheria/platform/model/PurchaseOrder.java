package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.enums.PurchaseOrderStatus;
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
public class PurchaseOrder {

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
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    private String number;

    private LocalDate recordDate;

    private LocalDate expectedDeliveryDate;

    private String currencyCode = "PEN";

    @Enumerated(EnumType.STRING)
    private PurchaseOrderStatus status = PurchaseOrderStatus.BORRADOR;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "approved_by_user_business_id")
    private UserBusiness approvedByUserBusiness;

    private LocalDateTime approvedAt;

    private BigDecimal total = new BigDecimal("0.00");

    private String notes;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public PurchaseOrder(Business business, Branch branch, Supplier supplier, Warehouse warehouse, String number, LocalDate recordDate) {
        changeBusiness(business);
        changeBranch(branch);
        changeSupplier(supplier);
        changeWarehouse(warehouse);
        changeNumber(number);
        changeRecordDate(recordDate);
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

    private void changeSupplier(Supplier value) {
        if (value == null) {
            throw new InvalidEntityDataException("El proveedor es obligatorio");
        }
        this.supplier = value;
    }

    private void changeWarehouse(Warehouse value) {
        if (value == null) {
            throw new InvalidEntityDataException("El almacén es obligatorio");
        }
        this.warehouse = value;
    }

    public void changeNumber(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El número es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El número no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 40) {
            throw new InvalidEntityDataException("El número no puede superar 40 caracteres");
        }
        this.number = normalized;
    }

    private void changeRecordDate(LocalDate value) {
        if (value == null) {
            throw new InvalidEntityDataException("La fecha de registro es obligatoria");
        }
        this.recordDate = value;
    }

    public void changeExpectedDeliveryDate(LocalDate value) {
        this.expectedDeliveryDate = value;
    }

    public void changeCurrencyCode(String value) {
        if (value == null || value.isBlank()) {
            this.currencyCode = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() != 3) {
            throw new InvalidEntityDataException("El código de moneda debe contener exactamente 3 caracteres");
        }
        normalized = normalized.toUpperCase(java.util.Locale.ROOT);
        this.currencyCode = normalized;
    }

    public void changeStatus(PurchaseOrderStatus value) {
        this.status = value;
    }

    public void changeApprovedByUserBusiness(UserBusiness value) {
        this.approvedByUserBusiness = value;
    }

    public void changeApprovedAt(LocalDateTime value) {
        this.approvedAt = value;
    }

    public void changeTotal(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El total supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El total no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El total no puede ser negativo");
        }
        this.total = value;
    }

    public void changeNotes(String value) {
        if (value == null || value.isBlank()) {
            this.notes = null;
            return;
        }
        String normalized = value.trim();
        this.notes = normalized;
    }
}