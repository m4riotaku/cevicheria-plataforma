package com.cevicheria.platform.service;

import java.util.List;
import com.cevicheria.platform.dto.PurchaseOrderItemRequest;
import com.cevicheria.platform.dto.PurchaseOrderItemResponse;

public interface PurchaseOrderItemService {
    List<PurchaseOrderItemResponse> getAll(Long businessId, Long branchId);
    PurchaseOrderItemResponse getById(Long businessId, Long id);
    PurchaseOrderItemResponse create(PurchaseOrderItemRequest request);
    PurchaseOrderItemResponse update(Long businessId, Long id, PurchaseOrderItemRequest request);
    void delete(Long businessId, Long id);
}
