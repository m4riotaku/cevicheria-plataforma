package com.cevicheria.platform.service.impl;
import com.cevicheria.platform.dto.SupplierCategoryLinkResponse;
import com.cevicheria.platform.model.SupplierCategoryLink;
import com.cevicheria.platform.model.Supplier;
import com.cevicheria.platform.model.id.SupplierCategoryLinkId;
import com.cevicheria.platform.repository.SupplierCategoryLinkRepository;
import com.cevicheria.platform.repository.SupplierCategoryRepository;
import com.cevicheria.platform.repository.SupplierRepository;
import com.cevicheria.platform.service.SupplierCategoryLinkService;
import com.cevicheria.platform.handlerException.BadRequestException;
import com.cevicheria.platform.handlerException.ResourceConflictException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SupplierCategoryLinkServiceImpl implements SupplierCategoryLinkService {
    private final SupplierCategoryLinkRepository repository;
    private final SupplierRepository suppliers;
    private final SupplierCategoryRepository targets;

    public List<SupplierCategoryLinkResponse> getAll(Long businessId, Long supplierId) {
        supplier(businessId, supplierId);
        return repository.findByBusinessIdAndSupplierId(businessId, supplierId).stream().map(this::response).toList();
    }

    @Transactional
    public SupplierCategoryLinkResponse create(Long businessId, Long supplierId, Long targetId) {
        var supplier = supplier(businessId, supplierId);
        validateId(targetId);
        var target = targets.findByIdAndBusinessId(targetId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("SupplierCategory", "ID", targetId));
        var key = new SupplierCategoryLinkId(businessId, supplierId, targetId);
        if (repository.existsById(key)) throw new ResourceConflictException("La asociación ya existe");
        return response(repository.saveAndFlush(new SupplierCategoryLink(supplier.getBusiness(), supplier, target)));
    }

    @Transactional
    public void delete(Long businessId, Long supplierId, Long targetId) {
        supplier(businessId, supplierId);
        validateId(targetId);
        var key = new SupplierCategoryLinkId(businessId, supplierId, targetId);
        var current = repository.findById(key)
                .orElseThrow(() -> new ResourceNotFoundException("SupplierCategoryLink", "ID", targetId));
        repository.delete(current);
        repository.flush();
    }

    private Supplier supplier(Long businessId, Long supplierId) {
        validateId(businessId);
        validateId(supplierId);
        return suppliers.findByIdAndBusinessId(supplierId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "ID", supplierId));
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) throw new BadRequestException("El ID debe ser positivo");
    }

    private SupplierCategoryLinkResponse response(SupplierCategoryLink value) {
        return new SupplierCategoryLinkResponse(value.getBusiness().getId(), value.getSupplier().getId(),
                value.getSupplierCategory().getId());
    }
}
