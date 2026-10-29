package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.InventoryOperation;
import com.cevicheria.platform.model.enums.InventoryOperationStatus;
import com.cevicheria.platform.repository.InventoryOperationRepository;
import com.cevicheria.platform.service.InventoryOperationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryOperationServiceImpl implements InventoryOperationService {

    private final InventoryOperationRepository operationRepository;

    @Override
    public List<InventoryOperation> getAllByBusiness(Long businessId) {
        return operationRepository.findByBusinessIdOrderByCreatedAtDesc(businessId);
    }

    @Override
    public List<InventoryOperation> getByBranch(Long businessId, Long branchId) {
        return operationRepository.findByBusinessIdAndBranchIdOrderByCreatedAtDesc(businessId, branchId);
    }

    @Override
    public List<InventoryOperation> getByStatus(Long businessId, InventoryOperationStatus status) {
        return operationRepository.findByBusinessIdAndStatusOrderByCreatedAtDesc(businessId, status);
    }

    @Override
    public InventoryOperation getById(Long id) {
        return operationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InventoryOperation", "ID", id));
    }

    @Override
    public InventoryOperation save(InventoryOperation operation) {
        return operationRepository.save(operation);
    }

    @Override
    public InventoryOperation update(Long id, InventoryOperation operation) {
        InventoryOperation current = getById(id);
        if (operation.getType() != null) {
            current.changeType(operation.getType());
        }
        if (operation.getOperationKey() != null) {
            current.changeOperationKey(operation.getOperationKey());
        }
        if (operation.getAuthorizedByUserBusiness() != null) {
            current.changeAuthorizedByUserBusiness(operation.getAuthorizedByUserBusiness());
        }
        if (operation.getDestinationBranch() != null) {
            current.changeDestinationBranch(operation.getDestinationBranch());
        }
        if (operation.getCutoffAt() != null) {
            current.changeCutoffAt(operation.getCutoffAt());
        }
        if (operation.getStatus() != null) {
            current.changeStatus(operation.getStatus());
        }
        if (operation.getReturnResolution() != null) {
            current.changeReturnResolution(operation.getReturnResolution());
        }
        if (operation.getReason() != null) {
            current.changeReason(operation.getReason());
        }
        if (operation.getEvidenceUrl() != null) {
            current.changeEvidenceUrl(operation.getEvidenceUrl());
        }
        return operationRepository.save(current);
    }

    @Override
    public void deleteById(Long id) {
        operationRepository.delete(getById(id));
    }
}