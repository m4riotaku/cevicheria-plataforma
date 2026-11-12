package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.BatchControl;

public interface BatchControlService {

    List<BatchControl> getAllByBusiness(Long businessId);

    List<BatchControl> getByBatch(Long businessId, Long batchId);

    BatchControl getById(Long id);

    BatchControl save(BatchControl control);

    BatchControl update(Long id, BatchControl control);

    void deleteById(Long id);
}