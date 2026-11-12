package com.cevicheria.platform.service;

import java.util.List;
import com.cevicheria.platform.dto.PurchaseInstallmentRequest;
import com.cevicheria.platform.dto.PurchaseInstallmentResponse;

public interface PurchaseInstallmentService {
    List<PurchaseInstallmentResponse> getAll(Long businessId, Long branchId);
    PurchaseInstallmentResponse getById(Long businessId, Long id);
    PurchaseInstallmentResponse create(PurchaseInstallmentRequest request);
    PurchaseInstallmentResponse update(Long businessId, Long id, PurchaseInstallmentRequest request);
    void delete(Long businessId, Long id);
}
