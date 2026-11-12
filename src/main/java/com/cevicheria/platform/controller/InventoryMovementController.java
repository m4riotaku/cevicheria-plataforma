package com.cevicheria.platform.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cevicheria.platform.model.InventoryMovement;
import com.cevicheria.platform.service.InventoryMovementService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/inventory-movements")
@RequiredArgsConstructor
public class InventoryMovementController {
    private final InventoryMovementService movementService;

    @GetMapping
    public List<InventoryMovement> getAll(@RequestParam Long businessId) { return movementService.getAllByBusiness(businessId); }

    @GetMapping("/operation/{operationId}")
    public List<InventoryMovement> getByOperation(@RequestParam Long businessId, @PathVariable Long operationId) { return movementService.getByOperation(businessId, operationId); }

    @GetMapping("/batch/{batchId}")
    public List<InventoryMovement> getByBatch(@RequestParam Long businessId, @PathVariable Long batchId) { return movementService.getByBatch(businessId, batchId); }

    @GetMapping("/{id}")
    public InventoryMovement getById(@PathVariable Long id) { return movementService.getById(id); }

    @PostMapping
    public ResponseEntity<InventoryMovement> create(@RequestBody InventoryMovement movement) { return ResponseEntity.status(HttpStatus.CREATED).body(movementService.save(movement)); }

    @PutMapping("/{id}")
    public InventoryMovement update(@PathVariable Long id, @RequestBody InventoryMovement movement) { return movementService.update(id, movement); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { movementService.deleteById(id); return ResponseEntity.noContent().build(); }
}