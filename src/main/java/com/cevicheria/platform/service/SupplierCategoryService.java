package com.cevicheria.platform.service;
import com.cevicheria.platform.dto.SupplierCategoryRequest;
import com.cevicheria.platform.dto.SupplierCategoryResponse;
import java.util.List;

public interface SupplierCategoryService {
    List<SupplierCategoryResponse> getAll(Long businessId);
    SupplierCategoryResponse getById(Long businessId, Long id);
    SupplierCategoryResponse create(SupplierCategoryRequest request);
    SupplierCategoryResponse update(Long businessId, Long id, SupplierCategoryRequest request);
    void delete(Long businessId, Long id);
}
