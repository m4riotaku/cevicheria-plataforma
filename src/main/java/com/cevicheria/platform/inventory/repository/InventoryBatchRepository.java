package com.cevicheria.platform.inventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.InventoryBatch;

public interface InventoryBatchRepository extends JpaRepository<InventoryBatch, Long> {

    List<InventoryBatch> findByIngredientIdAndWarehouseIdAndStatusOrderByExpirationDateAscReceivedAtAsc(
            Long ingredientId, Long warehouseId, String status);
}