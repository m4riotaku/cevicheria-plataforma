package com.cevicheria.platform.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.InventoryBalance;
import com.cevicheria.platform.repository.InventoryBalanceRepository;
import com.cevicheria.platform.service.InventoryBalanceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryBalanceServiceImpl implements InventoryBalanceService {

    private final InventoryBalanceRepository balanceRepository;

    @Override
    public List<InventoryBalance> getAllByBusiness(Long businessId) {
        return balanceRepository.findByBusinessId(businessId);
    }

    @Override
    public List<InventoryBalance> getByBranch(Long businessId, Long branchId) {
        return balanceRepository.findByBusinessIdAndBranchId(businessId, branchId);
    }

    @Override
    public List<InventoryBalance> getByWarehouse(Long businessId, Long warehouseId) {
        return balanceRepository.findByBusinessIdAndWarehouseId(businessId, warehouseId);
    }

    @Override
    public Optional<InventoryBalance> getByWarehouseAndIngredient(Long businessId, Long warehouseId, Long ingredientId) {
        return balanceRepository.findByBusinessIdAndWarehouseIdAndIngredientId(businessId, warehouseId, ingredientId);
    }

    @Override
    public InventoryBalance getById(Long id) {
        return balanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InventoryBalance", "ID", id));
    }

    @Override
    public InventoryBalance save(InventoryBalance balance) {
        return balanceRepository.save(balance);
    }

    @Override
    public InventoryBalance update(Long id, InventoryBalance balance) {
        InventoryBalance current = getById(id);
        if (balance.getQuantity() != null) {
            current.changeQuantity(balance.getQuantity());
        }
        if (balance.getAverageCost() != null) {
            current.changeAverageCost(balance.getAverageCost());
        }
        if (balance.getMinimumStock() != null) {
            current.changeMinimumStock(balance.getMinimumStock());
        }
        return balanceRepository.save(current);
    }

    @Override
    public void deleteById(Long id) {
        balanceRepository.delete(getById(id));
    }
}