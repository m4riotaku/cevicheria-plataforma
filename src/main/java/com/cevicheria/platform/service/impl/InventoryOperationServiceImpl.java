package com.cevicheria.platform.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.InventoryOperation;
import com.cevicheria.platform.model.enums.InventoryOperationStatus;
import com.cevicheria.platform.repository.InventoryOperationRepository;
import com.cevicheria.platform.service.InventoryOperationService;
import lombok.RequiredArgsConstructor;
import com.cevicheria.platform.handlerException.BadRequestException;
import com.cevicheria.platform.handlerException.ResourceConflictException;
import com.cevicheria.platform.model.*;
import com.cevicheria.platform.model.enums.*;
import com.cevicheria.platform.security.UsersAccessService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryOperationServiceImpl implements InventoryOperationService {
    private final EntityManager em;
    private final UsersAccessService access;
    private final InventoryOperationRepository operationRepository;

    @Override
    public List<InventoryOperation> getAllByBusiness(Long businessId) {
        return operationRepository.findByBusinessIdOrderByCreatedAtDesc(businessId);
    }

    @Override
    public List<InventoryOperation> getByBranch(Long businessId, Long branchId) {
        return operationRepository.findByBusinessIdAndBranchIdOrderByCreatedAtDesc(businessId, branchId);
    }

    @Override
    public List<InventoryOperation> getByStatus(Long businessId, InventoryOperationStatus status) {
        return operationRepository.findByBusinessIdAndStatusOrderByCreatedAtDesc(businessId, status);
    }

    @Override
    public InventoryOperation getById(Long id) {
        return operationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InventoryOperation", "ID", id));
    }

    @Override
    public InventoryOperation save(InventoryOperation operation) {
        return operationRepository.save(operation);
    }

    @Override
    public InventoryOperation update(Long id, InventoryOperation operation) {
        InventoryOperation current = getById(id);
        if (operation.getType() != null) {
            current.changeType(operation.getType());
        }
        if (operation.getOperationKey() != null) {
            current.changeOperationKey(operation.getOperationKey());
        }
        if (operation.getAuthorizedByUserBusiness() != null) {
            current.changeAuthorizedByUserBusiness(operation.getAuthorizedByUserBusiness());
        }
        if (operation.getDestinationBranch() != null) {
            current.changeDestinationBranch(operation.getDestinationBranch());
        }
        if (operation.getCutoffAt() != null) {
            current.changeCutoffAt(operation.getCutoffAt());
        }
        if (operation.getStatus() != null) {
            current.changeStatus(operation.getStatus());
        }
        if (operation.getReturnResolution() != null) {
            current.changeReturnResolution(operation.getReturnResolution());
        }
        if (operation.getReason() != null) {
            current.changeReason(operation.getReason());
        }
        if (operation.getEvidenceUrl() != null) {
            current.changeEvidenceUrl(operation.getEvidenceUrl());
        }
        return operationRepository.save(current);
    }

    @Override
    public void deleteById(Long id) {
        operationRepository.delete(getById(id));
    }

    @Override
    @Transactional
    public void receivePurchase(Purchase purchase) {
        if (purchase.getType() == PurchaseType.NOTA_CREDITO || purchase.getType() == PurchaseType.NOTA_DEBITO)
            throw new BadRequestException("Las notas requieren un flujo de devolución o ajuste; no se registran como recepción de compra");
        em.lock(purchase.getBranch(), LockModeType.PESSIMISTIC_WRITE);
        String key = "COMPRA:" + purchase.getId();
        ensureNewOperation(purchase.getBusiness().getId(), key);
        List<PurchaseItem> details = em.createQuery(
                        "select i from PurchaseItem i where i.purchase.id = :id order by i.id", PurchaseItem.class)
                .setParameter("id", purchase.getId()).getResultList();
        if (details.isEmpty()) throw new BadRequestException("La compra necesita detalles para confirmarse");
        BigDecimal subtotal = BigDecimal.ZERO, tax = BigDecimal.ZERO, total = BigDecimal.ZERO;
        boolean accepted = false;
        for (PurchaseItem detail : details) {
            subtotal = subtotal.add(detail.getSubtotal());
            tax = tax.add(detail.getTax());
            total = total.add(detail.getTotal());
            accepted |= detail.getAcceptedBaseQuantity().signum() > 0;
        }
        if (!accepted) throw new BadRequestException("La compra no tiene cantidades aceptadas");
        purchase.changeAmounts(subtotal, tax, total);
        updatePurchaseOrder(purchase, details);
        InventoryOperation operation = new InventoryOperation(purchase.getBusiness(), purchase.getBranch(),
                InventoryOperationType.COMPRA, LocalDateTime.now(), key, responsible(purchase.getBusiness().getId()));
        operation.changePurchase(purchase);
        operation.changeStatus(InventoryOperationStatus.CONFIRMADA);
        em.persist(operation);
        int sequence = 1;
        for (PurchaseItem detail : details) {
            BigDecimal quantity = detail.getAcceptedBaseQuantity();
            if (quantity.signum() == 0) continue;
            if (!detail.getWarehouse().isActive()) throw new BadRequestException("El almacén está inactivo");
            BigDecimal cost = detail.getAcceptedTotalCost().signum() > 0 ? detail.getAcceptedTotalCost() : detail.getTotal();
            BigDecimal unitCost = cost.divide(quantity, 6, RoundingMode.HALF_UP);
            InventoryBalance balance = balance(purchase, detail);
            BigDecimal newQuantity = balance.getQuantity().add(quantity);
            BigDecimal newValue = balance.getQuantity().multiply(balance.getAverageCost()).add(cost);
            balance.changeQuantity(newQuantity);
            balance.changeAverageCost(newValue.divide(newQuantity, 6, RoundingMode.HALF_UP));
            InventoryBatch batch = new InventoryBatch(purchase.getBusiness(), purchase.getBranch(),
                    detail.getWarehouse(), detail.getIngredient(), "COMPRA-" + purchase.getId() + "-" + detail.getId(),
                    detail.getReceivedAt() == null ? LocalDateTime.now() : detail.getReceivedAt());
            batch.changePurchaseItem(detail);
            batch.changeOnHandQuantity(quantity);
            em.persist(batch);
            InventoryMovement movement = new InventoryMovement(purchase.getBusiness(), purchase.getBranch(),
                    operation, batch, sequence++, InventoryMovementType.ENTRADA, LocalDateTime.now(), "Recepción de compra");
            movement.changePurchaseItem(detail);
            movement.changeQuantity(quantity);
            movement.changeUnitCost(unitCost);
            em.persist(movement);
        }
        em.flush();
    }

    private InventoryBalance balance(Purchase purchase, PurchaseItem detail) {
        List<InventoryBalance> balances = em.createQuery(
                        "select b from InventoryBalance b where b.business.id = :b and b.warehouse.id = :w and b.ingredient.id = :i", InventoryBalance.class)
                .setParameter("b", purchase.getBusiness().getId()).setParameter("w", detail.getWarehouse().getId())
                .setParameter("i", detail.getIngredient().getId()).setLockMode(LockModeType.PESSIMISTIC_WRITE).getResultList();
        if (!balances.isEmpty()) return balances.getFirst();
        InventoryBalance balance = new InventoryBalance(purchase.getBusiness(), purchase.getBranch(), detail.getWarehouse(), detail.getIngredient());
        em.persist(balance);
        return balance;
    }

    private void updatePurchaseOrder(Purchase purchase, List<PurchaseItem> receivedItems) {
        PurchaseOrder order = purchase.getPurchaseOrder();
        if (order == null) return;
        em.lock(order, LockModeType.PESSIMISTIC_WRITE);
        if (order.getStatus() != PurchaseOrderStatus.APROBADA && order.getStatus() != PurchaseOrderStatus.PARCIAL)
            throw new BadRequestException("La orden de compra debe estar aprobada o parcialmente recibida");
        List<PurchaseOrderItem> ordered = em.createQuery(
                        "select i from PurchaseOrderItem i where i.purchaseOrder.id = :id", PurchaseOrderItem.class)
                .setParameter("id", order.getId()).getResultList();
        Map<Long, BigDecimal> requested = new LinkedHashMap<>();
        for (PurchaseOrderItem item : ordered) requested.merge(item.getIngredient().getId(), item.getEstimatedBaseQuantity(), BigDecimal::add);
        for (PurchaseItem item : receivedItems) {
            if (!requested.containsKey(item.getIngredient().getId()) || !order.getWarehouse().getId().equals(item.getWarehouse().getId()))
                throw new BadRequestException("El insumo o almacén no corresponde a la orden de compra");
        }
        boolean complete = true;
        for (Map.Entry<Long, BigDecimal> entry : requested.entrySet()) {
            BigDecimal received = em.createQuery(
                            "select coalesce(sum(i.acceptedBaseQuantity),0) from PurchaseItem i where i.purchase.purchaseOrder.id = :o "
                                    + "and i.ingredient.id = :i and i.purchase.status = :s", BigDecimal.class)
                    .setParameter("o", order.getId()).setParameter("i", entry.getKey()).setParameter("s", PurchaseStatus.CONFIRMADA).getSingleResult();
            if (received.compareTo(entry.getValue()) > 0) throw new BadRequestException("La recepción supera la cantidad de la orden de compra");
            complete &= received.compareTo(entry.getValue()) == 0;
        }
        order.changeStatus(complete ? PurchaseOrderStatus.RECIBIDA : PurchaseOrderStatus.PARCIAL);
    }

    @Override
    @Transactional
    public void consumeOrder(CustomerOrder order) {
        em.lock(order.getBranch(), LockModeType.PESSIMISTIC_WRITE);
        String key = "VENTA:" + order.getId();
        ensureNewOperation(order.getBusiness().getId(), key);
        List<OrderItem> lines = em.createQuery(
                        "select i from OrderItem i where i.order.id = :id and i.status <> :s order by i.id", OrderItem.class)
                .setParameter("id", order.getId()).setParameter("s", OrderItemStatus.ANULADO).getResultList();
        Map<OrderItem, Map<Long, BigDecimal>> plans = new LinkedHashMap<>();
        Map<Long, BigDecimal> totals = new LinkedHashMap<>();
        for (OrderItem line : lines) {
            Map<Long, BigDecimal> quantities = new LinkedHashMap<>();
            List<OrderItemRecipeSnapshotIngredient> snapshot = em.createQuery(
                            "select i from OrderItemRecipeSnapshotIngredient i where i.orderItem.id = :id", OrderItemRecipeSnapshotIngredient.class)
                    .setParameter("id", line.getId()).getResultList();
            if (!snapshot.isEmpty()) {
                for (OrderItemRecipeSnapshotIngredient part : snapshot)
                    quantities.merge(part.getIngredient().getId(), part.getBaseQuantityPerServing().multiply(line.getQuantity()), BigDecimal::add);
            } else addProductIngredients(line.getProduct(), line.getQuantity(), quantities, new HashSet<>());
            for (OrderItemModifier modifier : em.createQuery(
                    "select m from OrderItemModifier m where m.orderItem.id = :id", OrderItemModifier.class).setParameter("id", line.getId()).getResultList()) {
                for (ModifierIngredient part : em.createQuery(
                        "select i from ModifierIngredient i where i.modifier.id = :id", ModifierIngredient.class).setParameter("id", modifier.getModifier().getId()).getResultList())
                    quantities.merge(part.getIngredient().getId(), part.getBaseQuantity().multiply(modifier.getQuantity()).multiply(line.getQuantity()), BigDecimal::add);
            }
            quantities.replaceAll((id, quantity) -> quantity.max(BigDecimal.ZERO).setScale(6, RoundingMode.HALF_UP));
            quantities.values().removeIf(quantity -> quantity.signum() == 0);
            plans.put(line, quantities);
            quantities.forEach((id, quantity) -> totals.merge(id, quantity, BigDecimal::add));
        }
        for (Map.Entry<Long, BigDecimal> entry : totals.entrySet()) {
            BigDecimal available = batches(order, entry.getKey()).stream().map(InventoryBatch::getOnHandQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (available.compareTo(entry.getValue()) < 0) throw new ResourceConflictException("Stock insuficiente o sin lotes aptos para el insumo " + entry.getKey());
        }
        InventoryOperation operation = new InventoryOperation(order.getBusiness(), order.getBranch(), InventoryOperationType.VENTA,
                LocalDateTime.now(), key, order.getWaiter() == null ? responsible(order.getBusiness().getId()) : order.getWaiter());
        operation.changeOrder(order);
        operation.changeStatus(InventoryOperationStatus.CONFIRMADA);
        em.persist(operation);
        int sequence = 1;
        for (Map.Entry<OrderItem, Map<Long, BigDecimal>> plan : plans.entrySet()) {
            for (Map.Entry<Long, BigDecimal> entry : plan.getValue().entrySet()) {
                Ingredient ingredient = em.find(Ingredient.class, entry.getKey());
                OrderConsumption consumption = new OrderConsumption(order.getBusiness(), order.getBranch(), plan.getKey(), ingredient, entry.getValue());
                em.persist(consumption);
                BigDecimal remaining = entry.getValue(), cost = BigDecimal.ZERO;
                for (InventoryBatch batch : batches(order, ingredient.getId())) {
                    if (remaining.signum() == 0) break;
                    InventoryBalance balance = em.createQuery(
                                    "select b from InventoryBalance b where b.business.id = :b and b.warehouse.id = :w and b.ingredient.id = :i", InventoryBalance.class)
                            .setParameter("b", order.getBusiness().getId()).setParameter("w", batch.getWarehouse().getId()).setParameter("i", ingredient.getId())
                            .setLockMode(LockModeType.PESSIMISTIC_WRITE).getResultStream().findFirst()
                            .orElseThrow(() -> new ResourceConflictException("El lote no tiene saldo de inventario"));
                    BigDecimal used = remaining.min(batch.getOnHandQuantity()).min(balance.getQuantity());
                    if (used.signum() == 0) continue;
                    batch.changeOnHandQuantity(batch.getOnHandQuantity().subtract(used));
                    if (batch.getOnHandQuantity().signum() == 0) batch.changeStatus(InventoryBatchStatus.AGOTADO);
                    balance.changeQuantity(balance.getQuantity().subtract(used));
                    InventoryMovement movement = new InventoryMovement(order.getBusiness(), order.getBranch(), operation, batch,
                            sequence++, InventoryMovementType.SALIDA, LocalDateTime.now(), "Consumo de pedido confirmado");
                    movement.changeOrderConsumption(consumption);
                    movement.changeQuantity(used);
                    movement.changeUnitCost(balance.getAverageCost());
                    em.persist(movement);
                    cost = cost.add(used.multiply(balance.getAverageCost()));
                    remaining = remaining.subtract(used);
                }
                if (remaining.signum() > 0) throw new ResourceConflictException("El saldo no coincide con los lotes disponibles");
                consumption.changeTotalCost(cost.setScale(2, RoundingMode.HALF_UP));
            }
        }
        em.flush();
    }

    private List<InventoryBatch> batches(CustomerOrder order, Long ingredientId) {
        return em.createQuery("select b from InventoryBatch b where b.business.id = :b and b.branch.id = :br and b.ingredient.id = :i "
                        + "and b.warehouse.active = true and b.status = :s and b.onHandQuantity > 0 and (b.dueOn is null or b.dueOn >= :today) "
                        + "order by case when b.dueOn is null then 1 else 0 end, b.dueOn, b.receivedAt, b.id", InventoryBatch.class)
                .setParameter("b", order.getBusiness().getId()).setParameter("br", order.getBranch().getId()).setParameter("i", ingredientId)
                .setParameter("s", InventoryBatchStatus.APTO).setParameter("today", LocalDate.now()).setLockMode(LockModeType.PESSIMISTIC_WRITE).getResultList();
    }

    private void addProductIngredients(Product product, BigDecimal quantity, Map<Long, BigDecimal> quantities, Set<Long> visited) {
        if (!visited.add(product.getId())) throw new BadRequestException("El combo contiene una referencia circular");
        if (product.getType() == ProductType.COMBO) {
            List<ComboOption> options = em.createQuery("select o from ComboOption o where o.comboProduct.id = :id and o.active = true", ComboOption.class)
                    .setParameter("id", product.getId()).getResultList();
            if (options.isEmpty()) throw new BadRequestException("El combo no tiene productos configurados");
            Set<Integer> positions = new HashSet<>();
            for (ComboOption option : options) {
                if (!positions.add(option.getPositionNumber())) throw new BadRequestException("El flujo requiere una opción activa por posición del combo");
                addProductIngredients(option.getOptionProduct(), quantity.multiply(option.getFraction()), quantities, visited);
            }
        } else if (product.getDirectIngredient() != null) quantities.merge(product.getDirectIngredient().getId(), quantity, BigDecimal::add);
        else {
            Recipe recipe = em.createQuery("select r from Recipe r where r.product.id = :id and r.active = true order by r.version desc", Recipe.class)
                    .setParameter("id", product.getId()).setMaxResults(1).getResultStream().findFirst()
                    .orElseThrow(() -> new BadRequestException("El producto " + product.getName() + " necesita una receta activa"));
            List<RecipeIngredient> parts = em.createQuery("select i from RecipeIngredient i where i.recipe.id = :id", RecipeIngredient.class)
                    .setParameter("id", recipe.getId()).getResultList();
            if (parts.isEmpty()) throw new BadRequestException("La receta no tiene insumos");
            for (RecipeIngredient part : parts)
                quantities.merge(part.getIngredient().getId(), part.getBaseQuantity().multiply(quantity)
                        .divide(recipe.getServings().multiply(recipe.getYieldRatio()), 6, RoundingMode.HALF_UP), BigDecimal::add);
        }
        visited.remove(product.getId());
    }

    private Employee responsible(Long businessId) {
        return em.createQuery("select u.employee from UserBusiness u where u.business.id = :b and u.userAccount.id = :u "
                        + "and u.active = true and u.employee.active = true", Employee.class)
                .setParameter("b", businessId).setParameter("u", access.currentUserId()).getResultStream().findFirst()
                .orElseThrow(() -> new BadRequestException("El usuario necesita un empleado activo asociado al negocio para registrar inventario"));
    }

    private void ensureNewOperation(Long businessId, String key) {
        Long count = em.createQuery("select count(o) from InventoryOperation o where o.business.id = :b and o.operationKey = :k", Long.class)
                .setParameter("b", businessId).setParameter("k", key).getSingleResult();
        if (count > 0) throw new ResourceConflictException("La operación de inventario ya fue registrada");
    }
}
