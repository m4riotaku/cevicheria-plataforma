package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.InventoryMovement;

public interface InventoryMovementService {

    List<InventoryMovement> getAllByBusiness(Long businessId);

    List<InventoryMovement> getByOperation(Long businessId, Long operationId);

    List<InventoryMovement> getByBatch(Long businessId, Long batchId);

    InventoryMovement getById(Long id);

    InventoryMovement save(InventoryMovement movement);

    InventoryMovement update(Long id, InventoryMovement movement);

    void deleteById(Long id);
}