package com.cevicheria.platform.service;
import com.cevicheria.platform.dto.ReservationCancellationTermsRequest;
import com.cevicheria.platform.dto.ReservationCancellationTermsResponse;

public interface ReservationCancellationTermsService {
    ReservationCancellationTermsResponse get(Long businessId, Long branchId, Long reservationId);
    ReservationCancellationTermsResponse replace(Long businessId, Long branchId, Long reservationId,
            ReservationCancellationTermsRequest request);
    void delete(Long businessId, Long branchId, Long reservationId);
}
