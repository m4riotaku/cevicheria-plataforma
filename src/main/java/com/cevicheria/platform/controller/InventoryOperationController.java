package com.cevicheria.platform.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cevicheria.platform.model.InventoryOperation;
import com.cevicheria.platform.model.enums.InventoryOperationStatus;
import com.cevicheria.platform.service.InventoryOperationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/inventory-operations")
@RequiredArgsConstructor
public class InventoryOperationController {
    private final InventoryOperationService operationService;

    @GetMapping
    public List<InventoryOperation> getAll(@RequestParam Long businessId) { return operationService.getAllByBusiness(businessId); }

    @GetMapping("/branch/{branchId}")
    public List<InventoryOperation> getByBranch(@RequestParam Long businessId, @PathVariable Long branchId) { return operationService.getByBranch(businessId, branchId); }

    @GetMapping("/status/{status}")
    public List<InventoryOperation> getByStatus(@RequestParam Long businessId, @PathVariable InventoryOperationStatus status) { return operationService.getByStatus(businessId, status); }

    @GetMapping("/{id}")
    public InventoryOperation getById(@PathVariable Long id) { return operationService.getById(id); }

    @PostMapping
    public ResponseEntity<InventoryOperation> create(@RequestBody InventoryOperation operation) { return ResponseEntity.status(HttpStatus.CREATED).body(operationService.save(operation)); }

    @PutMapping("/{id}")
    public InventoryOperation update(@PathVariable Long id, @RequestBody InventoryOperation operation) { return operationService.update(id, operation); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { operationService.deleteById(id); return ResponseEntity.noContent().build(); }
}