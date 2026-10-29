package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.InventoryOperationDetail;
import com.cevicheria.platform.model.id.InventoryOperationDetailId;
import com.cevicheria.platform.repository.InventoryOperationDetailRepository;
import com.cevicheria.platform.service.InventoryOperationDetailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryOperationDetailServiceImpl implements InventoryOperationDetailService {

    private final InventoryOperationDetailRepository detailRepository;

    @Override
    public List<InventoryOperationDetail> getAllByBusiness(Long businessId) {
        return detailRepository.findByBusinessId(businessId);
    }

    @Override
    public List<InventoryOperationDetail> getByOperation(Long businessId, Long operationId) {
        return detailRepository.findByBusinessIdAndOperationId(businessId, operationId);
    }

    @Override
    public InventoryOperationDetail getById(InventoryOperationDetailId id) {
        return detailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InventoryOperationDetail", "ID", id));
    }

    @Override
    public InventoryOperationDetail save(InventoryOperationDetail detail) {
        return detailRepository.save(detail);
    }

    @Override
    public void deleteById(InventoryOperationDetailId id) {
        detailRepository.delete(getById(id));
    }
}