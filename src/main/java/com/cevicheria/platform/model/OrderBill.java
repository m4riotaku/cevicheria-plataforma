package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.enums.OrderBillCriterion;
import com.cevicheria.platform.model.enums.OrderBillStates;
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
public class OrderBill {

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
    @JoinColumn(name = "order_id")
    private CustomerOrder order;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private Integer number;

    private String name;

    @Enumerated(EnumType.STRING)
    private OrderBillCriterion criterion = OrderBillCriterion.COMPLETA;

    private BigDecimal subtotal = new BigDecimal("0.00");

    private BigDecimal discount = new BigDecimal("0.00");

    private BigDecimal tax = new BigDecimal("0.00");

    private BigDecimal deliveryFee = new BigDecimal("0.00");

    private BigDecimal total = new BigDecimal("0.00");

    @Enumerated(EnumType.STRING)
    private OrderBillStates status = OrderBillStates.ABIERTA;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public OrderBill(Business business, Branch branch, CustomerOrder order, Integer number) {
        changeBusiness(business);
        changeBranch(branch);
        changeOrder(order);
        changeNumber(number);
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

    private void changeOrder(CustomerOrder value) {
        if (value == null) {
            throw new InvalidEntityDataException("El pedido es obligatorio");
        }
        this.order = value;
    }

    private void changeCustomer(Customer value) {
        this.customer = value;
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

    public void changeName(String value) {
        if (value == null || value.isBlank()) {
            this.name = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 80) {
            throw new InvalidEntityDataException("El nombre no puede superar 80 caracteres");
        }
        this.name = normalized;
    }

    public void changeCriterion(OrderBillCriterion value) {
        this.criterion = value;
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

    public void changeStatus(OrderBillStates value) {
        this.status = value;
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
            throw new InvalidEntityDataException("El total de la cuenta no coincide con sus importes");
        }
        changeSubtotal(subtotal);
        changeDiscount(discount);
        changeTax(tax);
        changeDeliveryFee(deliveryFee);
        changeTotal(total);
    }
}