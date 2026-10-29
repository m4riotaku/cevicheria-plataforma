package com.cevicheria.platform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.InventoryBatch;
import com.cevicheria.platform.model.enums.InventoryBatchStatus;

public interface InventoryBatchRepository extends JpaRepository<InventoryBatch, Long>
{
	List<InventoryBatch> findByBusinessId(Long businessId);

	List<InventoryBatch> findByBusinessIdAndWarehouseId(Long businessId, Long warehouseId);

	List<InventoryBatch> findByBusinessIdAndIngredientId(Long businessId, Long ingredientId);

	List<InventoryBatch> findByBusinessIdAndStatus(Long businessId, InventoryBatchStatus status);

	Optional<InventoryBatch> findByBusinessIdAndCode(Long businessId, String code);
}