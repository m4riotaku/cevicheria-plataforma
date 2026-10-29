package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.InventoryBalance;

public interface InventoryBalanceRepository extends JpaRepository<InventoryBalance, Long> {

    List<InventoryBalance> findByBusinessId(Long businessId);

    List<InventoryBalance> findByBusinessIdAndBranchId(Long businessId, Long branchId);

    List<InventoryBalance> findByBusinessIdAndWarehouseId(Long businessId, Long warehouseId);

    List<InventoryBalance> findByWarehouseId(Long warehouseId);

    List<InventoryBalance> findByIngredientIdAndWarehouseId(Long ingredientId, Long warehouseId);

    java.util.Optional<InventoryBalance> findByBusinessIdAndWarehouseIdAndIngredientId(
            Long businessId, Long warehouseId, Long ingredientId);
}