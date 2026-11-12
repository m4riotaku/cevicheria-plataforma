package com.cevicheria.platform.service;

import com.cevicheria.platform.model.enums.PurchaseOrderStatus;

import java.util.List;
import com.cevicheria.platform.dto.PurchaseOrderRequest;
import com.cevicheria.platform.dto.PurchaseOrderResponse;

public interface PurchaseOrderService {
    List<PurchaseOrderResponse> getByStatus(Long businessId, Long branchId, PurchaseOrderStatus status);
    List<PurchaseOrderResponse> getAll(Long businessId, Long branchId);
    PurchaseOrderResponse getById(Long businessId, Long id);
    PurchaseOrderResponse create(PurchaseOrderRequest request);
    PurchaseOrderResponse update(Long businessId, Long id, PurchaseOrderRequest request);
    void delete(Long businessId, Long id);
}
