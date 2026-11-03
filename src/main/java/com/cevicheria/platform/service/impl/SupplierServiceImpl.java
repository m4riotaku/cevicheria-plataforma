package com.cevicheria.platform.service.impl;

import java.util.List;
import java.util.Objects;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cevicheria.platform.dto.SupplierRequest;
import com.cevicheria.platform.dto.SupplierResponse;
import com.cevicheria.platform.handlerException.BadRequestException;
import com.cevicheria.platform.handlerException.ResourceConflictException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.Business;
import com.cevicheria.platform.model.Supplier;
import com.cevicheria.platform.repository.SupplierRepository;
import com.cevicheria.platform.service.SupplierService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository repository;
    private final EntityManager entityManager;

    @Override
    public List<SupplierResponse> getAll(Long businessId) {
        validateId(businessId);
        return repository.findByBusinessId(businessId).stream().map(this::toResponse).toList();
    }

    @Override
    public SupplierResponse getById(Long businessId, Long id) {
        return toResponse(find(businessId, id));
    }

    @Override
    @Transactional
    public SupplierResponse create(SupplierRequest request) {
        validateRequest(request);
        Business business = require(Business.class, request.businessId());
        Supplier current = new Supplier(business, request.name(), request.documentType(), request.documentNumber());
        applyChanges(current, request);
        validateRelations(current);
        return toResponse(repository.saveAndFlush(current));
    }

    @Override
    @Transactional
    public SupplierResponse update(Long businessId, Long id, SupplierRequest request) {
        validateRequest(request);
        if (request.businessId() == null
                && request.name() == null
                && request.documentType() == null
                && request.documentNumber() == null
                && request.contactName() == null
                && request.phone() == null
                && request.email() == null
                && request.address() == null
                && request.creditDays() == null
                && request.active() == null) {
            throw new BadRequestException("Debes enviar al menos un dato para actualizar");
        }
        Supplier current = find(businessId, id);
        if (request.businessId() != null && !Objects.equals(request.businessId(), current.getBusiness().getId())) {
            throw new BadRequestException("El campo businessId no se puede modificar");
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

    private Supplier find(Long businessId, Long id) {
        validateId(businessId);
        validateId(id);
        return repository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "ID", id));
    }

    private void applyChanges(Supplier current, SupplierRequest request) {
        if (request.name() != null) {
            current.changeName(request.name());
        }
        if (request.documentType() != null) {
            current.changeDocumentType(request.documentType());
        }
        if (request.documentNumber() != null) {
            current.changeDocumentNumber(request.documentNumber());
        }
        if (request.contactName() != null) {
            current.changeContactName(request.contactName());
        }
        if (request.phone() != null) {
            current.changePhone(request.phone());
        }
        if (request.email() != null) {
            current.changeEmail(request.email());
        }
        if (request.address() != null) {
            current.changeAddress(request.address());
        }
        if (request.creditDays() != null) {
            current.changeCreditDays(request.creditDays());
        }
        if (request.active() != null) {
            if (request.active()) {
                current.activate();
            } else {
                current.deactivate();
            }
        }
    }

    private void validateRelations(Supplier current) {
        repository.findByBusinessIdAndDocumentTypeAndDocumentNumber(
                        current.getBusiness().getId(), current.getDocumentType(), current.getDocumentNumber())
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

    private void validateRequest(SupplierRequest request) {
        if (request == null) {
            throw new BadRequestException("Los datos son obligatorios");
        }
    }

    private SupplierResponse toResponse(Supplier current) {
        return new SupplierResponse(
                current.getId(),
                current.getBusiness() == null ? null : current.getBusiness().getId(),
                current.getName(),
                current.getDocumentType(),
                current.getDocumentNumber(),
                current.getContactName(),
                current.getPhone(),
                current.getEmail(),
                current.getAddress(),
                current.getCreditDays(),
                current.isActive(),
                current.getCreatedAt());
    }
}
