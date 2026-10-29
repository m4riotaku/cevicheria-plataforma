package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.Warehouse;
import com.cevicheria.platform.repository.WarehouseRepository;
import com.cevicheria.platform.service.WarehouseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;

    @Override
    public List<Warehouse> getAllByBusiness(Long businessId) {
        return warehouseRepository.findByBusinessId(businessId);
    }

    @Override
    public List<Warehouse> getActiveByBusiness(Long businessId) {
        return warehouseRepository.findByBusinessIdAndActiveTrue(businessId);
    }

    @Override
    public List<Warehouse> getByBranch(Long businessId, Long branchId) {
        return warehouseRepository.findByBusinessIdAndBranchId(businessId, branchId);
    }

    @Override
    public List<Warehouse> getActiveByBranch(Long branchId) {
        return warehouseRepository.findByBranchIdAndActiveTrue(branchId);
    }

    @Override
    public Warehouse getById(Long id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "ID", id));
    }

    @Override
    public Warehouse save(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    @Override
    public Warehouse update(Long id, Warehouse warehouse) {
        Warehouse current = getById(id);
        if (warehouse.getName() != null) {
            current.changeName(warehouse.getName());
        }
        if (warehouse.isActive()) {
            current.activate();
        } else {
            current.deactivate();
        }
        return warehouseRepository.save(current);
    }

    @Override
    public void deleteById(Long id) {
        warehouseRepository.delete(getById(id));
    }
}