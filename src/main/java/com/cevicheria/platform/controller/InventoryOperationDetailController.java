package com.cevicheria.platform.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cevicheria.platform.model.InventoryOperationDetail;
import com.cevicheria.platform.model.id.InventoryOperationDetailId;
import com.cevicheria.platform.service.InventoryOperationDetailService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/inventory-operation-details")
@RequiredArgsConstructor
public class InventoryOperationDetailController {
    private final InventoryOperationDetailService detailService;

    @GetMapping
    public List<InventoryOperationDetail> getAll(@RequestParam Long businessId) { return detailService.getAllByBusiness(businessId); }

    @GetMapping("/operation/{operationId}")
    public List<InventoryOperationDetail> getByOperation(@RequestParam Long businessId, @PathVariable Long operationId) { return detailService.getByOperation(businessId, operationId); }

    @GetMapping("/{branchId}/{operationId}")
    public InventoryOperationDetail getById(@PathVariable Long businessId, @PathVariable Long branchId, @PathVariable Long operationId) {
        return detailService.getById(new InventoryOperationDetailId(businessId, branchId, operationId));
    }

    @PostMapping
    public ResponseEntity<InventoryOperationDetail> create(@RequestBody InventoryOperationDetail detail) { return ResponseEntity.status(HttpStatus.CREATED).body(detailService.save(detail)); }

    @DeleteMapping("/{branchId}/{operationId}")
    public ResponseEntity<Void> delete(@PathVariable Long businessId, @PathVariable Long branchId, @PathVariable Long operationId) {
        detailService.deleteById(new InventoryOperationDetailId(businessId, branchId, operationId));
        return ResponseEntity.noContent().build();
    }
}