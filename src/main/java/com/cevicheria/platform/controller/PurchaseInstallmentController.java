package com.cevicheria.platform.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import com.cevicheria.platform.dto.PurchaseInstallmentRequest;
import com.cevicheria.platform.dto.PurchaseInstallmentResponse;
import com.cevicheria.platform.service.PurchaseInstallmentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/purchase-installments")
public class PurchaseInstallmentController {
    private final PurchaseInstallmentService service;

    @GetMapping
    public List<PurchaseInstallmentResponse> getAll(@RequestParam Long businessId, @RequestParam Long branchId) {
        return service.getAll(businessId, branchId);
    }

    @GetMapping("/{id}")
    public PurchaseInstallmentResponse getById(@RequestParam Long businessId, @PathVariable Long id) {
        return service.getById(businessId, id);
    }

    @PostMapping
    public ResponseEntity<PurchaseInstallmentResponse> create(@RequestBody PurchaseInstallmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PatchMapping("/{id}")
    public PurchaseInstallmentResponse update(@RequestParam Long businessId, @PathVariable Long id,
                                 @RequestBody PurchaseInstallmentRequest request) {
        return service.update(businessId, id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestParam Long businessId, @PathVariable Long id) {
        service.delete(businessId, id);
        return ResponseEntity.noContent().build();
    }
}
