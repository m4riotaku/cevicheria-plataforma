package com.cevicheria.platform.service.impl;

import java.util.List;
import java.util.Objects;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cevicheria.platform.dto.PurchaseInstallmentRequest;
import com.cevicheria.platform.dto.PurchaseInstallmentResponse;
import com.cevicheria.platform.handlerException.BadRequestException;
import com.cevicheria.platform.handlerException.ResourceConflictException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.Branch;
import com.cevicheria.platform.model.Business;
import com.cevicheria.platform.model.Purchase;
import com.cevicheria.platform.model.PurchaseInstallment;
import com.cevicheria.platform.repository.PurchaseInstallmentRepository;
import com.cevicheria.platform.service.PurchaseInstallmentService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurchaseInstallmentServiceImpl implements PurchaseInstallmentService {
    private final PurchaseInstallmentRepository repository;
    private final EntityManager entityManager;

    @Override
    public List<PurchaseInstallmentResponse> getAll(Long businessId, Long branchId) {
        validateId(businessId);
        validateId(branchId);
        return repository.findByBusinessIdAndBranchId(businessId, branchId).stream().map(this::toResponse).toList();
    }

    @Override
    public PurchaseInstallmentResponse getById(Long businessId, Long id) {
        return toResponse(find(businessId, id));
    }

    @Override
    @Transactional
    public PurchaseInstallmentResponse create(PurchaseInstallmentRequest request) {
        validateRequest(request);
        Business business = require(Business.class, request.businessId());
        Branch branch = require(Branch.class, request.branchId());
        Purchase purchase = require(Purchase.class, request.purchaseId());
        PurchaseInstallment current = new PurchaseInstallment(business, branch, purchase, request.number(), request.dueOn());
        applyChanges(current, request);
        validateRelations(current);
        return toResponse(repository.saveAndFlush(current));
    }

    @Override
    @Transactional
    public PurchaseInstallmentResponse update(Long businessId, Long id, PurchaseInstallmentRequest request) {
        validateRequest(request);
        if (request.businessId() == null && request.branchId() == null && request.purchaseId() == null && request.number() == null && request.dueOn() == null && request.amount() == null) {
            throw new BadRequestException("Debes enviar al menos un dato para actualizar");
        }
        PurchaseInstallment current = find(businessId, id);
        if (request.businessId() != null && !Objects.equals(request.businessId(), current.getBusiness().getId())) {
            throw new BadRequestException("El campo businessId no se puede modificar");
        }
        if (request.branchId() != null && !Objects.equals(request.branchId(), current.getBranch().getId())) {
            throw new BadRequestException("El campo branchId no se puede modificar");
        }
        if (request.purchaseId() != null && !Objects.equals(request.purchaseId(), current.getPurchase().getId())) {
            throw new BadRequestException("El campo purchaseId no se puede modificar");
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

    private PurchaseInstallment find(Long businessId, Long id) {
        validateId(businessId);
        validateId(id);
        return repository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseInstallment", "ID", id));
    }

    private void applyChanges(PurchaseInstallment current, PurchaseInstallmentRequest request) {
        if (request.number() != null) {
            current.changeNumber(request.number());
        }
        if (request.dueOn() != null) {
            current.changeDueOn(request.dueOn());
        }
        if (request.amount() != null) {
            current.changeAmount(request.amount());
        }
    }

    private void validateRelations(PurchaseInstallment current) {
        if (current.getAmount() == null || current.getAmount().signum() <= 0) {
            throw new BadRequestException("El importe de la cuota debe ser mayor que cero");
        }
        repository.findByBusinessIdAndBranchIdAndPurchaseIdAndNumber(current.getBusiness().getId(),
                        current.getBranch().getId(), current.getPurchase().getId(), current.getNumber())
                .filter(existing -> !Objects.equals(existing.getId(), current.getId()))
                .ifPresent(existing -> {
                    throw new ResourceConflictException("Ya existe una cuota con ese número para la compra");
                });
        if (current.getBranch() != null && (!Objects.equals(current.getBusiness().getId(), current.getBranch().getBusiness().getId()))) {
            throw new BadRequestException("La relación branch no pertenece al mismo ámbito");
        }
        if (current.getPurchase() != null
                && (!Objects.equals(current.getBusiness().getId(), current.getPurchase().getBusiness().getId())
                || !Objects.equals(current.getBranch().getId(), current.getPurchase().getBranch().getId()))) {
            throw new BadRequestException("La relación purchase no pertenece al mismo ámbito");
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

    private void validateRequest(PurchaseInstallmentRequest request) {
        if (request == null) {
            throw new BadRequestException("Los datos son obligatorios");
        }
    }

    private PurchaseInstallmentResponse toResponse(PurchaseInstallment current) {
        return new PurchaseInstallmentResponse(
                current.getId(),
                current.getBusiness() == null ? null : current.getBusiness().getId(),
                current.getBranch() == null ? null : current.getBranch().getId(),
                current.getPurchase() == null ? null : current.getPurchase().getId(),
                current.getNumber(),
                current.getDueOn(),
                current.getAmount(),
                current.getCreatedAt());
    }
}
