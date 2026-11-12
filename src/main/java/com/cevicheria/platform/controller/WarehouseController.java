package com.cevicheria.platform.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cevicheria.platform.model.Warehouse;
import com.cevicheria.platform.service.WarehouseService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/warehouses")
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;

    @GetMapping
    public List<Warehouse> getAll(@RequestParam Long businessId) { return warehouseService.getAllByBusiness(businessId); }

    @GetMapping("/active")
    public List<Warehouse> getActive(@RequestParam Long businessId) { return warehouseService.getActiveByBusiness(businessId); }

    @GetMapping("/branch/{branchId}")
    public List<Warehouse> getByBranch(@RequestParam Long businessId, @PathVariable Long branchId) { return warehouseService.getByBranch(businessId, branchId); }

    @GetMapping("/branch/{branchId}/active")
    public List<Warehouse> getActiveByBranch(@PathVariable Long branchId) { return warehouseService.getActiveByBranch(branchId); }

    @GetMapping("/{id}")
    public Warehouse getById(@PathVariable Long id) { return warehouseService.getById(id); }

    @PostMapping
    public ResponseEntity<Warehouse> create(@RequestBody Warehouse warehouse) { return ResponseEntity.status(HttpStatus.CREATED).body(warehouseService.save(warehouse)); }

    @PutMapping("/{id}")
    public Warehouse update(@PathVariable Long id, @RequestBody Warehouse warehouse) { return warehouseService.update(id, warehouse); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { warehouseService.deleteById(id); return ResponseEntity.noContent().build(); }
}