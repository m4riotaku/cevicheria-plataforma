package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.enums.SalesReceiptStates;
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
public class SalesReceipt {

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "business_tax_regime_id")
    private BusinessTaxRegimes businessTaxRegimes;
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
    @JoinColumn(name = "receipt_series_id")
    private ReceiptSeries receiptSeries;

    private Long number;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "order_bill_id")
    private OrderBill orderBill;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "original_receipt_id")
    private SalesReceipt originalReceipt;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private LocalDateTime recordDate;

    private String currencyCode = "PEN";

    private BigDecimal subtotal = new BigDecimal("0.00");

    private BigDecimal discount = new BigDecimal("0.00");

    private BigDecimal tax = new BigDecimal("0.00");

    private BigDecimal deliveryFee = new BigDecimal("0.00");

    private BigDecimal total = new BigDecimal("0.00");

    private String adjustmentReason;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "authorized_by_user_business_id")
    private UserBusiness authorizedByUserBusiness;

    @Enumerated(EnumType.STRING)
    private SalesReceiptStates status = SalesReceiptStates.BORRADOR;

    private String submissionTicket;

    private String responseCode;

    private String responseMessage;

    private String xmlUrl;

    private String cdrUrl;

    private String pdfUrl;

    private Integer submissionAttempts = 0;

    private LocalDateTime lastSubmissionAt;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public SalesReceipt(Business business, Branch branch, ReceiptSeries receiptSeries, Long number, LocalDateTime recordDate) {
        changeBusiness(business);
        changeBranch(branch);
        changeReceiptSeries(receiptSeries);
        changeNumber(number);
        changeRecordDate(recordDate);
    }

    public void changeBusinessTaxRegimes(BusinessTaxRegimes value) {
        this.businessTaxRegimes = value;
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

    private void changeReceiptSeries(ReceiptSeries value) {
        if (value == null) {
            throw new InvalidEntityDataException("El comprobante series es obligatorio");
        }
        this.receiptSeries = value;
    }

    public void changeNumber(Long value) {
        if (value == null) {
            throw new InvalidEntityDataException("El número es obligatorio");
        }
        if (value != null && value < 0) {
            throw new InvalidEntityDataException("El número no puede ser negativo");
        }
        if (value != null && value <= 0) {
            throw new InvalidEntityDataException("El número debe ser mayor que cero");
        }
        this.number = value;
    }

    public void changeOrderBill(OrderBill value) {
        this.orderBill = value;
    }

    public void changeOriginalReceipt(SalesReceipt value) {
        this.originalReceipt = value;
    }

    private void changeCustomer(Customer value) {
        this.customer = value;
    }

    private void changeRecordDate(LocalDateTime value) {
        if (value == null) {
            throw new InvalidEntityDataException("La fecha de registro es obligatoria");
        }
        this.recordDate = value;
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

    private void changeDeliveryFee(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El delivery fee supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El delivery fee no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El delivery fee no puede ser negativo");
        }
        this.deliveryFee = value;
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

    public void changeAdjustmentReason(String value) {
        if (value == null || value.isBlank()) {
            this.adjustmentReason = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 250) {
            throw new InvalidEntityDataException("El adjustment reason no puede superar 250 caracteres");
        }
        this.adjustmentReason = normalized;
    }

    public void changeAuthorizedByUserBusiness(UserBusiness value) {
        this.authorizedByUserBusiness = value;
    }

    public void changeStatus(SalesReceiptStates value) {
        this.status = value;
    }

    public void changeSubmissionTicket(String value) {
        if (value == null || value.isBlank()) {
            this.submissionTicket = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 150) {
            throw new InvalidEntityDataException("El submission ticket no puede superar 150 caracteres");
        }
        this.submissionTicket = normalized;
    }

    public void changeResponseCode(String value) {
        if (value == null || value.isBlank()) {
            this.responseCode = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 30) {
            throw new InvalidEntityDataException("El response código no puede superar 30 caracteres");
        }
        this.responseCode = normalized;
    }

    public void changeResponseMessage(String value) {
        if (value == null || value.isBlank()) {
            this.responseMessage = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 500) {
            throw new InvalidEntityDataException("El response message no puede superar 500 caracteres");
        }
        this.responseMessage = normalized;
    }

    public void changeXmlUrl(String value) {
        if (value == null || value.isBlank()) {
            this.xmlUrl = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 1000) {
            throw new InvalidEntityDataException("El xml url no puede superar 1000 caracteres");
        }
        this.xmlUrl = normalized;
    }

    public void changeCdrUrl(String value) {
        if (value == null || value.isBlank()) {
            this.cdrUrl = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 1000) {
            throw new InvalidEntityDataException("El cdr url no puede superar 1000 caracteres");
        }
        this.cdrUrl = normalized;
    }

    public void changePdfUrl(String value) {
        if (value == null || value.isBlank()) {
            this.pdfUrl = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 1000) {
            throw new InvalidEntityDataException("El pdf url no puede superar 1000 caracteres");
        }
        this.pdfUrl = normalized;
    }

    public void changeSubmissionAttempts(Integer value) {
        if (value != null && value < 0) {
            throw new InvalidEntityDataException("El submission attempts no puede ser negativo");
        }
        if (value != null && value > 65535) {
            throw new InvalidEntityDataException("El submission attempts supera el máximo permitido");
        }
        this.submissionAttempts = value;
    }

    public void changeLastSubmissionAt(LocalDateTime value) {
        this.lastSubmissionAt = value;
    }

    public void changeAmounts(BigDecimal subtotal, BigDecimal discount, BigDecimal tax, BigDecimal deliveryFee, BigDecimal total) {
        if (subtotal == null) {
            throw new InvalidEntityDataException("El importe subtotal es obligatorio");
        }
        if (discount == null) {
            throw new InvalidEntityDataException("El importe discount es obligatorio");
        }
        if (tax == null) {
            throw new InvalidEntityDataException("El importe tax es obligatorio");
        }
        if (deliveryFee == null) {
            throw new InvalidEntityDataException("El importe deliveryFee es obligatorio");
        }
        if (total == null) {
            throw new InvalidEntityDataException("El importe total es obligatorio");
        }
        if (discount.compareTo(subtotal) > 0) {
            throw new InvalidEntityDataException("El descuento no puede superar el subtotal");
        }
        if (total.compareTo(subtotal.subtract(discount).add(tax).add(deliveryFee)) != 0) {
            throw new InvalidEntityDataException("El comprobante presenta importes inconsistentes");
        }
        changeSubtotal(subtotal);
        changeDiscount(discount);
        changeTax(tax);
        changeDeliveryFee(deliveryFee);
        changeTotal(total);
    }


    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "salesReceipt", fetch = FetchType.LAZY)
    private List<SalesReceiptIssuerSnapshot> issuerSnapshots = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "salesReceipt", fetch = FetchType.LAZY)
    private List<SalesReceiptCustomerSnapshot> customerSnapshots = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "salesReceipt", fetch = FetchType.LAZY)
    private List<SalesReceiptIntegrationAttempt> integrationAttempts = new ArrayList<>();

    public List<SalesReceiptIssuerSnapshot> getIssuerSnapshots() {
        return List.copyOf(this.issuerSnapshots);
    }

    public List<SalesReceiptCustomerSnapshot> getCustomerSnapshots() {
        return List.copyOf(this.customerSnapshots);
    }

    public List<SalesReceiptIntegrationAttempt> getIntegrationAttempts() {
        return List.copyOf(this.integrationAttempts);
    }
}