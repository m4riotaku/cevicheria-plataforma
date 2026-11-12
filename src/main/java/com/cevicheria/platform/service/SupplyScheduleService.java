package com.cevicheria.platform.service;

import java.util.List;
import com.cevicheria.platform.dto.SupplyScheduleRequest;
import com.cevicheria.platform.dto.SupplyScheduleResponse;

public interface SupplyScheduleService {
    List<SupplyScheduleResponse> getAll(Long businessId, Long branchId);
    SupplyScheduleResponse getById(Long businessId, Long id);
    SupplyScheduleResponse create(SupplyScheduleRequest request);
    SupplyScheduleResponse update(Long businessId, Long id, SupplyScheduleRequest request);
    void delete(Long businessId, Long id);
}
