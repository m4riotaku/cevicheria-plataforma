package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.enums.OrderSource;
import com.cevicheria.platform.model.enums.OrderStatus;
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
public class CustomerOrder {

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

    private String number;

    private String requestKey;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "dining_table_id")
    private DiningTable diningTable;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "waiter_id")
    private Employee waiter;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "merged_into_order_id")
    private CustomerOrder mergedIntoOrder;

    @Enumerated(EnumType.STRING)
    private SalesChannel salesChannel;

    @Enumerated(EnumType.STRING)
    private OrderSource source = OrderSource.PERSONAL;

    private LocalDateTime recordDate;

    private Integer guestCount = 1;

    private String contactName;

    private String contactPhone;

    private String deliveryAddress;

    private byte[] trackingTokenHash;

    private String currencyCode = "PEN";

    private BigDecimal subtotal = new BigDecimal("0.00");

    private BigDecimal discount = new BigDecimal("0.00");

    private BigDecimal tax = new BigDecimal("0.00");

    private BigDecimal deliveryFee = new BigDecimal("0.00");

    private BigDecimal total = new BigDecimal("0.00");

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.SOLICITADO;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "authorized_by_user_business_id")
    private UserBusiness authorizedByUserBusiness;

    private String changeReason;

    private String notes;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public CustomerOrder(Business business, Branch branch, String number, String requestKey, SalesChannel salesChannel, LocalDateTime recordDate) {
        changeBusiness(business);
        changeBranch(branch);
        changeNumber(number);
        changeRequestKey(requestKey);
        changeSalesChannel(salesChannel);
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

    public void changeRequestKey(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El request key es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El request key no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 64) {
            throw new InvalidEntityDataException("El request key no puede superar 64 caracteres");
        }
        this.requestKey = normalized;
    }

    private void changeCustomer(Customer value) {
        this.customer = value;
    }

    public void changeDiningTable(DiningTable value) {
        this.diningTable = value;
    }

    public void changeReservation(Reservation value) {
        this.reservation = value;
    }

    public void changeWaiter(Employee value) {
        this.waiter = value;
    }

    public void changeMergedIntoOrder(CustomerOrder value) {
        this.mergedIntoOrder = value;
    }

    public void changeSalesChannel(SalesChannel value) {
        if (value == null) {
            throw new InvalidEntityDataException("El canal de venta es obligatorio");
        }
        this.salesChannel = value;
    }

    public void changeSource(OrderSource value) {
        this.source = value;
    }

    private void changeRecordDate(LocalDateTime value) {
        if (value == null) {
            throw new InvalidEntityDataException("La fecha de registro es obligatoria");
        }
        this.recordDate = value;
    }

    public void changeGuestCount(Integer value) {
        if (value != null && value < 0) {
            throw new InvalidEntityDataException("El guest count no puede ser negativo");
        }
        if (value != null && value > 65535) {
            throw new InvalidEntityDataException("El guest count supera el máximo permitido");
        }
        this.guestCount = value;
    }

    public void changeContactName(String value) {
        if (value == null || value.isBlank()) {
            this.contactName = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 150) {
            throw new InvalidEntityDataException("El contact nombre no puede superar 150 caracteres");
        }
        this.contactName = normalized;
    }

    public void changeContactPhone(String value) {
        if (value == null || value.isBlank()) {
            this.contactPhone = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 30) {
            throw new InvalidEntityDataException("El contact phone no puede superar 30 caracteres");
        }
        this.contactPhone = normalized;
    }

    public void changeDeliveryAddress(String value) {
        if (value == null || value.isBlank()) {
            this.deliveryAddress = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 300) {
            throw new InvalidEntityDataException("El delivery dirección no puede superar 300 caracteres");
        }
        this.deliveryAddress = normalized;
    }

    public void changeTrackingTokenHash(byte[] value) {
        if (value != null && value.length != 32) {
            throw new InvalidEntityDataException("El tracking token hash debe contener 32 bytes");
        }
        this.trackingTokenHash = value == null ? null : value.clone();
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

    public void changeStatus(OrderStatus value) {
        this.status = value;
    }

    public void changeAuthorizedByUserBusiness(UserBusiness value) {
        this.authorizedByUserBusiness = value;
    }

    public void changeChangeReason(String value) {
        if (value == null || value.isBlank()) {
            this.changeReason = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 250) {
            throw new InvalidEntityDataException("El change reason no puede superar 250 caracteres");
        }
        this.changeReason = normalized;
    }

    public void changeNotes(String value) {
        if (value == null || value.isBlank()) {
            this.notes = null;
            return;
        }
        String normalized = value.trim();
        this.notes = normalized;
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
            throw new InvalidEntityDataException("El total del pedido no coincide con sus importes");
        }
        changeSubtotal(subtotal);
        changeDiscount(discount);
        changeTax(tax);
        changeDeliveryFee(deliveryFee);
        changeTotal(total);
    }
}
