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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalesReceiptItem {

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
    @JoinColumn(name = "sales_receipt_id")
    private SalesReceipt salesReceipt;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "order_bill_item_id")
    private OrderBillItem orderBillItem;

    private String description;

    private String unit;

    private BigDecimal quantity;

    private BigDecimal unitValue = new BigDecimal("0.00");

    private BigDecimal subtotal = new BigDecimal("0.00");

    private BigDecimal discount = new BigDecimal("0.00");

    private BigDecimal tax = new BigDecimal("0.00");

    private BigDecimal total = new BigDecimal("0.00");

    private BigDecimal taxRate;

    private String taxTreatmentCode;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public SalesReceiptItem(Business business, Branch branch, SalesReceipt salesReceipt, String description, String unit, BigDecimal quantity) {
        changeBusiness(business);
        changeBranch(branch);
        changeSalesReceipt(salesReceipt);
        changeDescription(description);
        changeUnit(unit);
        changeQuantity(quantity);
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

    private void changeSalesReceipt(SalesReceipt value) {
        if (value == null) {
            throw new InvalidEntityDataException("El sales comprobante es obligatorio");
        }
        this.salesReceipt = value;
    }

    private void changeOrderBillItem(OrderBillItem value) {
        this.orderBillItem = value;
    }

    public void changeDescription(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("La descripción es obligatoria");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("La descripción no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 250) {
            throw new InvalidEntityDataException("La descripción no puede superar 250 caracteres");
        }
        this.description = normalized;
    }

    public void changeUnit(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El unit es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El unit no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 10) {
            throw new InvalidEntityDataException("El unit no puede superar 10 caracteres");
        }
        this.unit = normalized;
    }

    public void changeQuantity(BigDecimal value) {
        if (value == null) {
            throw new InvalidEntityDataException("La cantidad es obligatoria");
        }
        if (value != null && value.abs().compareTo(new BigDecimal("999999999999.999999")) > 0) {
            throw new InvalidEntityDataException("La cantidad supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 6) {
            throw new InvalidEntityDataException("La cantidad no admite más de 6 decimales");
        }
        if (value != null && value.signum() <= 0) {
            throw new InvalidEntityDataException("La cantidad debe ser mayor que cero");
        }
        this.quantity = value;
    }

    public void changeUnitValue(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El unit value supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El unit value no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El unit value no puede ser negativo");
        }
        this.unitValue = value;
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

    private void changeDiscount(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El discount supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El discount no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El discount no puede ser negativo");
        }
        this.discount = value;
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

    public void changeTaxRate(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("999.9999")) > 0) {
            throw new InvalidEntityDataException("El tributo tasa supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 4) {
            throw new InvalidEntityDataException("El tributo tasa no admite más de 4 decimales");
        }
        this.taxRate = value;
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

    public void changeAmounts(BigDecimal subtotal, BigDecimal discount, BigDecimal tax, BigDecimal total) {
        if (subtotal == null) {
            throw new InvalidEntityDataException("El importe subtotal es obligatorio");
        }
        if (discount == null) {
            throw new InvalidEntityDataException("El importe discount es obligatorio");
        }
        if (tax == null) {
            throw new InvalidEntityDataException("El importe tax es obligatorio");
        }
        if (total == null) {
            throw new InvalidEntityDataException("El importe total es obligatorio");
        }
        if (discount.compareTo(subtotal) > 0) {
            throw new InvalidEntityDataException("El descuento no puede superar el subtotal");
        }
        if (total.compareTo(subtotal.subtract(discount).add(tax)) != 0) {
            throw new InvalidEntityDataException("El total del detalle del comprobante no coincide con sus importes");
        }
        changeSubtotal(subtotal);
        changeDiscount(discount);
        changeTax(tax);
        changeTotal(total);
    }
}