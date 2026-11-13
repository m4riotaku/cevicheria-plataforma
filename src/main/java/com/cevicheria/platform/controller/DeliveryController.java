package com.cevicheria.platform.controller;

import com.cevicheria.platform.model.enums.DeliveryStatus;

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
import com.cevicheria.platform.dto.DeliveryRequest;
import com.cevicheria.platform.dto.DeliveryResponse;
import com.cevicheria.platform.service.DeliveryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deliveries")
public class DeliveryController {
    private final DeliveryService service;

    @GetMapping("/filter/status")
    public List<DeliveryResponse> getByStatus(@RequestParam Long businessId, @RequestParam Long branchId,
                                              @RequestParam DeliveryStatus status) {
        return service.getByStatus(businessId, branchId, status);
    }

    @GetMapping
    public List<DeliveryResponse> getAll(@RequestParam Long businessId, @RequestParam Long branchId) {
        return service.getAll(businessId, branchId);
    }

    @GetMapping("/{id}")
    public DeliveryResponse getById(@RequestParam Long businessId, @PathVariable Long id) {
        return service.getById(businessId, id);
    }

    @PostMapping
    public ResponseEntity<DeliveryResponse> create(@RequestBody DeliveryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public DeliveryResponse update(@RequestParam Long businessId, @PathVariable Long id,
                                   @RequestBody DeliveryRequest request) {
        return service.update(businessId, id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestParam Long businessId, @PathVariable Long id) {
        service.delete(businessId, id);
        return ResponseEntity.noContent().build();
    }
}
