package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.InventoryBalance;

public interface InventoryBalanceRepository extends JpaRepository<InventoryBalance, Long> {

    List<InventoryBalance> findByWarehouseId(Long warehouseId);

    List<InventoryBalance> findByIngredientIdAndWarehouseId(Long ingredientId, Long warehouseId);
}