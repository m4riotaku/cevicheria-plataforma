package com.cevicheria.platform.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cevicheria.platform.model.InventoryBalance;
import com.cevicheria.platform.service.InventoryBalanceService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/inventory-balances")
@RequiredArgsConstructor
public class InventoryBalanceController {
    private final InventoryBalanceService balanceService;

    @GetMapping
    public List<InventoryBalance> getAll(@RequestParam Long businessId) { return balanceService.getAllByBusiness(businessId); }

    @GetMapping("/branch/{branchId}")
    public List<InventoryBalance> getByBranch(@RequestParam Long businessId, @PathVariable Long branchId) { return balanceService.getByBranch(businessId, branchId); }

    @GetMapping("/warehouse/{warehouseId}")
    public List<InventoryBalance> getByWarehouse(@RequestParam Long businessId, @PathVariable Long warehouseId) { return balanceService.getByWarehouse(businessId, warehouseId); }

    @GetMapping("/warehouse/{warehouseId}/ingredient/{ingredientId}")
    public ResponseEntity<InventoryBalance> getByWarehouseAndIngredient(@RequestParam Long businessId, @PathVariable Long warehouseId, @PathVariable Long ingredientId) {
        return ResponseEntity.of(balanceService.getByWarehouseAndIngredient(businessId, warehouseId, ingredientId));
    }

    @GetMapping("/{id}")
    public InventoryBalance getById(@PathVariable Long id) { return balanceService.getById(id); }

    @PostMapping
    public ResponseEntity<InventoryBalance> create(@RequestBody InventoryBalance balance) { return ResponseEntity.status(HttpStatus.CREATED).body(balanceService.save(balance)); }

    @PutMapping("/{id}")
    public InventoryBalance update(@PathVariable Long id, @RequestBody InventoryBalance balance) { return balanceService.update(id, balance); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { balanceService.deleteById(id); return ResponseEntity.noContent().build(); }
}