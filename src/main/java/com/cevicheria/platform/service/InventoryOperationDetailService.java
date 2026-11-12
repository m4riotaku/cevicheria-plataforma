package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.InventoryOperationDetail;
import com.cevicheria.platform.model.id.InventoryOperationDetailId;

public interface InventoryOperationDetailService {

    List<InventoryOperationDetail> getAllByBusiness(Long businessId);

    List<InventoryOperationDetail> getByOperation(Long businessId, Long operationId);

    InventoryOperationDetail getById(InventoryOperationDetailId id);

    InventoryOperationDetail save(InventoryOperationDetail detail);

    void deleteById(InventoryOperationDetailId id);
}