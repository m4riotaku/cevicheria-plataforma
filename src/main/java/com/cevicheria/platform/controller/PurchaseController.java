package com.cevicheria.platform.controller;

import com.cevicheria.platform.model.enums.PurchaseStatus;

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
import com.cevicheria.platform.dto.PurchaseRequest;
import com.cevicheria.platform.dto.PurchaseResponse;
import com.cevicheria.platform.service.PurchaseService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/purchases")
public class PurchaseController {
    private final PurchaseService service;

    @GetMapping("/filter/status")
    public List<PurchaseResponse> getByStatus(@RequestParam Long businessId, @RequestParam Long branchId,
                                              @RequestParam PurchaseStatus status) {
        return service.getByStatus(businessId, branchId, status);
    }

    @GetMapping
    public List<PurchaseResponse> getAll(@RequestParam Long businessId, @RequestParam Long branchId) {
        return service.getAll(businessId, branchId);
    }

    @GetMapping("/{id}")
    public PurchaseResponse getById(@RequestParam Long businessId, @PathVariable Long id) {
        return service.getById(businessId, id);
    }

    @PostMapping
    public ResponseEntity<PurchaseResponse> create(@RequestBody PurchaseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public PurchaseResponse update(@RequestParam Long businessId, @PathVariable Long id,
                                   @RequestBody PurchaseRequest request) {
        return service.update(businessId, id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestParam Long businessId, @PathVariable Long id) {
        service.delete(businessId, id);
        return ResponseEntity.noContent().build();
    }
}
