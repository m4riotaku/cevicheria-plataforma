package com.cevicheria.platform.service;
import com.cevicheria.platform.dto.SupplierCategoryLinkResponse;
import java.util.List;

public interface SupplierCategoryLinkService {
    List<SupplierCategoryLinkResponse> getAll(Long businessId, Long supplierId);
    SupplierCategoryLinkResponse create(Long businessId, Long supplierId, Long targetId);
    void delete(Long businessId, Long supplierId, Long targetId);
}
