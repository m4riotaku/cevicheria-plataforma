package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.enums.PaymentConcept;
import com.cevicheria.platform.model.enums.PaymentDirection;
import com.cevicheria.platform.model.enums.PaymentMethod;
import com.cevicheria.platform.model.enums.PaymentStates;
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
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    private String operationKey;

    @Enumerated(EnumType.STRING)
    private PaymentDirection direction;

    @Enumerated(EnumType.STRING)
    private PaymentConcept concept;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "financial_account_id")
    private FinancialAccount financialAccount;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "cash_shift_id")
    private CashShift cashShift;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "source_payment_id")
    private Payment sourcePayment;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime recordDate;

    private String currencyCode = "PEN";

    private BigDecimal amount = new BigDecimal("0.00");

    private BigDecimal tip = new BigDecimal("0.00");

    private BigDecimal amountReceived;

    private BigDecimal changeAmount = new BigDecimal("0");

    private String externalReference;

    private String evidenceUrl;

    @Enumerated(EnumType.STRING)
    private PaymentStates status = PaymentStates.PENDIENTE;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "verified_by_user_business_id")
    private UserBusiness verifiedByUserBusiness;

    private LocalDateTime verifiedAt;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public Payment(Business business, Branch branch, String operationKey, PaymentDirection direction, PaymentConcept concept, PaymentMethod paymentMethod, FinancialAccount financialAccount, LocalDateTime recordDate, BigDecimal amount, BigDecimal tip, BigDecimal amountReceived, BigDecimal changeAmount) {
        changeBusiness(business);
        changeBranch(branch);
        changeOperationKey(operationKey);
        changeDirection(direction);
        changeConcept(concept);
        changePaymentMethod(paymentMethod);
        changeFinancialAccount(financialAccount);
        changeRecordDate(recordDate);
        changePaymentAmounts(amount, tip, amountReceived, changeAmount);
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

    public void changeOperationKey(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El identificador de operación es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El identificador de operación no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 64) {
            throw new InvalidEntityDataException("El identificador de operación no puede superar 64 caracteres");
        }
        this.operationKey = normalized;
    }

    public void changeDirection(PaymentDirection value) {
        if (value == null) {
            throw new InvalidEntityDataException("El direction es obligatorio");
        }
        this.direction = value;
    }

    public void changeConcept(PaymentConcept value) {
        if (value == null) {
            throw new InvalidEntityDataException("El concept es obligatorio");
        }
        this.concept = value;
    }

    public void changePaymentMethod(PaymentMethod value) {
        if (value == null) {
            throw new InvalidEntityDataException("El método de pago es obligatorio");
        }
        this.paymentMethod = value;
    }

    private void changeFinancialAccount(FinancialAccount value) {
        if (value == null) {
            throw new InvalidEntityDataException("El cuenta financiera es obligatorio");
        }
        this.financialAccount = value;
    }

    public void changeCashShift(CashShift value) {
        this.cashShift = value;
    }

    private void changeCustomer(Customer value) {
        this.customer = value;
    }

    private void changeSupplier(Supplier value) {
        this.supplier = value;
    }

    public void changePurchaseOrder(PurchaseOrder value) {
        this.purchaseOrder = value;
    }

    public void changeSourcePayment(Payment value) {
        this.sourcePayment = value;
    }

    public void changeDelivery(Delivery value) {
        this.delivery = value;
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

    private void changeAmount(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El importe supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El importe no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El importe no puede ser negativo");
        }
        this.amount = value;
    }

    private void changeTip(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El tip supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El tip no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El tip no puede ser negativo");
        }
        if (value != null && value.signum() <= 0) {
            throw new InvalidEntityDataException("El tip debe ser mayor que cero");
        }
        this.tip = value;
    }

    private void changeAmountReceived(BigDecimal value) {
        if (value == null) {
            throw new InvalidEntityDataException("El importe recibido es obligatorio");
        }
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El importe recibido supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El importe recibido no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El importe recibido no puede ser negativo");
        }
        this.amountReceived = value;
    }

    private void changeChangeAmount(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El change importe supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El change importe no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El change importe no puede ser negativo");
        }
        this.changeAmount = value;
    }

    public void changeExternalReference(String value) {
        if (value == null || value.isBlank()) {
            this.externalReference = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 150) {
            throw new InvalidEntityDataException("El external reference no puede superar 150 caracteres");
        }
        this.externalReference = normalized;
    }

    public void changeEvidenceUrl(String value) {
        if (value == null || value.isBlank()) {
            this.evidenceUrl = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 1000) {
            throw new InvalidEntityDataException("El URL de evidencia no puede superar 1000 caracteres");
        }
        this.evidenceUrl = normalized;
    }

    private void changeStatus(PaymentStates value) {
        this.status = value;
    }

    public void changeVerifiedByUserBusiness(UserBusiness value) {
        this.verifiedByUserBusiness = value;
    }

    private void changeVerifiedAt(LocalDateTime value) {
        this.verifiedAt = value;
    }

    public void changePaymentAmounts(BigDecimal amount, BigDecimal tip, BigDecimal amountReceived, BigDecimal changeAmount) {
        if (amount == null || tip == null || amountReceived == null || changeAmount == null) {
            throw new InvalidEntityDataException("Los cuatro importes del pago son obligatorios");
        }
        if (amount.signum() < 0) {
            throw new InvalidEntityDataException("El importe del pago no puede ser negativo");
        }
        if (tip.signum() < 0) {
            throw new InvalidEntityDataException("La propina no puede ser negativa");
        }
        if (amount.add(tip).signum() <= 0) {
            throw new InvalidEntityDataException("El pago debe tener un importe total mayor que cero");
        }
        if (changeAmount.signum() < 0 || amountReceived.signum() < 0) {
            throw new InvalidEntityDataException("El efectivo recibido y el vuelto no pueden ser negativos");
        }
        if (amountReceived.subtract(changeAmount).compareTo(amount.add(tip)) != 0) {
            throw new InvalidEntityDataException("El importe recibido menos el vuelto debe igualar el pago y la propina");
        }
        if (this.paymentMethod != PaymentMethod.EFECTIVO && changeAmount.signum() > 0) {
            throw new InvalidEntityDataException("Solo los pagos en efectivo pueden generar vuelto");
        }
        if (this.direction == PaymentDirection.EGRESO && (tip.signum() > 0 || changeAmount.signum() > 0)) {
            throw new InvalidEntityDataException("Los egresos no permiten propina ni vuelto");
        }
        changeAmount(amount);
        changeTip(tip);
        changeAmountReceived(amountReceived);
        changeChangeAmount(changeAmount);
    }

    public void verifyPayment(LocalDateTime verifiedAt, UserBusiness verifiedBy) {
        if (this.status == PaymentStates.VERIFICADO) {
            throw new InvalidEntityDataException("El pago ya fue verificado");
        }
        if (verifiedAt == null) {
            throw new InvalidEntityDataException("La fecha de verificación es obligatoria");
        }
        if (verifiedBy == null) {
            throw new InvalidEntityDataException("El usuario que verifica es obligatorio");
        }
        changeVerifiedAt(verifiedAt);
        changeVerifiedByUserBusiness(verifiedBy);
        changeStatus(PaymentStates.VERIFICADO);
    }
}