package com.cevicheria.platform.controller;

import com.cevicheria.platform.model.enums.PurchaseOrderStatus;

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
import com.cevicheria.platform.dto.PurchaseOrderRequest;
import com.cevicheria.platform.dto.PurchaseOrderResponse;
import com.cevicheria.platform.service.PurchaseOrderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/purchase-orders")
public class PurchaseOrderController {
    private final PurchaseOrderService service;

    @GetMapping("/filter/status")
    public List<PurchaseOrderResponse> getByStatus(@RequestParam Long businessId, @RequestParam Long branchId,
                                                   @RequestParam PurchaseOrderStatus status) {
        return service.getByStatus(businessId, branchId, status);
    }

    @GetMapping
    public List<PurchaseOrderResponse> getAll(@RequestParam Long businessId, @RequestParam Long branchId) {
        return service.getAll(businessId, branchId);
    }

    @GetMapping("/{id}")
    public PurchaseOrderResponse getById(@RequestParam Long businessId, @PathVariable Long id) {
        return service.getById(businessId, id);
    }

    @PostMapping
    public ResponseEntity<PurchaseOrderResponse> create(@RequestBody PurchaseOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public PurchaseOrderResponse update(@RequestParam Long businessId, @PathVariable Long id,
                                        @RequestBody PurchaseOrderRequest request) {
        return service.update(businessId, id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestParam Long businessId, @PathVariable Long id) {
        service.delete(businessId, id);
        return ResponseEntity.noContent().build();
    }
}
