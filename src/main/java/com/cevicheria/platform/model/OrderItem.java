package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.enums.OrderItemDestination;
import com.cevicheria.platform.model.enums.OrderItemStatus;
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
public class OrderItem {

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
    @JoinColumn(name = "parent_order_item_id")
    private OrderItem parentOrderItem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "preparation_area_id")
    private PreparationArea preparationArea;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "original_waiter_id")
    private Employee originalWaiter;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    private String description;

    private BigDecimal quantity;

    private BigDecimal unitPrice = new BigDecimal("0.00");

    private BigDecimal subtotal = new BigDecimal("0.00");

    private BigDecimal discount = new BigDecimal("0.00");

    private BigDecimal tax = new BigDecimal("0.00");

    private BigDecimal total = new BigDecimal("0.00");

    private BigDecimal taxRate;

    private String taxTreatmentCode;

    private String saleUnit = "UND";

    private boolean comboComponent = false;

    @Enumerated(EnumType.STRING)
    private OrderItemDestination destination = OrderItemDestination.VENTA;

    @Enumerated(EnumType.STRING)
    private OrderItemStatus status = OrderItemStatus.PENDIENTE;

    private LocalDateTime confirmedAt;

    private LocalDateTime preparationStartedAt;

    private LocalDateTime readyAt;

    private LocalDateTime deliveredAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "prepared_by_employee_id")
    private Employee preparedByEmployee;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "authorized_by_user_business_id")
    private UserBusiness authorizedByUserBusiness;

    private String cancellationReason;

    private String preparationNote;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public OrderItem(Business business, Branch branch, CustomerOrder order, Product product, String description, BigDecimal quantity) {
        changeBusiness(business);
        changeBranch(branch);
        changeOrder(order);
        changeProduct(product);
        changeDescription(description);
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

    private void changeOrder(CustomerOrder value) {
        if (value == null) {
            throw new InvalidEntityDataException("El pedido es obligatorio");
        }
        this.order = value;
    }

    public void changeParentOrderItem(OrderItem value) {
        this.parentOrderItem = value;
    }

    private void changeProduct(Product value) {
        if (value == null) {
            throw new InvalidEntityDataException("El producto es obligatorio");
        }
        this.product = value;
    }

    public void changeRecipe(Recipe value) {
        this.recipe = value;
    }

    public void changePreparationArea(PreparationArea value) {
        this.preparationArea = value;
    }

    public void changeOriginalWaiter(Employee value) {
        this.originalWaiter = value;
    }

    public void changePromotion(Promotion value) {
        this.promotion = value;
    }

    public void changeDescription(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("La descripción es obligatoria");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("La descripción no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 200) {
            throw new InvalidEntityDataException("La descripción no puede superar 200 caracteres");
        }
        this.description = normalized;
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

    public void changeUnitPrice(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El precio unitario supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El precio unitario no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El precio unitario no puede ser negativo");
        }
        this.unitPrice = value;
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

    public void changeSaleUnit(String value) {
        if (value == null || value.isBlank()) {
            this.saleUnit = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 10) {
            throw new InvalidEntityDataException("El sale unit no puede superar 10 caracteres");
        }
        this.saleUnit = normalized;
    }

    public void enableComboComponent() {
        this.comboComponent = true;
    }

    public void disableComboComponent() {
        this.comboComponent = false;
    }

    public void changeDestination(OrderItemDestination value) {
        this.destination = value;
    }

    public void changeStatus(OrderItemStatus value) {
        this.status = value;
    }

    public void changeConfirmedAt(LocalDateTime value) {
        this.confirmedAt = value;
    }

    public void changePreparationStartedAt(LocalDateTime value) {
        this.preparationStartedAt = value;
    }

    public void changeReadyAt(LocalDateTime value) {
        this.readyAt = value;
    }

    public void changeDeliveredAt(LocalDateTime value) {
        this.deliveredAt = value;
    }

    public void changePreparedByEmployee(Employee value) {
        this.preparedByEmployee = value;
    }

    public void changeAuthorizedByUserBusiness(UserBusiness value) {
        this.authorizedByUserBusiness = value;
    }

    public void changeCancellationReason(String value) {
        if (value == null || value.isBlank()) {
            this.cancellationReason = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 250) {
            throw new InvalidEntityDataException("El cancellation reason no puede superar 250 caracteres");
        }
        this.cancellationReason = normalized;
    }

    public void changePreparationNote(String value) {
        if (value == null || value.isBlank()) {
            this.preparationNote = null;
            return;
        }
        String normalized = value.trim();
        this.preparationNote = normalized;
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
            throw new InvalidEntityDataException("El total del detalle debe coincidir con el subtotal, descuento e impuestos");
        }
        changeSubtotal(subtotal);
        changeDiscount(discount);
        changeTax(tax);
        changeTotal(total);
    }


    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "orderItem", fetch = FetchType.LAZY)
    private List<OrderItemModifier> modifiers = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "orderItem", fetch = FetchType.LAZY)
    private List<OrderItemRecipeSnapshot> recipeSnapshots = new ArrayList<>();

    public List<OrderItemModifier> getModifiers() {
        return List.copyOf(this.modifiers);
    }

    public List<OrderItemRecipeSnapshot> getRecipeSnapshots() {
        return List.copyOf(this.recipeSnapshots);
    }
}