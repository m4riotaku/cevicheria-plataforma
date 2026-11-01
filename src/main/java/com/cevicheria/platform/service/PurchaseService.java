package com.cevicheria.platform.service;

import com.cevicheria.platform.model.enums.PurchaseStatus;

import java.util.List;
import com.cevicheria.platform.dto.PurchaseRequest;
import com.cevicheria.platform.dto.PurchaseResponse;

public interface PurchaseService {
    List<PurchaseResponse> getByStatus(Long businessId, Long branchId, PurchaseStatus status);
    List<PurchaseResponse> getAll(Long businessId, Long branchId);
    PurchaseResponse getById(Long businessId, Long id);
    PurchaseResponse create(PurchaseRequest request);
    PurchaseResponse update(Long businessId, Long id, PurchaseRequest request);
    void delete(Long businessId, Long id);
}
