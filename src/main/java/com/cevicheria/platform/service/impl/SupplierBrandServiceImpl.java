package com.cevicheria.platform.service.impl;
import com.cevicheria.platform.dto.SupplierBrandRequest;
import com.cevicheria.platform.dto.SupplierBrandResponse;
import com.cevicheria.platform.model.SupplierBrand;
import com.cevicheria.platform.repository.SupplierBrandRepository;
import com.cevicheria.platform.repository.BusinessRepository;
import com.cevicheria.platform.service.SupplierBrandService;
import com.cevicheria.platform.handlerException.BadRequestException;
import com.cevicheria.platform.handlerException.ResourceConflictException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SupplierBrandServiceImpl implements SupplierBrandService {
    private final SupplierBrandRepository repository;
    private final BusinessRepository businesses;

    public List<SupplierBrandResponse> getAll(Long businessId) {
        validateId(businessId);
        return repository.findByBusinessId(businessId).stream().map(this::response).toList();
    }

    public SupplierBrandResponse getById(Long businessId, Long id) {
        return response(find(businessId, id));
    }

    @Transactional
    public SupplierBrandResponse create(SupplierBrandRequest request) {
        if (request == null) throw new BadRequestException("Los datos son obligatorios");
        validateId(request.businessId());
        var business = businesses.findById(request.businessId())
                .orElseThrow(() -> new ResourceNotFoundException("Business", "ID", request.businessId()));
        var current = new SupplierBrand(business, request.name());
        validateUniqueName(current);
        return response(repository.saveAndFlush(current));
    }

    @Transactional
    public SupplierBrandResponse update(Long businessId, Long id, SupplierBrandRequest request) {
        if (request == null || request.name() == null) throw new BadRequestException("El nombre es obligatorio");
        var current = find(businessId, id);
        if (request.businessId() != null && !Objects.equals(businessId, request.businessId())) {
            throw new BadRequestException("El negocio no se puede modificar");
        }
        current.changeName(request.name());
        validateUniqueName(current);
        return response(repository.saveAndFlush(current));
    }

    @Transactional
    public void delete(Long businessId, Long id) {
        repository.delete(find(businessId, id));
        repository.flush();
    }

    private SupplierBrand find(Long businessId, Long id) {
        validateId(businessId);
        validateId(id);
        return repository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("SupplierBrand", "ID", id));
    }

    private void validateUniqueName(SupplierBrand current) {
        repository.findByBusinessIdAndName(current.getBusiness().getId(), current.getName())
                .filter(existing -> !Objects.equals(existing.getId(), current.getId()))
                .ifPresent(existing -> {
                    throw new ResourceConflictException("El nombre ya existe en este negocio");
                });
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) throw new BadRequestException("El ID debe ser positivo");
    }

    private SupplierBrandResponse response(SupplierBrand value) {
        return new SupplierBrandResponse(value.getId(), value.getBusiness().getId(), value.getName());
    }
}
