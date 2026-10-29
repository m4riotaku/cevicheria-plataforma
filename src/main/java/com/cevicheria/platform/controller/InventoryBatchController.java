package com.cevicheria.platform.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cevicheria.platform.model.InventoryBatch;
import com.cevicheria.platform.model.enums.InventoryBatchStatus;
import com.cevicheria.platform.service.InventoryBatchService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/inventory-batches")
@RequiredArgsConstructor
public class InventoryBatchController {
    private final InventoryBatchService batchService;

    @GetMapping
    public List<InventoryBatch> getAll(@RequestParam Long businessId) { return batchService.getAllByBusiness(businessId); }

    @GetMapping("/warehouse/{warehouseId}")
    public List<InventoryBatch> getByWarehouse(@RequestParam Long businessId, @PathVariable Long warehouseId) { return batchService.getByWarehouse(businessId, warehouseId); }

    @GetMapping("/ingredient/{ingredientId}")
    public List<InventoryBatch> getByIngredient(@RequestParam Long businessId, @PathVariable Long ingredientId) { return batchService.getByIngredient(businessId, ingredientId); }

    @GetMapping("/status/{status}")
    public List<InventoryBatch> getByStatus(@RequestParam Long businessId, @PathVariable InventoryBatchStatus status) { return batchService.getByStatus(businessId, status); }

    @GetMapping("/{id}")
    public InventoryBatch getById(@PathVariable Long id) { return batchService.getById(id); }

    @PostMapping
    public ResponseEntity<InventoryBatch> create(@RequestBody InventoryBatch batch) { return ResponseEntity.status(HttpStatus.CREATED).body(batchService.save(batch)); }

    @PutMapping("/{id}")
    public InventoryBatch update(@PathVariable Long id, @RequestBody InventoryBatch batch) { return batchService.update(id, batch); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { batchService.deleteById(id); return ResponseEntity.noContent().build(); }
}