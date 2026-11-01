package com.cevicheria.platform.service.impl;
import com.cevicheria.platform.dto.ReservationCancellationTermsRequest;
import com.cevicheria.platform.dto.ReservationCancellationTermsResponse;
import com.cevicheria.platform.model.Reservation;
import com.cevicheria.platform.model.ReservationCancellationTerms;
import com.cevicheria.platform.model.id.ReservationCancellationTermsId;
import com.cevicheria.platform.repository.ReservationCancellationTermsRepository;
import com.cevicheria.platform.repository.ReservationRepository;
import com.cevicheria.platform.service.ReservationCancellationTermsService;
import com.cevicheria.platform.handlerException.BadRequestException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationCancellationTermsServiceImpl implements ReservationCancellationTermsService {
    private final ReservationCancellationTermsRepository repository;
    private final ReservationRepository reservations;

    public ReservationCancellationTermsResponse get(Long businessId, Long branchId, Long reservationId) {
        var reservation = reservation(businessId, branchId, reservationId);
        return response(repository.findById(key(reservation))
                .orElseThrow(() -> new ResourceNotFoundException("ReservationCancellationTerms", "reservationId", reservationId)));
    }

    @Transactional
    public ReservationCancellationTermsResponse replace(Long businessId, Long branchId, Long reservationId,
            ReservationCancellationTermsRequest request) {
        if (request == null) throw new BadRequestException("Los datos son obligatorios");
        var reservation = reservation(businessId, branchId, reservationId);
        var current = repository.findById(key(reservation)).orElseGet(() -> new ReservationCancellationTerms(
                reservation.getBusiness(), reservation.getBranch(), reservation, null, null, null));
        current.changeFullRefundBeforeHours(request.fullRefundBeforeHours());
        current.changeNoShowRetentionAmount(request.noShowRetentionAmount());
        current.changePolicyNotes(request.policyNotes());
        return response(repository.saveAndFlush(current));
    }

    @Transactional
    public void delete(Long businessId, Long branchId, Long reservationId) {
        var reservation = reservation(businessId, branchId, reservationId);
        var current = repository.findById(key(reservation))
                .orElseThrow(() -> new ResourceNotFoundException("ReservationCancellationTerms", "reservationId", reservationId));
        repository.delete(current);
        repository.flush();
    }

    private Reservation reservation(Long businessId, Long branchId, Long reservationId) {
        if (businessId == null || businessId <= 0 || branchId == null || branchId <= 0
                || reservationId == null || reservationId <= 0) throw new BadRequestException("Los IDs deben ser positivos");
        return reservations.findByIdAndBusinessId(reservationId, businessId)
                .filter(value -> Objects.equals(value.getBranch().getId(), branchId))
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "ID", reservationId));
    }

    private ReservationCancellationTermsId key(Reservation reservation) {
        return new ReservationCancellationTermsId(reservation.getBusiness().getId(),
                reservation.getBranch().getId(), reservation.getId());
    }

    private ReservationCancellationTermsResponse response(ReservationCancellationTerms value) {
        return new ReservationCancellationTermsResponse(value.getBusiness().getId(), value.getBranch().getId(),
                value.getReservation().getId(), value.getFullRefundBeforeHours(),
                value.getNoShowRetentionAmount(), value.getPolicyNotes());
    }
}
