package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.BatchControl;
import com.cevicheria.platform.repository.BatchControlRepository;
import com.cevicheria.platform.service.BatchControlService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BatchControlServiceImpl implements BatchControlService {

    private final BatchControlRepository controlRepository;

    @Override
    public List<BatchControl> getAllByBusiness(Long businessId) {
        return controlRepository.findByBusinessIdOrderByRecordDateDesc(businessId);
    }

    @Override
    public List<BatchControl> getByBatch(Long businessId, Long batchId) {
        return controlRepository.findByBusinessIdAndBatchIdOrderByRecordDateDesc(businessId, batchId);
    }

    @Override
    public BatchControl getById(Long id) {
        return controlRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BatchControl", "ID", id));
    }

    @Override
    public BatchControl save(BatchControl control) {
        return controlRepository.save(control);
    }

    @Override
    public BatchControl update(Long id, BatchControl control) {
        BatchControl current = getById(id);
        if (control.getTemperature() != null) {
            current.changeTemperature(control.getTemperature());
        }
        if (control.getIncident() != null) {
            current.changeIncident(control.getIncident());
        }
        if (control.getCorrectiveAction() != null) {
            current.changeCorrectiveAction(control.getCorrectiveAction());
        }
        if (control.getEvidenceUrl() != null) {
            current.changeEvidenceUrl(control.getEvidenceUrl());
        }
        return controlRepository.save(current);
    }

    @Override
    public void deleteById(Long id) {
        controlRepository.delete(getById(id));
    }
}