package com.cevicheria.platform.service;

import com.cevicheria.platform.model.enums.DeliveryStatus;

import java.util.List;
import com.cevicheria.platform.dto.DeliveryRequest;
import com.cevicheria.platform.dto.DeliveryResponse;

public interface DeliveryService {
    List<DeliveryResponse> getByStatus(Long businessId, Long branchId, DeliveryStatus status);
    List<DeliveryResponse> getAll(Long businessId, Long branchId);
    DeliveryResponse getById(Long businessId, Long id);
    DeliveryResponse create(DeliveryRequest request);
    DeliveryResponse update(Long businessId, Long id, DeliveryRequest request);
    void delete(Long businessId, Long id);
}
