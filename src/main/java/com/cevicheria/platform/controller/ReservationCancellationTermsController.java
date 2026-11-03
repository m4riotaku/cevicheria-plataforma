package com.cevicheria.platform.controller;
import com.cevicheria.platform.dto.ReservationCancellationTermsRequest;
import com.cevicheria.platform.dto.ReservationCancellationTermsResponse;
import com.cevicheria.platform.service.ReservationCancellationTermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations/{reservationId}/cancellation-terms")
public class ReservationCancellationTermsController {
    private final ReservationCancellationTermsService service;

    @GetMapping
    public ReservationCancellationTermsResponse get(@RequestParam Long businessId,
            @RequestParam Long branchId, @PathVariable Long reservationId) {
        return service.get(businessId, branchId, reservationId);
    }

    @PutMapping
    public ReservationCancellationTermsResponse replace(@RequestParam Long businessId,
            @RequestParam Long branchId, @PathVariable Long reservationId,
            @RequestBody ReservationCancellationTermsRequest request) {
        return service.replace(businessId, branchId, reservationId, request);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam Long businessId,
            @RequestParam Long branchId, @PathVariable Long reservationId) {
        service.delete(businessId, branchId, reservationId);
        return ResponseEntity.noContent().build();
    }
}
