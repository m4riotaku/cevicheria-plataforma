package com.cevicheria.platform.service;

import java.util.List;
import com.cevicheria.platform.dto.PurchaseItemRequest;
import com.cevicheria.platform.dto.PurchaseItemResponse;

public interface PurchaseItemService {
    List<PurchaseItemResponse> getAll(Long businessId, Long branchId);
    PurchaseItemResponse getById(Long businessId, Long id);
    PurchaseItemResponse create(PurchaseItemRequest request);
    PurchaseItemResponse update(Long businessId, Long id, PurchaseItemRequest request);
    void delete(Long businessId, Long id);
}
