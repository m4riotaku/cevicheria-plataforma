package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.InventoryMovement;
import com.cevicheria.platform.repository.InventoryMovementRepository;
import com.cevicheria.platform.service.InventoryMovementService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryMovementServiceImpl implements InventoryMovementService {

    private final InventoryMovementRepository movementRepository;

    @Override
    public List<InventoryMovement> getAllByBusiness(Long businessId) {
        return movementRepository.findByBusinessIdOrderByRecordDateDesc(businessId);
    }

    @Override
    public List<InventoryMovement> getByOperation(Long businessId, Long operationId) {
        return movementRepository.findByBusinessIdAndOperationId(businessId, operationId);
    }

    @Override
    public List<InventoryMovement> getByBatch(Long businessId, Long batchId) {
        return movementRepository.findByBusinessIdAndBatchId(businessId, batchId);
    }

    @Override
    public InventoryMovement getById(Long id) {
        return movementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InventoryMovement", "ID", id));
    }

    @Override
    public InventoryMovement save(InventoryMovement movement) {
        return movementRepository.save(movement);
    }

    @Override
    public InventoryMovement update(Long id, InventoryMovement movement) {
        InventoryMovement current = getById(id);
        if (movement.getSequenceNumber() != null) {
            current.changeSequenceNumber(movement.getSequenceNumber());
        }
        if (movement.getQuantity() != null) {
            current.changeQuantity(movement.getQuantity());
        }
        if (movement.getUnitCost() != null) {
            current.changeUnitCost(movement.getUnitCost());
        }
        if (movement.getExpectedQuantity() != null) {
            current.changeExpectedQuantity(movement.getExpectedQuantity());
        }
        if (movement.getCountedQuantity() != null) {
            current.changeCountedQuantity(movement.getCountedQuantity());
        }
        if (movement.getConcept() != null) {
            current.changeConcept(movement.getConcept());
        }
        return movementRepository.save(current);
    }

    @Override
    public void deleteById(Long id) {
        movementRepository.delete(getById(id));
    }
}