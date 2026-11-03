package com.cevicheria.platform.service;
import com.cevicheria.platform.dto.SupplierBrandLinkResponse;
import java.util.List;

public interface SupplierBrandLinkService {
    List<SupplierBrandLinkResponse> getAll(Long businessId, Long supplierId);
    SupplierBrandLinkResponse create(Long businessId, Long supplierId, Long targetId);
    void delete(Long businessId, Long supplierId, Long targetId);
}
