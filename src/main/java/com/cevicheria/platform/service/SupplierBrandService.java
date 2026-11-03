package com.cevicheria.platform.service;
import com.cevicheria.platform.dto.SupplierBrandRequest;
import com.cevicheria.platform.dto.SupplierBrandResponse;
import java.util.List;

public interface SupplierBrandService {
    List<SupplierBrandResponse> getAll(Long businessId);
    SupplierBrandResponse getById(Long businessId, Long id);
    SupplierBrandResponse create(SupplierBrandRequest request);
    SupplierBrandResponse update(Long businessId, Long id, SupplierBrandRequest request);
    void delete(Long businessId, Long id);
}
