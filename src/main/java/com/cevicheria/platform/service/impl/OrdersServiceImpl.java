package com.cevicheria.platform.service.impl;

import com.cevicheria.platform.dto.*;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.*;
import com.cevicheria.platform.model.enums.*;
import com.cevicheria.platform.model.id.*;
import com.cevicheria.platform.repository.*;
import com.cevicheria.platform.service.OrdersService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrdersServiceImpl implements OrdersService {
    private final OrdersSupport guard;
    private final EntityManager em;
    private final CustomerOrderRepository orders;
    private final OrderItemRepository items;
    private final DiningTableRepository tables;
    private final BranchProductRepository branchProducts;
    private final RecipeRepository recipes;
    private final RecipeIngredientRepository recipeIngredients;
    private final OrderItemModifierRepository modifiers;
    private final OrderItemRecipeSnapshotRepository snapshots;
    private final OrderItemRecipeSnapshotIngredientRepository snapshotIngredients;

    @Override
    public List<OrdersView> orders(Long b, Long br) {
        guard.read(b, br);
        guard.branch(b, br);
        return orders.findByBusinessIdAndBranchIdOrderByIdDesc(b, br).stream().map(this::view).toList();
    }

    @Override
    public OrdersView order(Long b, Long br, Long id) {
        guard.read(b, br);
        return view(localOrder(b, br, id));
    }

    @Override @Transactional
    public OrdersView createOrder(OrdersCreateRequest r) {
        guard.check(r != null, "Solicitud obligatoria");
        guard.write(r.businessId(), r.branchId(), "PEDIDOS");
        Business business = guard.business(r.businessId());
        Branch branch = guard.branch(r.businessId(), r.branchId());
        guard.check(r.number() != null && !r.number().isBlank(), "El número del pedido es obligatorio");
        guard.check(r.salesChannel() != null, "El canal de venta es obligatorio");
        guard.conflict(orders.existsByBusinessIdAndBranchIdAndNumber(r.businessId(), r.branchId(), r.number().trim()),
                "Ya existe un pedido con este número en la sucursal");
        String key = r.requestKey() == null || r.requestKey().isBlank()
                ? UUID.randomUUID().toString() : r.requestKey().trim();
        guard.conflict(orders.existsByBusinessIdAndRequestKey(r.businessId(), key), "La clave de solicitud ya fue utilizada");
        guard.check(r.guestCount() == null || r.guestCount() > 0, "La cantidad de comensales debe ser positiva");
        guard.check(r.salesChannel() != SalesChannel.DELIVERY
                        || (r.deliveryAddress() != null && !r.deliveryAddress().isBlank()
                        && r.contactPhone() != null && !r.contactPhone().isBlank()),
                "Delivery requiere dirección y teléfono");
        guard.check(r.salesChannel() == SalesChannel.MESA || r.tableId() == null,
                "Solo pedidos de mesa pueden seleccionar una mesa");
        CustomerOrder order = new CustomerOrder(business, branch, r.number(), key, r.salesChannel(), java.time.LocalDateTime.now());
        order.changeStatus(OrderStatus.ABIERTO);
        applyOrderDetails(order, r.customerId(), r.guestCount(), r.contactName(), r.contactPhone(), r.deliveryAddress(), r.notes());
        if (r.waiterId() != null) order.changeWaiter(guard.employee(r.businessId(), r.branchId(), r.waiterId()));
        if (r.tableId() != null) {
            DiningTable table = em.find(DiningTable.class, r.tableId(), LockModeType.PESSIMISTIC_WRITE);
            if (table == null) throw new ResourceNotFoundException("DiningTable", "ID", r.tableId());
            guard.check(Objects.equals(table.getBusiness().getId(), r.businessId())
                    && Objects.equals(table.getBranch().getId(), r.branchId()), "Mesa de otra sucursal");
            guard.conflict(table.getStatus() != DiningTableStatus.DISPONIBLE, "La mesa no está disponible");
            guard.check(r.guestCount() == null || r.guestCount() <= table.getCapacity(),
                    "La cantidad de comensales supera la capacidad de la mesa");
            order.changeDiningTable(table);
            table.changeStatus(DiningTableStatus.OCUPADA);
        }
        return view(orders.saveAndFlush(order));
    }

    @Override @Transactional
    public OrdersView updateOrder(Long b, Long br, Long id, OrdersUpdateRequest r) {
        guard.check(r != null, "Solicitud obligatoria");
        guard.write(b, br, "PEDIDOS");
        CustomerOrder order = localOrder(b, br, id);
        editable(order);
        applyOrderDetails(order, r.customerId(), r.guestCount(), r.contactName(),
                r.contactPhone(), r.deliveryAddress(), r.notes());
        return view(order);
    }

    @Override @Transactional
    public OrdersView confirm(Long b, Long br, Long id) {
        guard.write(b, br, "PEDIDOS");
        CustomerOrder order = localOrder(b, br, id);
        editable(order);
        List<OrderItem> lines = items.findByBusinessIdAndBranchIdAndOrderIdOrderByIdAsc(b, br, id).stream()
                .filter(i -> i.getStatus() != OrderItemStatus.ANULADO).toList();
        guard.check(!lines.isEmpty(), "Agrega al menos un producto antes de confirmar");
        for (OrderItem line : lines) {
            guard.check(line.getStatus() == OrderItemStatus.PENDIENTE, "Solo se pueden confirmar detalles pendientes");
            line.changeStatus(OrderItemStatus.CONFIRMADO);
            line.changeConfirmedAt(java.time.LocalDateTime.now());
            createRecipeSnapshot(b, br, line);
        }
        order.changeStatus(OrderStatus.CONFIRMADO);
        return view(order);
    }

    private void createRecipeSnapshot(Long b, Long br, OrderItem line) {
        Recipe recipe = line.getRecipe();
        if (recipe == null) return;
        var key = new OrderItemRecipeSnapshotId(b, br, line.getId());
        guard.conflict(snapshots.existsById(key), "La receta ya fue congelada para este detalle");
        OrderItemRecipeSnapshot snapshot = new OrderItemRecipeSnapshot(line.getBusiness(), line.getBranch(),
                line, recipe.getVersion(), recipe.getServings(), recipe.getYieldRatio(), recipe.getInstructions());
        snapshots.saveAndFlush(snapshot);
        for (RecipeIngredient part : recipeIngredients.findByBusinessIdAndRecipeId(b, recipe.getId())) {
            Ingredient ingredient = part.getIngredient();
            snapshotIngredients.save(new OrderItemRecipeSnapshotIngredient(line.getBusiness(), line.getBranch(),
                    line, ingredient, ingredient.getName(), ingredient.getBaseUnit(), part.getBaseQuantity()));
        }
    }

    @Override @Transactional
    public OrdersView close(Long b, Long br, Long id) {
        guard.write(b, br, "PEDIDOS");
        CustomerOrder order = localOrder(b, br, id);
        guard.check(order.getStatus() == OrderStatus.ENTREGADO, "Primero entrega todos los productos");
        Long paid = em.createQuery("select count(x) from OrderBill x where x.order.id = :id and x.status = :status", Long.class)
                .setParameter("id", id).setParameter("status", OrderBillStates.PAGADA).getSingleResult();
        Long pending = em.createQuery("select count(x) from OrderBill x where x.order.id = :id and x.status = :status", Long.class)
                .setParameter("id", id).setParameter("status", OrderBillStates.ABIERTA).getSingleResult();
        guard.check(paid > 0 && pending == 0, "Se requiere una cuenta pagada para cerrar el pedido");
        order.changeStatus(OrderStatus.CERRADO);
        releaseTable(order);
        return view(order);
    }

    @Override @Transactional
    public void cancel(Long b, Long br, Long id, OrdersCancelRequest r) {
        guard.write(b, br, "PEDIDOS");
        CustomerOrder order = localOrder(b, br, id);
        guard.check(order.getStatus() == OrderStatus.ABIERTO || order.getStatus() == OrderStatus.SOLICITADO,
                "Solo puede anularse un pedido sin confirmar");
        assertNotBilled(order);
        for (OrderItem line : items.findByBusinessIdAndBranchIdAndOrderIdOrderByIdAsc(b, br, id)) {
            line.changeStatus(OrderItemStatus.ANULADO);
        }
        order.changeChangeReason(r == null ? "Anulación del pedido" : r.reason());
        order.changeStatus(OrderStatus.ANULADO);
        order.changeAmounts(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        releaseTable(order);
    }

    @Override
    public List<OrdersItemView> items(Long b, Long br, Long orderId) {
        guard.read(b, br);
        localOrder(b, br, orderId);
        return items.findByBusinessIdAndBranchIdAndOrderIdOrderByIdAsc(b, br, orderId)
                .stream().map(this::itemView).toList();
    }

    @Override
    public OrdersItemView item(Long b, Long br, Long id) {
        guard.read(b, br);
        return itemView(localItem(b, br, id));
    }

    @Override @Transactional
    public OrdersItemView addItem(OrdersItemRequest r) {
        guard.check(r != null, "Solicitud obligatoria");
        guard.write(r.businessId(), r.branchId(), "PEDIDOS");
        CustomerOrder order = localOrder(r.businessId(), r.branchId(), r.orderId());
        editable(order);
        Product product = guard.require(Product.class, r.productId());
        guard.businessScope(product.getBusiness(), r.businessId());
        guard.check(product.isActive(), "El producto está inactivo");
        BranchProduct configured = branchProducts.findByBusinessIdAndBranchIdAndProductIdAndSalesChannel(
                r.businessId(), r.branchId(), r.productId(), order.getSalesChannel())
                .orElseThrow(() -> new ResourceNotFoundException("BranchProduct", "productId", r.productId()));
        guard.check(configured.isVisible() && !configured.isBlocked(), "Producto no disponible para el canal seleccionado");
        OrderItem line = new OrderItem(order.getBusiness(), order.getBranch(), order, product,
                product.getName(), r.quantity());
        line.changeUnitPrice(guard.money(configured.getPrice(), "precio"));
        line.changeSaleUnit(product.getSaleUnit());
        line.changePreparationNote(r.preparationNote());
        line.changeTaxTreatmentCode(configured.getTaxTreatmentCode());
        if (configured.getPreparationArea() != null) {
            guard.check(configured.getPreparationArea().isActive(), "Área de preparación inactiva");
            line.changePreparationArea(configured.getPreparationArea());
        }
        recipes.findFirstByBusinessIdAndProductIdAndActiveTrueOrderByVersionDesc(r.businessId(), r.productId())
                .ifPresent(line::changeRecipe);
        if (order.getWaiter() != null) line.changeOriginalWaiter(order.getWaiter());
        line.changeAmounts(guard.amount(line.getQuantity(), line.getUnitPrice()), BigDecimal.ZERO,
                BigDecimal.ZERO, guard.amount(line.getQuantity(), line.getUnitPrice()));
        items.saveAndFlush(line);
        recalculate(order);
        return itemView(line);
    }

    @Override @Transactional
    public OrdersItemView updateItem(Long b, Long br, Long id, OrdersItemUpdateRequest r) {
        guard.check(r != null, "Solicitud obligatoria");
        guard.write(b, br, "PEDIDOS");
        OrderItem line = localItem(b, br, id);
        editable(line.getOrder());
        guard.check(line.getStatus() == OrderItemStatus.PENDIENTE, "El detalle ya fue confirmado o anulado");
        line.changeQuantity(r.quantity());
        line.changePreparationNote(r.preparationNote());
        recalculateLine(line);
        recalculate(line.getOrder());
        return itemView(line);
    }

    @Override @Transactional
    public void cancelItem(Long b, Long br, Long id) {
        guard.write(b, br, "PEDIDOS");
        OrderItem line = localItem(b, br, id);
        editable(line.getOrder());
        guard.check(line.getStatus() == OrderItemStatus.PENDIENTE, "El detalle ya fue confirmado o anulado");
        line.changeStatus(OrderItemStatus.ANULADO);
        recalculate(line.getOrder());
    }

    @Override
    public List<OrdersModifierView> modifiers(Long b, Long br, Long itemId) {
        guard.read(b, br);
        localItem(b, br, itemId);
        return modifiers.findByOrderItemIdOrderByIdAsc(itemId).stream().map(this::modifierView).toList();
    }

    @Override @Transactional
    public OrdersModifierView addModifier(OrdersModifierRequest r) {
        guard.check(r != null, "Solicitud obligatoria");
        guard.write(r.businessId(), r.branchId(), "PEDIDOS");
        OrderItem line = localItem(r.businessId(), r.branchId(), r.orderItemId());
        editable(line.getOrder());
        guard.check(line.getStatus() == OrderItemStatus.PENDIENTE, "El detalle ya fue confirmado o anulado");
        Modifier modifier = guard.require(Modifier.class, r.modifierId());
        guard.businessScope(modifier.getBusiness(), r.businessId());
        guard.check(modifier.isActive(), "El modificador está inactivo");
        guard.check((modifier.getProduct() == null || Objects.equals(modifier.getProduct().getId(), line.getProduct().getId()))
                && (modifier.getCategory() == null || (line.getProduct().getCategory() != null
                && Objects.equals(modifier.getCategory().getId(), line.getProduct().getCategory().getId()))),
                "El modificador no corresponde al producto");
        guard.conflict(modifiers.findByOrderItemIdOrderByIdAsc(line.getId()).stream()
                .anyMatch(m -> Objects.equals(m.getModifier().getId(), modifier.getId())),
                "El modificador ya está agregado al detalle");
        OrderItemModifier item = new OrderItemModifier(line.getBusiness(), line.getBranch(), line,
                modifier, modifier.getName(), r.quantity(), modifier.getExtraPrice());
        modifiers.saveAndFlush(item);
        recalculateLine(line);
        recalculate(line.getOrder());
        return modifierView(item);
    }

    @Override @Transactional
    public void removeModifier(Long b, Long br, Long id) {
        guard.write(b, br, "PEDIDOS");
        OrderItemModifier m = guard.local(OrderItemModifier.class, id, b, br,
                OrderItemModifier::getBusiness, OrderItemModifier::getBranch);
        OrderItem line = m.getOrderItem();
        editable(line.getOrder());
        guard.check(line.getStatus() == OrderItemStatus.PENDIENTE, "El detalle ya fue confirmado o anulado");
        modifiers.delete(m);
        modifiers.flush();
        recalculateLine(line);
        recalculate(line.getOrder());
    }

    @Override
    public OrdersRecipeSnapshotView snapshot(Long b, Long br, Long itemId) {
        guard.read(b, br);
        localItem(b, br, itemId);
        OrderItemRecipeSnapshot s = snapshots.findById(new OrderItemRecipeSnapshotId(b, br, itemId))
                .orElseThrow(() -> new ResourceNotFoundException("OrderItemRecipeSnapshot", "orderItemId", itemId));
        return new OrdersRecipeSnapshotView(itemId, s.getRecipeVersion(), s.getServings(),
                s.getYieldRatio(), s.getInstructionsSnapshot());
    }

    @Override
    public List<OrdersRecipeIngredientView> recipeIngredients(Long b, Long br, Long itemId) {
        guard.read(b, br);
        localItem(b, br, itemId);
        return snapshotIngredients.findByOrderItemId(itemId).stream()
                .map(i -> new OrdersRecipeIngredientView(itemId, i.getIngredient().getId(),
                        i.getIngredientNameSnapshot(), i.getBaseUnitSnapshot(), i.getBaseQuantityPerServing()))
                .toList();
    }

    private void applyOrderDetails(CustomerOrder order, Long customerId, Integer count,
            String name, String phone, String address, String notes) {
        if (customerId != null) {
            Customer customer = guard.require(Customer.class, customerId);
            guard.businessScope(customer.getBusiness(), order.getBusiness().getId());
            order.assignCustomer(customer);
        }
        if (count != null) {
            guard.check(count > 0, "La cantidad de comensales debe ser positiva");
            order.changeGuestCount(count);
        }
        order.changeContactName(name);
        order.changeContactPhone(phone);
        order.changeDeliveryAddress(address);
        order.changeNotes(notes);
        guard.check(order.getSalesChannel() != SalesChannel.DELIVERY
                        || (order.getDeliveryAddress() != null && order.getContactPhone() != null),
                "Delivery requiere dirección y teléfono");
    }

    private void editable(CustomerOrder order) {
        guard.check(order.getStatus() == OrderStatus.ABIERTO, "Solo pueden modificarse pedidos abiertos");
        assertNotBilled(order);
    }

    private void assertNotBilled(CustomerOrder order) {
        Long count = em.createQuery("select count(b) from OrderBill b where b.order.id = :id and b.status <> :status", Long.class)
                .setParameter("id", order.getId()).setParameter("status", OrderBillStates.ANULADA).getSingleResult();
        guard.conflict(count > 0, "No se puede modificar un pedido con una cuenta vigente");
    }

    private void recalculateLine(OrderItem line) {
        BigDecimal subtotal = guard.amount(line.getQuantity(), line.getUnitPrice());
        for (OrderItemModifier modifier : modifiers.findByOrderItemIdOrderByIdAsc(line.getId())) {
            subtotal = subtotal.add(guard.amount(line.getQuantity().multiply(modifier.getQuantity()), modifier.getUnitExtraPrice()));
        }
        line.changeAmounts(subtotal, BigDecimal.ZERO, BigDecimal.ZERO, subtotal);
    }

    private void recalculate(CustomerOrder order) {
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal tax = BigDecimal.ZERO;
        for (OrderItem line : items.findByBusinessIdAndBranchIdAndOrderIdOrderByIdAsc(
                order.getBusiness().getId(), order.getBranch().getId(), order.getId())) {
            if (line.getStatus() != OrderItemStatus.ANULADO) {
                subtotal = subtotal.add(line.getSubtotal());
                discount = discount.add(line.getDiscount());
                tax = tax.add(line.getTax());
            }
        }
        BigDecimal fee = order.getDeliveryFee();
        order.changeAmounts(subtotal, discount, tax, fee, subtotal.subtract(discount).add(tax).add(fee));
    }

    private void releaseTable(CustomerOrder order) {
        if (order.getDiningTable() != null) {
            DiningTable t = em.find(DiningTable.class, order.getDiningTable().getId(), LockModeType.PESSIMISTIC_WRITE);
            if (t != null && t.getStatus() == DiningTableStatus.OCUPADA) t.changeStatus(DiningTableStatus.DISPONIBLE);
        }
    }

    private CustomerOrder localOrder(Long b, Long br, Long id) {
        return guard.local(CustomerOrder.class, id, b, br, CustomerOrder::getBusiness, CustomerOrder::getBranch);
    }

    private OrderItem localItem(Long b, Long br, Long id) {
        return guard.local(OrderItem.class, id, b, br, OrderItem::getBusiness, OrderItem::getBranch);
    }

    private OrdersView view(CustomerOrder o) {
        return new OrdersView(o.getId(), o.getBusiness().getId(), o.getBranch().getId(), o.getNumber(),
                o.getSalesChannel(), o.getStatus(), o.getCustomer() == null ? null : o.getCustomer().getId(),
                o.getDiningTable() == null ? null : o.getDiningTable().getId(), o.getGuestCount(),
                o.getSubtotal(), o.getDiscount(), o.getTax(), o.getDeliveryFee(), o.getTotal(), o.getNotes());
    }

    private OrdersItemView itemView(OrderItem i) {
        return new OrdersItemView(i.getId(), i.getOrder().getId(), i.getProduct().getId(),
                i.getPreparationArea() == null ? null : i.getPreparationArea().getId(),
                i.getDescription(), i.getQuantity(), i.getUnitPrice(), i.getSubtotal(), i.getTax(),
                i.getTotal(), i.getStatus());
    }

    private OrdersModifierView modifierView(OrderItemModifier m) {
        return new OrdersModifierView(m.getId(), m.getOrderItem().getId(), m.getModifier().getId(),
                m.getModifierNameSnapshot(), m.getQuantity(), m.getUnitExtraPrice());
    }
}
