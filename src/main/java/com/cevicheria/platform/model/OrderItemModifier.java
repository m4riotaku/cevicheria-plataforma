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
public class OrderItemModifier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String modifierNameSnapshot;

    private BigDecimal quantity = new BigDecimal("1");

    private BigDecimal unitExtraPrice = new BigDecimal("0");

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "modifier_id")
    private Modifier modifier;

    public OrderItemModifier(
            Business business,
            Branch branch,
            OrderItem orderItem,
            Modifier modifier,
            String modifierNameSnapshot,
            BigDecimal quantity,
            BigDecimal unitExtraPrice) {
        changeBusiness(business);
        changeBranch(branch);
        changeOrderItem(orderItem);
        changeModifier(modifier);
        changeModifierNameSnapshot(modifierNameSnapshot);
        changeQuantity(quantity);
        changeUnitExtraPrice(unitExtraPrice);
        if (business.getId() != null && branch.getBusiness() != null
                && branch.getBusiness().getId() != null
                && !business.getId().equals(branch.getBusiness().getId())) {
            throw new InvalidEntityDataException("El local no pertenece al negocio seleccionado");
        }
    }

    private void changeBusiness(Business business) {
        if (business == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        this.business = business;
    }

    private void changeBranch(Branch branch) {
        if (branch == null) {
            throw new InvalidEntityDataException("El local es obligatorio");
        }
        this.branch = branch;
    }

    private void changeOrderItem(OrderItem orderItem) {
        if (orderItem == null) {
            throw new InvalidEntityDataException("El detalle del pedido es obligatorio");
        }
        this.orderItem = orderItem;
    }

    private void changeModifier(Modifier modifier) {
        if (modifier == null) {
            throw new InvalidEntityDataException("El modificador es obligatorio");
        }
        this.modifier = modifier;
    }

    public void changeModifierNameSnapshot(String modifierNameSnapshot) {
        if (modifierNameSnapshot == null) {
            throw new InvalidEntityDataException("El nombre histórico del modificador es obligatorio");
        }
        if (modifierNameSnapshot.isBlank()) {
            throw new InvalidEntityDataException("El nombre histórico del modificador no puede estar vacío");
        }
        String normalizedValue = modifierNameSnapshot.trim();
        if (normalizedValue.length() > 120) {
            throw new InvalidEntityDataException("El nombre histórico del modificador no puede superar 120 caracteres");
        }
        this.modifierNameSnapshot = normalizedValue;
    }

    public void changeQuantity(BigDecimal quantity) {
        if (quantity == null) {
            throw new InvalidEntityDataException("La cantidad es obligatoria");
        }
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidEntityDataException("La cantidad debe ser mayor que cero");
        }
        if (quantity.abs().compareTo(new BigDecimal("999999999999.999999")) > 0) {
            throw new InvalidEntityDataException("La cantidad excede DECIMAL(18,6)");
        }
        if (quantity.stripTrailingZeros().scale() > 6) {
            throw new InvalidEntityDataException("La cantidad admite hasta 6 decimales");
        }
        this.quantity = quantity.setScale(6);
    }

    public void changeUnitExtraPrice(BigDecimal unitExtraPrice) {
        if (unitExtraPrice == null) {
            throw new InvalidEntityDataException("El precio adicional unitario es obligatorio");
        }
        if (unitExtraPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidEntityDataException("El precio adicional unitario debe ser cero o positivo");
        }
        if (unitExtraPrice.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El precio adicional unitario excede DECIMAL(18,2)");
        }
        if (unitExtraPrice.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El precio adicional unitario admite hasta 2 decimales");
        }
        this.unitExtraPrice = unitExtraPrice.setScale(2);
    }

}
