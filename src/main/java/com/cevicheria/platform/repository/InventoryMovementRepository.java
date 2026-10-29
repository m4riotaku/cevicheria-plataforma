package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.InventoryMovement;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long>
{
    List<InventoryMovement> findByBusinessIdOrderByRecordDateDesc(Long businessId);

    List<InventoryMovement> findByOperationId(Long operationId);

    List<InventoryMovement> findByBusinessIdAndOperationId(Long businessId, Long operationId);

    List<InventoryMovement> findByBusinessIdAndBatchId(Long businessId, Long batchId);
}