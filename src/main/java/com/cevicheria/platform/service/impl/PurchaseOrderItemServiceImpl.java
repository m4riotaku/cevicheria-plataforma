package com.cevicheria.platform.service.impl;

import java.util.List;
import java.util.Objects;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cevicheria.platform.dto.PurchaseOrderItemRequest;
import com.cevicheria.platform.dto.PurchaseOrderItemResponse;
import com.cevicheria.platform.handlerException.BadRequestException;
import com.cevicheria.platform.handlerException.ResourceConflictException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.Branch;
import com.cevicheria.platform.model.Business;
import com.cevicheria.platform.model.Ingredient;
import com.cevicheria.platform.model.IngredientPresentation;
import com.cevicheria.platform.model.PurchaseOrder;
import com.cevicheria.platform.model.PurchaseOrderItem;
import com.cevicheria.platform.repository.PurchaseOrderItemRepository;
import com.cevicheria.platform.service.PurchaseOrderItemService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurchaseOrderItemServiceImpl implements PurchaseOrderItemService {
    private final PurchaseOrderItemRepository repository;
    private final EntityManager entityManager;

    @Override
    public List<PurchaseOrderItemResponse> getAll(Long businessId, Long branchId) {
        validateId(businessId);
        validateId(branchId);
        return repository.findByBusinessIdAndBranchId(businessId, branchId).stream().map(this::toResponse).toList();
    }

    @Override
    public PurchaseOrderItemResponse getById(Long businessId, Long id) {
        return toResponse(find(businessId, id));
    }

    @Override
    @Transactional
    public PurchaseOrderItemResponse create(PurchaseOrderItemRequest request) {
        validateRequest(request);
        Business business = require(Business.class, request.businessId());
        Branch branch = require(Branch.class, request.branchId());
        PurchaseOrder purchaseOrder = require(PurchaseOrder.class, request.purchaseOrderId());
        Ingredient ingredient = require(Ingredient.class, request.ingredientId());
        PurchaseOrderItem current = new PurchaseOrderItem(business, branch, purchaseOrder, ingredient, request.quantity(), request.estimatedBaseQuantity());
        applyChanges(current, request);
        validateRelations(current);
        return toResponse(repository.saveAndFlush(current));
    }

    @Override
    @Transactional
    public PurchaseOrderItemResponse update(Long businessId, Long id, PurchaseOrderItemRequest request) {
        validateRequest(request);
        if (request.businessId() == null
                && request.branchId() == null
                && request.purchaseOrderId() == null
                && request.ingredientId() == null
                && request.presentationId() == null
                && request.quantity() == null
                && request.estimatedBaseQuantity() == null
                && request.unitPrice() == null
                && request.total() == null) {
            throw new BadRequestException("Debes enviar al menos un dato para actualizar");
        }
        PurchaseOrderItem current = find(businessId, id);
        if (request.businessId() != null && !Objects.equals(request.businessId(), current.getBusiness().getId())) {
            throw new BadRequestException("El campo businessId no se puede modificar");
        }
        if (request.branchId() != null && !Objects.equals(request.branchId(), current.getBranch().getId())) {
            throw new BadRequestException("El campo branchId no se puede modificar");
        }
        if (request.purchaseOrderId() != null && !Objects.equals(request.purchaseOrderId(), current.getPurchaseOrder().getId())) {
            throw new BadRequestException("El campo purchaseOrderId no se puede modificar");
        }
        if (request.ingredientId() != null && !Objects.equals(request.ingredientId(), current.getIngredient().getId())) {
            throw new BadRequestException("El campo ingredientId no se puede modificar");
        }
        applyChanges(current, request);
        validateRelations(current);
        return toResponse(repository.saveAndFlush(current));
    }

    @Override
    @Transactional
    public void delete(Long businessId, Long id) {
        repository.delete(find(businessId, id));
        repository.flush();
    }

    private PurchaseOrderItem find(Long businessId, Long id) {
        validateId(businessId);
        validateId(id);
        return repository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrderItem", "ID", id));
    }

    private void applyChanges(PurchaseOrderItem current, PurchaseOrderItemRequest request) {
        if (request.presentationId() != null) {
            current.changePresentation(require(IngredientPresentation.class, request.presentationId()));
        }
        if (request.quantity() != null) {
            current.changeQuantity(request.quantity());
        }
        if (request.estimatedBaseQuantity() != null) {
            current.changeEstimatedBaseQuantity(request.estimatedBaseQuantity());
        }
        if (request.unitPrice() != null) {
            current.changeUnitPrice(request.unitPrice());
        }
        if (request.total() != null) {
            current.changeTotal(request.total());
        }
    }

    private void validateRelations(PurchaseOrderItem current) {
        if (current.getBranch() != null && (!Objects.equals(current.getBusiness().getId(), current.getBranch().getBusiness().getId()))) {
            throw new BadRequestException("La relación branch no pertenece al mismo ámbito");
        }
        if (current.getPurchaseOrder() != null
                && (!Objects.equals(current.getBusiness().getId(), current.getPurchaseOrder().getBusiness().getId())
                || !Objects.equals(current.getBranch().getId(), current.getPurchaseOrder().getBranch().getId()))) {
            throw new BadRequestException("La relación purchaseOrder no pertenece al mismo ámbito");
        }
        if (current.getIngredient() != null && (!Objects.equals(current.getBusiness().getId(), current.getIngredient().getBusiness().getId()))) {
            throw new BadRequestException("La relación ingredient no pertenece al mismo ámbito");
        }
        if (current.getPresentation() != null
                && (!Objects.equals(current.getBusiness().getId(), current.getPresentation().getBusiness().getId())
                || !Objects.equals(current.getIngredient().getId(), current.getPresentation().getIngredient().getId()))) {
            throw new BadRequestException("La relación presentation no pertenece al mismo ámbito");
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

    private void validateRequest(PurchaseOrderItemRequest request) {
        if (request == null) {
            throw new BadRequestException("Los datos son obligatorios");
        }
    }

    private PurchaseOrderItemResponse toResponse(PurchaseOrderItem current) {
        return new PurchaseOrderItemResponse(
                current.getId(),
                current.getBusiness() == null ? null : current.getBusiness().getId(),
                current.getBranch() == null ? null : current.getBranch().getId(),
                current.getPurchaseOrder() == null ? null : current.getPurchaseOrder().getId(),
                current.getIngredient() == null ? null : current.getIngredient().getId(),
                current.getPresentation() == null ? null : current.getPresentation().getId(),
                current.getQuantity(),
                current.getEstimatedBaseQuantity(),
                current.getUnitPrice(),
                current.getTotal(),
                current.getCreatedAt());
    }
}
