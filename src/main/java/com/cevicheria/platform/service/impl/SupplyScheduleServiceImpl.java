package com.cevicheria.platform.service.impl;

import java.util.List;
import java.util.Objects;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cevicheria.platform.dto.SupplyScheduleRequest;
import com.cevicheria.platform.dto.SupplyScheduleResponse;
import com.cevicheria.platform.handlerException.BadRequestException;
import com.cevicheria.platform.handlerException.ResourceConflictException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.Branch;
import com.cevicheria.platform.model.Business;
import com.cevicheria.platform.model.Ingredient;
import com.cevicheria.platform.model.Supplier;
import com.cevicheria.platform.model.SupplySchedule;
import com.cevicheria.platform.model.Warehouse;
import com.cevicheria.platform.repository.SupplyScheduleRepository;
import com.cevicheria.platform.service.SupplyScheduleService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SupplyScheduleServiceImpl implements SupplyScheduleService {
    private final SupplyScheduleRepository repository;
    private final EntityManager entityManager;

    @Override
    public List<SupplyScheduleResponse> getAll(Long businessId, Long branchId) {
        validateId(businessId);
        validateId(branchId);
        return repository.findByBusinessIdAndBranchId(businessId, branchId).stream().map(this::toResponse).toList();
    }

    @Override
    public SupplyScheduleResponse getById(Long businessId, Long id) {
        return toResponse(find(businessId, id));
    }

    @Override
    @Transactional
    public SupplyScheduleResponse create(SupplyScheduleRequest request) {
        validateRequest(request);
        Business business = require(Business.class, request.businessId());
        Branch branch = require(Branch.class, request.branchId());
        Warehouse warehouse = require(Warehouse.class, request.warehouseId());
        Ingredient ingredient = require(Ingredient.class, request.ingredientId());
        Supplier supplier = require(Supplier.class, request.supplierId());
        SupplySchedule current = new SupplySchedule(business, branch, warehouse, ingredient, supplier, request.baseQuantity(), request.frequencyDays(), request.nextDate());
        applyChanges(current, request);
        validateRelations(current);
        return toResponse(repository.saveAndFlush(current));
    }

    @Override
    @Transactional
    public SupplyScheduleResponse update(Long businessId, Long id, SupplyScheduleRequest request) {
        validateRequest(request);
        if (request.businessId() == null
                && request.branchId() == null
                && request.warehouseId() == null
                && request.ingredientId() == null
                && request.supplierId() == null
                && request.alternateSupplierId() == null
                && request.baseQuantity() == null
                && request.frequencyDays() == null
                && request.nextDate() == null
                && request.active() == null) {
            throw new BadRequestException("Debes enviar al menos un dato para actualizar");
        }
        SupplySchedule current = find(businessId, id);
        if (request.businessId() != null && !Objects.equals(request.businessId(), current.getBusiness().getId())) {
            throw new BadRequestException("El campo businessId no se puede modificar");
        }
        if (request.branchId() != null && !Objects.equals(request.branchId(), current.getBranch().getId())) {
            throw new BadRequestException("El campo branchId no se puede modificar");
        }
        if (request.warehouseId() != null && !Objects.equals(request.warehouseId(), current.getWarehouse().getId())) {
            throw new BadRequestException("El campo warehouseId no se puede modificar");
        }
        if (request.ingredientId() != null && !Objects.equals(request.ingredientId(), current.getIngredient().getId())) {
            throw new BadRequestException("El campo ingredientId no se puede modificar");
        }
        if (request.supplierId() != null && !Objects.equals(request.supplierId(), current.getSupplier().getId())) {
            throw new BadRequestException("El campo supplierId no se puede modificar");
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

    private SupplySchedule find(Long businessId, Long id) {
        validateId(businessId);
        validateId(id);
        return repository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("SupplySchedule", "ID", id));
    }

    private void applyChanges(SupplySchedule current, SupplyScheduleRequest request) {
        if (request.alternateSupplierId() != null) {
            current.changeAlternateSupplier(require(Supplier.class, request.alternateSupplierId()));
        }
        if (request.baseQuantity() != null) {
            current.changeBaseQuantity(request.baseQuantity());
        }
        if (request.frequencyDays() != null) {
            current.changeFrequencyDays(request.frequencyDays());
        }
        if (request.nextDate() != null) {
            current.changeNextDate(request.nextDate());
        }
        if (request.active() != null) {
            if (request.active()) {
                current.activate();
            } else {
                current.deactivate();
            }
        }
    }

    private void validateRelations(SupplySchedule current) {
        if (current.getBranch() != null && (!Objects.equals(current.getBusiness().getId(), current.getBranch().getBusiness().getId()))) {
            throw new BadRequestException("La relación branch no pertenece al mismo ámbito");
        }
        if (current.getWarehouse() != null
                && (!Objects.equals(current.getBusiness().getId(), current.getWarehouse().getBusiness().getId())
                || !Objects.equals(current.getBranch().getId(), current.getWarehouse().getBranch().getId()))) {
            throw new BadRequestException("La relación warehouse no pertenece al mismo ámbito");
        }
        if (current.getIngredient() != null && (!Objects.equals(current.getBusiness().getId(), current.getIngredient().getBusiness().getId()))) {
            throw new BadRequestException("La relación ingredient no pertenece al mismo ámbito");
        }
        if (current.getSupplier() != null && (!Objects.equals(current.getBusiness().getId(), current.getSupplier().getBusiness().getId()))) {
            throw new BadRequestException("La relación supplier no pertenece al mismo ámbito");
        }
        if (current.getAlternateSupplier() != null && (!Objects.equals(current.getBusiness().getId(), current.getAlternateSupplier().getBusiness().getId()))) {
            throw new BadRequestException("La relación alternateSupplier no pertenece al mismo ámbito");
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

    private void validateRequest(SupplyScheduleRequest request) {
        if (request == null) {
            throw new BadRequestException("Los datos son obligatorios");
        }
    }

    private SupplyScheduleResponse toResponse(SupplySchedule current) {
        return new SupplyScheduleResponse(
                current.getId(),
                current.getBusiness() == null ? null : current.getBusiness().getId(),
                current.getBranch() == null ? null : current.getBranch().getId(),
                current.getWarehouse() == null ? null : current.getWarehouse().getId(),
                current.getIngredient() == null ? null : current.getIngredient().getId(),
                current.getSupplier() == null ? null : current.getSupplier().getId(),
                current.getAlternateSupplier() == null ? null : current.getAlternateSupplier().getId(),
                current.getBaseQuantity(),
                current.getFrequencyDays(),
                current.getNextDate(),
                current.isActive(),
                current.getCreatedAt());
    }
}
