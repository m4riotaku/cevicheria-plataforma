package com.cevicheria.platform.service.impl;
import com.cevicheria.platform.dto.SupplierBrandLinkResponse;
import com.cevicheria.platform.model.SupplierBrandLink;
import com.cevicheria.platform.model.Supplier;
import com.cevicheria.platform.model.id.SupplierBrandLinkId;
import com.cevicheria.platform.repository.SupplierBrandLinkRepository;
import com.cevicheria.platform.repository.SupplierBrandRepository;
import com.cevicheria.platform.repository.SupplierRepository;
import com.cevicheria.platform.service.SupplierBrandLinkService;
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
public class SupplierBrandLinkServiceImpl implements SupplierBrandLinkService {
    private final SupplierBrandLinkRepository repository;
    private final SupplierRepository suppliers;
    private final SupplierBrandRepository targets;

    public List<SupplierBrandLinkResponse> getAll(Long businessId, Long supplierId) {
        supplier(businessId, supplierId);
        return repository.findByBusinessIdAndSupplierId(businessId, supplierId).stream().map(this::response).toList();
    }

    @Transactional
    public SupplierBrandLinkResponse create(Long businessId, Long supplierId, Long targetId) {
        var supplier = supplier(businessId, supplierId);
        validateId(targetId);
        var target = targets.findByIdAndBusinessId(targetId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("SupplierBrand", "ID", targetId));
        var key = new SupplierBrandLinkId(businessId, supplierId, targetId);
        if (repository.existsById(key)) throw new ResourceConflictException("La asociación ya existe");
        return response(repository.saveAndFlush(new SupplierBrandLink(supplier.getBusiness(), supplier, target)));
    }

    @Transactional
    public void delete(Long businessId, Long supplierId, Long targetId) {
        supplier(businessId, supplierId);
        validateId(targetId);
        var key = new SupplierBrandLinkId(businessId, supplierId, targetId);
        var current = repository.findById(key)
                .orElseThrow(() -> new ResourceNotFoundException("SupplierBrandLink", "ID", targetId));
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

    private SupplierBrandLinkResponse response(SupplierBrandLink value) {
        return new SupplierBrandLinkResponse(value.getBusiness().getId(), value.getSupplier().getId(),
                value.getSupplierBrand().getId());
    }
}
