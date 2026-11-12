package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.enums.PaymentApplicationStates;
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
public class PaymentApplication {

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
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "order_bill_id")
    private OrderBill orderBill;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "purchase_installment_id")
    private PurchaseInstallment purchaseInstallment;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    private BigDecimal amount = new BigDecimal("0.00");

    @Enumerated(EnumType.STRING)
    private PaymentApplicationStates status = PaymentApplicationStates.APLICADA;

    private String applicationKey;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public PaymentApplication(Business business, Branch branch, Payment payment, OrderBill orderBill,
                              PurchaseInstallment purchaseInstallment, Reservation reservation,
                              BigDecimal amount, String applicationKey) {
        changeBusiness(business);
        changeBranch(branch);
        changePayment(payment);
        changeApplicationKey(applicationKey);
        int destinations = (orderBill != null ? 1 : 0) + (purchaseInstallment != null ? 1 : 0)
                + (reservation != null ? 1 : 0);
        if (destinations == 0) {
            throw new InvalidEntityDataException("Se debe identificar la cuenta, cuota o reserva a la que se aplica el pago");
        }
        if (destinations > 1) {
            throw new InvalidEntityDataException("El pago solo puede aplicarse a un destino por registro");
        }
        changeOrderBill(orderBill);
        changePurchaseInstallment(purchaseInstallment);
        changeReservation(reservation);
        changeAmount(amount);
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

    private void changePayment(Payment value) {
        if (value == null) {
            throw new InvalidEntityDataException("El pago es obligatorio");
        }
        this.payment = value;
    }

    private void changeOrderBill(OrderBill value) {
        this.orderBill = value;
    }

    private void changePurchaseInstallment(PurchaseInstallment value) {
        this.purchaseInstallment = value;
    }

    private void changeReservation(Reservation value) {
        this.reservation = value;
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

    public void changeStatus(PaymentApplicationStates value) {
        this.status = value;
    }

    public void changeApplicationKey(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El application key es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El application key no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 64) {
            throw new InvalidEntityDataException("El application key no puede superar 64 caracteres");
        }
        this.applicationKey = normalized;
    }
}