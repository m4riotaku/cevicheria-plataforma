package com.cevicheria.platform.service;

import java.util.List;
import java.util.Optional;

import com.cevicheria.platform.model.InventoryBalance;

public interface InventoryBalanceService {

    List<InventoryBalance> getAllByBusiness(Long businessId);

    List<InventoryBalance> getByBranch(Long businessId, Long branchId);

    List<InventoryBalance> getByWarehouse(Long businessId, Long warehouseId);

    Optional<InventoryBalance> getByWarehouseAndIngredient(Long businessId, Long warehouseId, Long ingredientId);

    InventoryBalance getById(Long id);

    InventoryBalance save(InventoryBalance balance);

    InventoryBalance update(Long id, InventoryBalance balance);

    void deleteById(Long id);
}