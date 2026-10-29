package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.InventoryOperation;
import com.cevicheria.platform.model.enums.InventoryOperationStatus;

public interface InventoryOperationService {

    List<InventoryOperation> getAllByBusiness(Long businessId);

    List<InventoryOperation> getByBranch(Long businessId, Long branchId);

    List<InventoryOperation> getByStatus(Long businessId, InventoryOperationStatus status);

    InventoryOperation getById(Long id);

    InventoryOperation save(InventoryOperation operation);

    InventoryOperation update(Long id, InventoryOperation operation);

    void deleteById(Long id);
}