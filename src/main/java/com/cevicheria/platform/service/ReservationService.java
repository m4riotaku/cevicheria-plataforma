package com.cevicheria.platform.service;

import com.cevicheria.platform.model.enums.ReservationStatus;

import java.util.List;
import com.cevicheria.platform.dto.ReservationRequest;
import com.cevicheria.platform.dto.ReservationResponse;

public interface ReservationService {
    List<ReservationResponse> getByStatus(Long businessId, Long branchId, ReservationStatus status);
    List<ReservationResponse> getAll(Long businessId, Long branchId);
    ReservationResponse getById(Long businessId, Long id);
    ReservationResponse create(ReservationRequest request);
    ReservationResponse update(Long businessId, Long id, ReservationRequest request);
    void delete(Long businessId, Long id);
}
