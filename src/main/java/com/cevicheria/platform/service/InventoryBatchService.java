package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.InventoryBatch;
import com.cevicheria.platform.model.enums.InventoryBatchStatus;

public interface InventoryBatchService {

    List<InventoryBatch> getAllByBusiness(Long businessId);

    List<InventoryBatch> getByWarehouse(Long businessId, Long warehouseId);

    List<InventoryBatch> getByIngredient(Long businessId, Long ingredientId);

    List<InventoryBatch> getByStatus(Long businessId, InventoryBatchStatus status);

    InventoryBatch getById(Long id);

    InventoryBatch save(InventoryBatch batch);

    InventoryBatch update(Long id, InventoryBatch batch);

    void deleteById(Long id);
}