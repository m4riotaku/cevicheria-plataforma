package com.cevicheria.platform.service;

import java.util.List;
import com.cevicheria.platform.dto.DeliveryZoneRequest;
import com.cevicheria.platform.dto.DeliveryZoneResponse;

public interface DeliveryZoneService {
    List<DeliveryZoneResponse> getAll(Long businessId, Long branchId);
    DeliveryZoneResponse getById(Long businessId, Long id);
    DeliveryZoneResponse create(DeliveryZoneRequest request);
    DeliveryZoneResponse update(Long businessId, Long id, DeliveryZoneRequest request);
    void delete(Long businessId, Long id);
}
