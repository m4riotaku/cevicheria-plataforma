package com.cevicheria.platform.service;

import java.util.List;
import com.cevicheria.platform.dto.SupplierRequest;
import com.cevicheria.platform.dto.SupplierResponse;

public interface SupplierService {
    List<SupplierResponse> getAll(Long businessId);
    SupplierResponse getById(Long businessId, Long id);
    SupplierResponse create(SupplierRequest request);
    SupplierResponse update(Long businessId, Long id, SupplierRequest request);
    void delete(Long businessId, Long id);
}
