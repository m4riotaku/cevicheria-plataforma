package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.InventoryBatch;
import com.cevicheria.platform.model.enums.InventoryBatchStatus;
import com.cevicheria.platform.repository.InventoryBatchRepository;
import com.cevicheria.platform.service.InventoryBatchService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryBatchServiceImpl implements InventoryBatchService {

    private final InventoryBatchRepository batchRepository;

    @Override
    public List<InventoryBatch> getAllByBusiness(Long businessId) {
        return batchRepository.findByBusinessId(businessId);
    }

    @Override
    public List<InventoryBatch> getByWarehouse(Long businessId, Long warehouseId) {
        return batchRepository.findByBusinessIdAndWarehouseId(businessId, warehouseId);
    }

    @Override
    public List<InventoryBatch> getByIngredient(Long businessId, Long ingredientId) {
        return batchRepository.findByBusinessIdAndIngredientId(businessId, ingredientId);
    }

    @Override
    public List<InventoryBatch> getByStatus(Long businessId, InventoryBatchStatus status) {
        return batchRepository.findByBusinessIdAndStatus(businessId, status);
    }

    @Override
    public InventoryBatch getById(Long id) {
        return batchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InventoryBatch", "ID", id));
    }

    @Override
    public InventoryBatch save(InventoryBatch batch) {
        return batchRepository.save(batch);
    }

    @Override
    public InventoryBatch update(Long id, InventoryBatch batch) {
        InventoryBatch current = getById(id);
        if (batch.getReceivedAt() != null) {
            current.changeReceivedAt(batch.getReceivedAt());
        }
        if (batch.getDueOn() != null) {
            current.changeDueOn(batch.getDueOn());
        }
        if (batch.getOnHandQuantity() != null) {
            current.changeOnHandQuantity(batch.getOnHandQuantity());
        }
        if (batch.getStatus() != null) {
            current.changeStatus(batch.getStatus());
        }
        if (batch.getBlockReason() != null) {
            current.changeBlockReason(batch.getBlockReason());
        }
        return batchRepository.save(current);
    }

    @Override
    public void deleteById(Long id) {
        batchRepository.delete(getById(id));
    }
}