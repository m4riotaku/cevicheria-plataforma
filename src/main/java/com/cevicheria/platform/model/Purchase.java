package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.enums.PurchaseStatus;
import com.cevicheria.platform.model.enums.PurchaseType;
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
public class Purchase {

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

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "source_purchase_id")
    private Purchase sourcePurchase;

    @Enumerated(EnumType.STRING)
    private PurchaseType type;

    private String series;

    private String number;

    private LocalDate recordDate;

    private LocalDate expirationDate;

    private String currencyCode = "PEN";

    private BigDecimal subtotal = new BigDecimal("0.00");

    private BigDecimal tax = new BigDecimal("0.00");

    private BigDecimal total = new BigDecimal("0.00");

    @Enumerated(EnumType.STRING)
    private PurchaseStatus status = PurchaseStatus.BORRADOR;

    private String notes;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public Purchase(Business business, Branch branch, Supplier supplier, PurchaseType type,
                    String series, String number, LocalDate recordDate) {
        this(business, branch, supplier, type, series, number, recordDate, null);
    }

    public Purchase(Business business, Branch branch, Supplier supplier, PurchaseType type,
                    String series, String number, LocalDate recordDate, Purchase sourcePurchase) {
        changeBusiness(business);
        changeBranch(branch);
        changeSupplier(supplier);
        changeType(type);
        changeSeries(series);
        changeNumber(number);
        changeRecordDate(recordDate);
        changeSourcePurchase(sourcePurchase);
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

    public void changePurchaseOrder(PurchaseOrder value) {
        this.purchaseOrder = value;
    }

    public void changeSourcePurchase(Purchase value) {
        if (value == null && (this.type == PurchaseType.NOTA_CREDITO || this.type == PurchaseType.NOTA_DEBITO)) {
            throw new InvalidEntityDataException("Las notas de crédito y débito requieren una compra de origen");
        }
        this.sourcePurchase = value;
    }

    private void changeType(PurchaseType value) {
        if (value == null) {
            throw new InvalidEntityDataException("El tipo es obligatorio");
        }
        this.type = value;
    }

    public void changeSeries(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El series es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El series no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 20) {
            throw new InvalidEntityDataException("El series no puede superar 20 caracteres");
        }
        this.series = normalized;
    }

    public void changeNumber(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El número es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El número no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 30) {
            throw new InvalidEntityDataException("El número no puede superar 30 caracteres");
        }
        this.number = normalized;
    }

    private void changeRecordDate(LocalDate value) {
        if (value == null) {
            throw new InvalidEntityDataException("La fecha de registro es obligatoria");
        }
        this.recordDate = value;
    }

    public void changeExpirationDate(LocalDate value) {
        this.expirationDate = value;
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

    private void changeSubtotal(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El subtotal supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El subtotal no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El subtotal no puede ser negativo");
        }
        this.subtotal = value;
    }

    private void changeTax(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El impuesto supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El impuesto no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El impuesto no puede ser negativo");
        }
        this.tax = value;
    }

    private void changeTotal(BigDecimal value) {
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

    public void changeStatus(PurchaseStatus value) {
        this.status = value;
    }

    public void changeNotes(String value) {
        if (value == null || value.isBlank()) {
            this.notes = null;
            return;
        }
        String normalized = value.trim();
        this.notes = normalized;
    }

    public void changeAmounts(BigDecimal subtotal, BigDecimal tax, BigDecimal total) {
        if (subtotal == null) {
            throw new InvalidEntityDataException("El importe subtotal es obligatorio");
        }
        if (tax == null) {
            throw new InvalidEntityDataException("El importe tax es obligatorio");
        }
        if (total == null) {
            throw new InvalidEntityDataException("El importe total es obligatorio");
        }
        if (total.compareTo(subtotal.add(tax)) != 0) {
            throw new InvalidEntityDataException("El total de la compra debe ser igual al subtotal más impuestos");
        }
        changeSubtotal(subtotal);
        changeTax(tax);
        changeTotal(total);
    }
}