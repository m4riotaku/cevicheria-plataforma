package com.cevicheria.platform.service.impl;

import java.util.List;
import java.util.Objects;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cevicheria.platform.dto.SupplierIngredientRequest;
import com.cevicheria.platform.dto.SupplierIngredientResponse;
import com.cevicheria.platform.handlerException.BadRequestException;
import com.cevicheria.platform.handlerException.ResourceConflictException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.Business;
import com.cevicheria.platform.model.Ingredient;
import com.cevicheria.platform.model.IngredientPresentation;
import com.cevicheria.platform.model.Supplier;
import com.cevicheria.platform.model.SupplierIngredient;
import com.cevicheria.platform.repository.SupplierIngredientRepository;
import com.cevicheria.platform.service.SupplierIngredientService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SupplierIngredientServiceImpl implements SupplierIngredientService {
    private final SupplierIngredientRepository repository;
    private final EntityManager entityManager;

    @Override
    public List<SupplierIngredientResponse> getAll(Long businessId) {
        validateId(businessId);
        return repository.findByBusinessId(businessId).stream().map(this::toResponse).toList();
    }

    @Override
    public SupplierIngredientResponse getById(Long businessId, Long id) {
        return toResponse(find(businessId, id));
    }

    @Override
    @Transactional
    public SupplierIngredientResponse create(SupplierIngredientRequest request) {
        validateRequest(request);
        Business business = require(Business.class, request.businessId());
        Supplier supplier = require(Supplier.class, request.supplierId());
        Ingredient ingredient = require(Ingredient.class, request.ingredientId());
        SupplierIngredient current = new SupplierIngredient(business, supplier, ingredient);
        applyChanges(current, request);
        validateRelations(current);
        return toResponse(repository.saveAndFlush(current));
    }

    @Override
    @Transactional
    public SupplierIngredientResponse update(Long businessId, Long id, SupplierIngredientRequest request) {
        validateRequest(request);
        if (request.businessId() == null
                && request.supplierId() == null
                && request.ingredientId() == null
                && request.presentationId() == null
                && request.brand() == null
                && request.referencePrice() == null
                && request.preferred() == null
                && request.active() == null) {
            throw new BadRequestException("Debes enviar al menos un dato para actualizar");
        }
        SupplierIngredient current = find(businessId, id);
        if (request.businessId() != null && !Objects.equals(request.businessId(), current.getBusiness().getId())) {
            throw new BadRequestException("El campo businessId no se puede modificar");
        }
        if (request.supplierId() != null && !Objects.equals(request.supplierId(), current.getSupplier().getId())) {
            throw new BadRequestException("El campo supplierId no se puede modificar");
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

    private SupplierIngredient find(Long businessId, Long id) {
        validateId(businessId);
        validateId(id);
        return repository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("SupplierIngredient", "ID", id));
    }

    private void applyChanges(SupplierIngredient current, SupplierIngredientRequest request) {
        if (request.presentationId() != null) {
            current.changePresentation(require(IngredientPresentation.class, request.presentationId()));
        }
        if (request.brand() != null) {
            current.changeBrand(request.brand());
        }
        if (request.referencePrice() != null) {
            current.changeReferencePrice(request.referencePrice());
        }
        if (request.active() != null) {
            if (request.active()) {
                current.activate();
            } else {
                current.deactivate();
            }
        }
        if (request.preferred() != null) {
            if (request.preferred()) {
                current.enablePreferred();
            } else {
                current.disablePreferred();
            }
        }
    }

    private void validateRelations(SupplierIngredient current) {
        if (current.getSupplier() != null && (!Objects.equals(current.getBusiness().getId(), current.getSupplier().getBusiness().getId()))) {
            throw new BadRequestException("La relación supplier no pertenece al mismo ámbito");
        }
        if (current.getIngredient() != null && (!Objects.equals(current.getBusiness().getId(), current.getIngredient().getBusiness().getId()))) {
            throw new BadRequestException("La relación ingredient no pertenece al mismo ámbito");
        }
        if (current.getPresentation() != null
                && (!Objects.equals(current.getBusiness().getId(), current.getPresentation().getBusiness().getId())
                || !Objects.equals(current.getIngredient().getId(), current.getPresentation().getIngredient().getId()))) {
            throw new BadRequestException("La relación presentation no pertenece al mismo ámbito");
        }
        repository.findBySupplierIdAndIngredientId(current.getSupplier().getId(), current.getIngredient().getId())
                .filter(existing -> !Objects.equals(existing.getId(), current.getId()))
                .ifPresent(existing -> {
                    throw new ResourceConflictException("Ya existe un registro con esos datos");
                });
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

    private void validateRequest(SupplierIngredientRequest request) {
        if (request == null) {
            throw new BadRequestException("Los datos son obligatorios");
        }
    }

    private SupplierIngredientResponse toResponse(SupplierIngredient current) {
        return new SupplierIngredientResponse(
                current.getId(),
                current.getBusiness() == null ? null : current.getBusiness().getId(),
                current.getSupplier() == null ? null : current.getSupplier().getId(),
                current.getIngredient() == null ? null : current.getIngredient().getId(),
                current.getPresentation() == null ? null : current.getPresentation().getId(),
                current.getBrand(),
                current.getReferencePrice(),
                current.isPreferred(),
                current.isActive(),
                current.getCreatedAt());
    }
}
