package com.cevicheria.platform.service.impl;

import com.cevicheria.platform.model.enums.PurchaseStatus;

import java.util.List;
import java.util.Objects;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cevicheria.platform.dto.PurchaseRequest;
import com.cevicheria.platform.dto.PurchaseResponse;
import com.cevicheria.platform.handlerException.BadRequestException;
import com.cevicheria.platform.handlerException.ResourceConflictException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.Branch;
import com.cevicheria.platform.model.Business;
import com.cevicheria.platform.model.Purchase;
import com.cevicheria.platform.model.PurchaseOrder;
import com.cevicheria.platform.model.Supplier;
import com.cevicheria.platform.repository.PurchaseRepository;
import com.cevicheria.platform.service.PurchaseService;
import com.cevicheria.platform.service.InventoryOperationService;
import jakarta.persistence.LockModeType;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurchaseServiceImpl implements PurchaseService {
    private final PurchaseRepository repository;
    private final EntityManager entityManager;
    private final InventoryOperationService inventoryOperations;

    @Override
    public List<PurchaseResponse> getByStatus(Long businessId, Long branchId, PurchaseStatus status) {
        validateId(businessId);
        validateId(branchId);
        if (status == null) {
            throw new BadRequestException("El estado es obligatorio");
        }
        return repository.findByBusinessIdAndBranchIdAndStatus(businessId, branchId, status)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public List<PurchaseResponse> getAll(Long businessId, Long branchId) {
        validateId(businessId);
        validateId(branchId);
        return repository.findByBusinessIdAndBranchId(businessId, branchId).stream().map(this::toResponse).toList();
    }

    @Override
    public PurchaseResponse getById(Long businessId, Long id) {
        return toResponse(find(businessId, id));
    }

    @Override
    @Transactional
    public PurchaseResponse create(PurchaseRequest request) {
        validateRequest(request);
        if (request.status() != null && request.status() != PurchaseStatus.BORRADOR)
            throw new BadRequestException("Crea la compra en borrador, agrega sus detalles y luego confírmala");
        Business business = require(Business.class, request.businessId());
        Branch branch = require(Branch.class, request.branchId());
        Supplier supplier = require(Supplier.class, request.supplierId());
        Purchase sourcePurchase = optional(Purchase.class, request.sourcePurchaseId());
        Purchase current = new Purchase(business, branch, supplier, request.type(), request.series(), request.number(), request.recordDate(), sourcePurchase);
        applyChanges(current, request);
        validateRelations(current);
        return toResponse(repository.saveAndFlush(current));
    }

    @Override
    @Transactional
    public PurchaseResponse update(Long businessId, Long id, PurchaseRequest request) {
        validateRequest(request);
        if (request.businessId() == null
                && request.branchId() == null
                && request.supplierId() == null
                && request.purchaseOrderId() == null
                && request.sourcePurchaseId() == null
                && request.type() == null
                && request.series() == null
                && request.number() == null
                && request.recordDate() == null
                && request.expirationDate() == null
                && request.currencyCode() == null
                && request.subtotal() == null
                && request.tax() == null
                && request.total() == null
                && request.status() == null
                && request.notes() == null) {
            throw new BadRequestException("Debes enviar al menos un dato para actualizar");
        }
        Purchase current = find(businessId, id);
        entityManager.lock(current, LockModeType.PESSIMISTIC_WRITE);
        if (current.getStatus() != PurchaseStatus.BORRADOR)
            throw new ResourceConflictException("Solo se pueden modificar compras en borrador");
        if (request.businessId() != null && !Objects.equals(request.businessId(), current.getBusiness().getId())) {
            throw new BadRequestException("El campo businessId no se puede modificar");
        }
        if (request.branchId() != null && !Objects.equals(request.branchId(), current.getBranch().getId())) {
            throw new BadRequestException("El campo branchId no se puede modificar");
        }
        if (request.supplierId() != null && !Objects.equals(request.supplierId(), current.getSupplier().getId())) {
            throw new BadRequestException("El campo supplierId no se puede modificar");
        }
        if (request.type() != null && !Objects.equals(request.type(), current.getType())) {
            throw new BadRequestException("El campo type no se puede modificar");
        }
        if (request.recordDate() != null && !Objects.equals(request.recordDate(), current.getRecordDate())) {
            throw new BadRequestException("El campo recordDate no se puede modificar");
        }
        applyChanges(current, request);
        validateRelations(current);
        repository.saveAndFlush(current);
        if (current.getStatus() == PurchaseStatus.CONFIRMADA) inventoryOperations.receivePurchase(current);
        return toResponse(current);
    }

    @Override
    @Transactional
    public void delete(Long businessId, Long id) {
        Purchase current = find(businessId, id);
        entityManager.lock(current, LockModeType.PESSIMISTIC_WRITE);
        if (current.getStatus() != PurchaseStatus.BORRADOR)
            throw new ResourceConflictException("Solo se pueden eliminar compras en borrador");
        repository.delete(current);
        repository.flush();
    }

    private Purchase find(Long businessId, Long id) {
        validateId(businessId);
        validateId(id);
        return repository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase", "ID", id));
    }

    private void applyChanges(Purchase current, PurchaseRequest request) {
        if (request.purchaseOrderId() != null) {
            current.changePurchaseOrder(require(PurchaseOrder.class, request.purchaseOrderId()));
        }
        if (request.sourcePurchaseId() != null) {
            current.changeSourcePurchase(require(Purchase.class, request.sourcePurchaseId()));
        }
        if (request.series() != null) {
            current.changeSeries(request.series());
        }
        if (request.number() != null) {
            current.changeNumber(request.number());
        }
        if (request.expirationDate() != null) {
            current.changeExpirationDate(request.expirationDate());
        }
        if (request.currencyCode() != null) {
            current.changeCurrencyCode(request.currencyCode());
        }
        if (request.status() != null) {
            current.changeStatus(request.status());
        }
        if (request.notes() != null) {
            current.changeNotes(request.notes());
        }
        if (request.subtotal() != null || request.tax() != null || request.total() != null) {
            current.changeAmounts(request.subtotal() == null ? current.getSubtotal() : request.subtotal(),
                    request.tax() == null ? current.getTax() : request.tax(),
                    request.total() == null ? current.getTotal() : request.total());
        }
    }

    private void validateRelations(Purchase current) {
        if (current.getBranch() != null && (!Objects.equals(current.getBusiness().getId(), current.getBranch().getBusiness().getId()))) {
            throw new BadRequestException("La relación branch no pertenece al mismo ámbito");
        }
        if (current.getSupplier() != null && (!Objects.equals(current.getBusiness().getId(), current.getSupplier().getBusiness().getId()))) {
            throw new BadRequestException("La relación supplier no pertenece al mismo ámbito");
        }
        if (current.getPurchaseOrder() != null
                && (!Objects.equals(current.getBusiness().getId(), current.getPurchaseOrder().getBusiness().getId())
                || !Objects.equals(current.getBranch().getId(), current.getPurchaseOrder().getBranch().getId()))) {
            throw new BadRequestException("La relación purchaseOrder no pertenece al mismo ámbito");
        }
        if (current.getSourcePurchase() != null
                && (!Objects.equals(current.getBusiness().getId(), current.getSourcePurchase().getBusiness().getId())
                || !Objects.equals(current.getBranch().getId(), current.getSourcePurchase().getBranch().getId()))) {
            throw new BadRequestException("La relación sourcePurchase no pertenece al mismo ámbito");
        }
        if (current.getSourcePurchase() != null && Objects.equals(current.getId(), current.getSourcePurchase().getId())) {
            throw new BadRequestException("La compra no puede referenciarse a sí misma");
        }
        if (current.getPurchaseOrder() != null && !Objects.equals(current.getSupplier().getId(), current.getPurchaseOrder().getSupplier().getId())) {
            throw new BadRequestException("La orden y la compra deben tener el mismo proveedor");
        }
        repository.findByBusinessIdAndSupplierIdAndTypeAndSeriesAndNumber(current.getBusiness().getId(), current.getSupplier().getId(), current.getType(), current.getSeries(), current.getNumber())
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

    private <T> T optional(Class<T> type, Long id) {
        return id == null ? null : require(type, id);
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("El ID debe ser un número mayor que cero");
        }
    }

    private void validateRequest(PurchaseRequest request) {
        if (request == null) {
            throw new BadRequestException("Los datos son obligatorios");
        }
    }

    private PurchaseResponse toResponse(Purchase current) {
        return new PurchaseResponse(
                current.getId(),
                current.getBusiness() == null ? null : current.getBusiness().getId(),
                current.getBranch() == null ? null : current.getBranch().getId(),
                current.getSupplier() == null ? null : current.getSupplier().getId(),
                current.getPurchaseOrder() == null ? null : current.getPurchaseOrder().getId(),
                current.getSourcePurchase() == null ? null : current.getSourcePurchase().getId(),
                current.getType(),
                current.getSeries(),
                current.getNumber(),
                current.getRecordDate(),
                current.getExpirationDate(),
                current.getCurrencyCode(),
                current.getSubtotal(),
                current.getTax(),
                current.getTotal(),
                current.getStatus(),
                current.getNotes(),
                current.getCreatedAt());
    }
}
