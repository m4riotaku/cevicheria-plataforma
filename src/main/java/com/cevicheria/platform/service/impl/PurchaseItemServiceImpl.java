package com.cevicheria.platform.service.impl;

import java.util.List;
import java.util.Objects;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import com.cevicheria.platform.model.enums.PurchaseStatus;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cevicheria.platform.dto.PurchaseItemRequest;
import com.cevicheria.platform.dto.PurchaseItemResponse;
import com.cevicheria.platform.handlerException.BadRequestException;
import com.cevicheria.platform.handlerException.ResourceConflictException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.Branch;
import com.cevicheria.platform.model.Business;
import com.cevicheria.platform.model.Ingredient;
import com.cevicheria.platform.model.Purchase;
import com.cevicheria.platform.model.PurchaseItem;
import com.cevicheria.platform.model.Warehouse;
import com.cevicheria.platform.repository.PurchaseItemRepository;
import com.cevicheria.platform.service.PurchaseItemService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurchaseItemServiceImpl implements PurchaseItemService {
    private final PurchaseItemRepository repository;
    private final EntityManager entityManager;

    @Override
    public List<PurchaseItemResponse> getAll(Long businessId, Long branchId) {
        validateId(businessId);
        validateId(branchId);
        return repository.findByBusinessIdAndBranchId(businessId, branchId).stream().map(this::toResponse).toList();
    }

    @Override
    public PurchaseItemResponse getById(Long businessId, Long id) {
        return toResponse(find(businessId, id));
    }

    @Override
    @Transactional
    public PurchaseItemResponse create(PurchaseItemRequest request) {
        validateRequest(request);
        Business business = require(Business.class, request.businessId());
        Branch branch = require(Branch.class, request.branchId());
        Purchase purchase = require(Purchase.class, request.purchaseId());
        requireDraft(purchase);
        Ingredient ingredient = require(Ingredient.class, request.ingredientId());
        Warehouse warehouse = require(Warehouse.class, request.warehouseId());
        PurchaseItem current = new PurchaseItem(business, branch, purchase, ingredient, warehouse, request.presentationQuantity(), request.acceptedBaseQuantity());
        applyChanges(current, request);
        validateRelations(current);
        repository.saveAndFlush(current);
        refreshPurchaseAmounts(current.getPurchase());
        return toResponse(current);
    }

    @Override
    @Transactional
    public PurchaseItemResponse update(Long businessId, Long id, PurchaseItemRequest request) {
        validateRequest(request);
        if (request.businessId() == null
                && request.branchId() == null
                && request.purchaseId() == null
                && request.ingredientId() == null
                && request.warehouseId() == null
                && request.receivedAt() == null
                && request.presentationQuantity() == null
                && request.acceptedBaseQuantity() == null
                && request.rejectedBaseQuantity() == null
                && request.grossWeight() == null
                && request.tareWeight() == null
                && request.temperature() == null
                && request.acceptedTotalCost() == null
                && request.subtotal() == null
                && request.tax() == null
                && request.total() == null
                && request.rejectionReason() == null
                && request.evidenceUrl() == null) {
            throw new BadRequestException("Debes enviar al menos un dato para actualizar");
        }
        PurchaseItem current = find(businessId, id);
        requireDraft(current.getPurchase());
        if (request.businessId() != null && !Objects.equals(request.businessId(), current.getBusiness().getId())) {
            throw new BadRequestException("El campo businessId no se puede modificar");
        }
        if (request.branchId() != null && !Objects.equals(request.branchId(), current.getBranch().getId())) {
            throw new BadRequestException("El campo branchId no se puede modificar");
        }
        if (request.purchaseId() != null && !Objects.equals(request.purchaseId(), current.getPurchase().getId())) {
            throw new BadRequestException("El campo purchaseId no se puede modificar");
        }
        if (request.ingredientId() != null && !Objects.equals(request.ingredientId(), current.getIngredient().getId())) {
            throw new BadRequestException("El campo ingredientId no se puede modificar");
        }
        if (request.warehouseId() != null && !Objects.equals(request.warehouseId(), current.getWarehouse().getId())) {
            throw new BadRequestException("El campo warehouseId no se puede modificar");
        }
        applyChanges(current, request);
        validateRelations(current);
        repository.saveAndFlush(current);
        refreshPurchaseAmounts(current.getPurchase());
        return toResponse(current);
    }

    @Override
    @Transactional
    public void delete(Long businessId, Long id) {
        PurchaseItem current = find(businessId, id);
        requireDraft(current.getPurchase());
        Purchase purchase = current.getPurchase();
        repository.delete(current);
        repository.flush();
        refreshPurchaseAmounts(purchase);
    }

    private void requireDraft(Purchase purchase) {
        entityManager.lock(purchase, LockModeType.PESSIMISTIC_WRITE);
        if (purchase.getStatus() != PurchaseStatus.BORRADOR)
            throw new ResourceConflictException("Solo se pueden cambiar detalles de compras en borrador");
    }

    private void refreshPurchaseAmounts(Purchase purchase) {
        BigDecimal subtotal = BigDecimal.ZERO, tax = BigDecimal.ZERO, total = BigDecimal.ZERO;
        for (PurchaseItem item : repository.findByPurchaseId(purchase.getId())) {
            subtotal = subtotal.add(item.getSubtotal());
            tax = tax.add(item.getTax());
            total = total.add(item.getTotal());
        }
        purchase.changeAmounts(subtotal, tax, total);
    }

    private PurchaseItem find(Long businessId, Long id) {
        validateId(businessId);
        validateId(id);
        return repository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseItem", "ID", id));
    }

    private void applyChanges(PurchaseItem current, PurchaseItemRequest request) {
        if (request.receivedAt() != null) {
            current.changeReceivedAt(request.receivedAt());
        }
        if (request.presentationQuantity() != null) {
            current.changePresentationQuantity(request.presentationQuantity());
        }
        if (request.acceptedBaseQuantity() != null) {
            current.changeAcceptedBaseQuantity(request.acceptedBaseQuantity());
        }
        if (request.rejectedBaseQuantity() != null) {
            current.changeRejectedBaseQuantity(request.rejectedBaseQuantity());
        }
        if (request.grossWeight() != null) {
            current.changeGrossWeight(request.grossWeight());
        }
        if (request.tareWeight() != null) {
            current.changeTareWeight(request.tareWeight());
        }
        if (request.temperature() != null) {
            current.changeTemperature(request.temperature());
        }
        if (request.acceptedTotalCost() != null) {
            current.changeAcceptedTotalCost(request.acceptedTotalCost());
        }
        if (request.subtotal() != null) {
            current.changeSubtotal(request.subtotal());
        }
        if (request.tax() != null) {
            current.changeTax(request.tax());
        }
        if (request.total() != null) {
            current.changeTotal(request.total());
        }
        if (request.rejectionReason() != null) {
            current.changeRejectionReason(request.rejectionReason());
        }
        if (request.evidenceUrl() != null) {
            current.changeEvidenceUrl(request.evidenceUrl());
        }
    }

    private void validateRelations(PurchaseItem current) {
        if (current.getTotal().compareTo(current.getSubtotal().add(current.getTax())) != 0) {
            throw new BadRequestException("El total del detalle debe ser igual al subtotal más impuestos");
        }
        if (current.getAcceptedBaseQuantity().add(current.getRejectedBaseQuantity()).signum() <= 0) {
            throw new BadRequestException("La cantidad recibida debe ser mayor que cero");
        }
        if (current.getRejectedBaseQuantity().signum() > 0 && current.getRejectionReason() == null) {
            throw new BadRequestException("El motivo de rechazo es obligatorio");
        }
        if ((current.getGrossWeight() == null) != (current.getTareWeight() == null)
                || (current.getGrossWeight() != null && current.getGrossWeight().compareTo(current.getTareWeight()) < 0)) {
            throw new BadRequestException("Debes indicar peso bruto y tara; el peso bruto no puede ser menor que la tara");
        }
        if (current.getBranch() != null && (!Objects.equals(current.getBusiness().getId(), current.getBranch().getBusiness().getId()))) {
            throw new BadRequestException("La relación branch no pertenece al mismo ámbito");
        }
        if (current.getPurchase() != null
                && (!Objects.equals(current.getBusiness().getId(), current.getPurchase().getBusiness().getId())
                || !Objects.equals(current.getBranch().getId(), current.getPurchase().getBranch().getId()))) {
            throw new BadRequestException("La relación purchase no pertenece al mismo ámbito");
        }
        if (current.getIngredient() != null && (!Objects.equals(current.getBusiness().getId(), current.getIngredient().getBusiness().getId()))) {
            throw new BadRequestException("La relación ingredient no pertenece al mismo ámbito");
        }
        if (current.getWarehouse() != null
                && (!Objects.equals(current.getBusiness().getId(), current.getWarehouse().getBusiness().getId())
                || !Objects.equals(current.getBranch().getId(), current.getWarehouse().getBranch().getId()))) {
            throw new BadRequestException("La relación warehouse no pertenece al mismo ámbito");
        }
    }

    private <T> T require(Class<T> type, Long id) {
        validateId(id);
        T value = entityManager.find(type, id);
        if (value == null) {
            throw new ResourceNotFoundException(type.getSimpleName(), "ID", id);
        }
        return value;
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("El ID debe ser un número mayor que cero");
        }
    }

    private void validateRequest(PurchaseItemRequest request) {
        if (request == null) {
            throw new BadRequestException("Los datos son obligatorios");
        }
    }

    private PurchaseItemResponse toResponse(PurchaseItem current) {
        return new PurchaseItemResponse(
                current.getId(),
                current.getBusiness() == null ? null : current.getBusiness().getId(),
                current.getBranch() == null ? null : current.getBranch().getId(),
                current.getPurchase() == null ? null : current.getPurchase().getId(),
                current.getPurchaseOrderItem() == null ? null : current.getPurchaseOrderItem().getId(),
                current.getIngredient() == null ? null : current.getIngredient().getId(),
                current.getPresentation() == null ? null : current.getPresentation().getId(),
                current.getWarehouse() == null ? null : current.getWarehouse().getId(),
                current.getReceivedAt(),
                current.getPresentationQuantity(),
                current.getAcceptedBaseQuantity(),
                current.getRejectedBaseQuantity(),
                current.getGrossWeight(),
                current.getTareWeight(),
                current.getNetWeight(),
                current.getTemperature(),
                current.getAcceptedTotalCost(),
                current.getSubtotal(),
                current.getTax(),
                current.getTotal(),
                current.getRejectionReason(),
                current.getEvidenceUrl(),
                current.getCreatedAt());
    }
}
