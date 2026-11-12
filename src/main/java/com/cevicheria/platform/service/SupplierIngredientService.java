package com.cevicheria.platform.service;

import java.util.List;
import com.cevicheria.platform.dto.SupplierIngredientRequest;
import com.cevicheria.platform.dto.SupplierIngredientResponse;

public interface SupplierIngredientService {
    List<SupplierIngredientResponse> getAll(Long businessId);
    SupplierIngredientResponse getById(Long businessId, Long id);
    SupplierIngredientResponse create(SupplierIngredientRequest request);
    SupplierIngredientResponse update(Long businessId, Long id, SupplierIngredientRequest request);
    void delete(Long businessId, Long id);
}
