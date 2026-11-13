package com.cevicheria.platform.controller;

import com.cevicheria.platform.model.enums.ReservationStatus;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import com.cevicheria.platform.dto.ReservationRequest;
import com.cevicheria.platform.dto.ReservationResponse;
import com.cevicheria.platform.service.ReservationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService service;

    @GetMapping("/filter/status")
    public List<ReservationResponse> getByStatus(@RequestParam Long businessId, @RequestParam Long branchId,
                                                 @RequestParam ReservationStatus status) {
        return service.getByStatus(businessId, branchId, status);
    }

    @GetMapping
    public List<ReservationResponse> getAll(@RequestParam Long businessId, @RequestParam Long branchId) {
        return service.getAll(businessId, branchId);
    }

    @GetMapping("/{id}")
    public ReservationResponse getById(@RequestParam Long businessId, @PathVariable Long id) {
        return service.getById(businessId, id);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@RequestBody ReservationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ReservationResponse update(@RequestParam Long businessId, @PathVariable Long id,
                                      @RequestBody ReservationRequest request) {
        return service.update(businessId, id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestParam Long businessId, @PathVariable Long id) {
        service.delete(businessId, id);
        return ResponseEntity.noContent().build();
    }
}
