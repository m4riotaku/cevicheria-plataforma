package com.cevicheria.platform.service.impl;

import java.util.List;
import java.util.Objects;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cevicheria.platform.dto.DeliveryZoneRequest;
import com.cevicheria.platform.dto.DeliveryZoneResponse;
import com.cevicheria.platform.handlerException.BadRequestException;
import com.cevicheria.platform.handlerException.ResourceConflictException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.Branch;
import com.cevicheria.platform.model.Business;
import com.cevicheria.platform.model.DeliveryZone;
import com.cevicheria.platform.repository.DeliveryZoneRepository;
import com.cevicheria.platform.service.DeliveryZoneService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryZoneServiceImpl implements DeliveryZoneService {
    private final DeliveryZoneRepository repository;
    private final EntityManager entityManager;

    @Override
    public List<DeliveryZoneResponse> getAll(Long businessId, Long branchId) {
        validateId(businessId);
        validateId(branchId);
        return repository.findByBusinessIdAndBranchId(businessId, branchId).stream().map(this::toResponse).toList();
    }

    @Override
    public DeliveryZoneResponse getById(Long businessId, Long id) {
        return toResponse(find(businessId, id));
    }

    @Override
    @Transactional
    public DeliveryZoneResponse create(DeliveryZoneRequest request) {
        validateRequest(request);
        Business business = require(Business.class, request.businessId());
        Branch branch = require(Branch.class, request.branchId());
        DeliveryZone current = new DeliveryZone(business, branch, request.name(), request.estimatedMinutes());
        applyChanges(current, request);
        validateRelations(current);
        return toResponse(repository.saveAndFlush(current));
    }

    @Override
    @Transactional
    public DeliveryZoneResponse update(Long businessId, Long id, DeliveryZoneRequest request) {
        validateRequest(request);
        if (request.businessId() == null
                && request.branchId() == null
                && request.name() == null
                && request.active() == null
                && request.fee() == null
                && request.minimumOrderAmount() == null
                && request.estimatedMinutes() == null) {
            throw new BadRequestException("Debes enviar al menos un dato para actualizar");
        }
        DeliveryZone current = find(businessId, id);
        if (request.businessId() != null && !Objects.equals(request.businessId(), current.getBusiness().getId())) {
            throw new BadRequestException("El campo businessId no se puede modificar");
        }
        if (request.branchId() != null && !Objects.equals(request.branchId(), current.getBranch().getId())) {
            throw new BadRequestException("El campo branchId no se puede modificar");
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

    private DeliveryZone find(Long businessId, Long id) {
        validateId(businessId);
        validateId(id);
        return repository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryZone", "ID", id));
    }

    private void applyChanges(DeliveryZone current, DeliveryZoneRequest request) {
        if (request.name() != null) {
            current.changeName(request.name());
        }
        if (request.fee() != null) {
            current.changeFee(request.fee());
        }
        if (request.minimumOrderAmount() != null) {
            current.changeMinimumOrderAmount(request.minimumOrderAmount());
        }
        if (request.estimatedMinutes() != null) {
            current.changeEstimatedMinutes(request.estimatedMinutes());
        }
        if (request.active() != null) {
            if (request.active()) {
                current.activate();
            } else {
                current.deactivate();
            }
        }
    }

    private void validateRelations(DeliveryZone current) {
        if (current.getBranch() != null && (!Objects.equals(current.getBusiness().getId(), current.getBranch().getBusiness().getId()))) {
            throw new BadRequestException("La relación branch no pertenece al mismo ámbito");
        }
        repository.findByBusinessIdAndName(current.getBusiness().getId(), current.getName())
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

    private void validateRequest(DeliveryZoneRequest request) {
        if (request == null) {
            throw new BadRequestException("Los datos son obligatorios");
        }
    }

    private DeliveryZoneResponse toResponse(DeliveryZone current) {
        return new DeliveryZoneResponse(
                current.getId(),
                current.getBusiness() == null ? null : current.getBusiness().getId(),
                current.getBranch() == null ? null : current.getBranch().getId(),
                current.getName(),
                current.isActive(),
                current.getFee(),
                current.getMinimumOrderAmount(),
                current.getEstimatedMinutes(),
                current.getCreatedAt());
    }
}
