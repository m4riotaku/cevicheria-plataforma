package com.cevicheria.platform.service.impl;
import com.cevicheria.platform.dto.SupplierCategoryRequest;
import com.cevicheria.platform.dto.SupplierCategoryResponse;
import com.cevicheria.platform.model.SupplierCategory;
import com.cevicheria.platform.repository.SupplierCategoryRepository;
import com.cevicheria.platform.repository.BusinessRepository;
import com.cevicheria.platform.service.SupplierCategoryService;
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
public class SupplierCategoryServiceImpl implements SupplierCategoryService {
    private final SupplierCategoryRepository repository;
    private final BusinessRepository businesses;

    public List<SupplierCategoryResponse> getAll(Long businessId) {
        validateId(businessId);
        return repository.findByBusinessId(businessId).stream().map(this::response).toList();
    }

    public SupplierCategoryResponse getById(Long businessId, Long id) {
        return response(find(businessId, id));
    }

    @Transactional
    public SupplierCategoryResponse create(SupplierCategoryRequest request) {
        if (request == null) throw new BadRequestException("Los datos son obligatorios");
        validateId(request.businessId());
        var business = businesses.findById(request.businessId())
                .orElseThrow(() -> new ResourceNotFoundException("Business", "ID", request.businessId()));
        var current = new SupplierCategory(business, request.name());
        validateUniqueName(current);
        return response(repository.saveAndFlush(current));
    }

    @Transactional
    public SupplierCategoryResponse update(Long businessId, Long id, SupplierCategoryRequest request) {
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

    private SupplierCategory find(Long businessId, Long id) {
        validateId(businessId);
        validateId(id);
        return repository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("SupplierCategory", "ID", id));
    }

    private void validateUniqueName(SupplierCategory current) {
        repository.findByBusinessIdAndName(current.getBusiness().getId(), current.getName())
                .filter(existing -> !Objects.equals(existing.getId(), current.getId()))
                .ifPresent(existing -> {
                    throw new ResourceConflictException("El nombre ya existe en este negocio");
                });
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) throw new BadRequestException("El ID debe ser positivo");
    }

    private SupplierCategoryResponse response(SupplierCategory value) {
        return new SupplierCategoryResponse(value.getId(), value.getBusiness().getId(), value.getName());
    }
}
