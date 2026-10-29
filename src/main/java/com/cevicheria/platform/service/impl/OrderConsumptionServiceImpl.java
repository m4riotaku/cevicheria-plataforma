package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.OrderConsumption;
import com.cevicheria.platform.repository.OrderConsumptionRepository;
import com.cevicheria.platform.service.OrderConsumptionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderConsumptionServiceImpl implements OrderConsumptionService {

    private final OrderConsumptionRepository consumptionRepository;

    @Override
    public List<OrderConsumption> getAllByBusiness(Long businessId) {
        return consumptionRepository.findByBusinessIdOrderByCreatedAtDesc(businessId);
    }

    @Override
    public List<OrderConsumption> getByOrderItem(Long businessId, Long orderItemId) {
        return consumptionRepository.findByBusinessIdAndOrderItemId(businessId, orderItemId);
    }

    @Override
    public List<OrderConsumption> getByIngredient(Long businessId, Long ingredientId) {
        return consumptionRepository.findByBusinessIdAndIngredientId(businessId, ingredientId);
    }

    @Override
    public OrderConsumption getById(Long id) {
        return consumptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderConsumption", "ID", id));
    }

    @Override
    public OrderConsumption save(OrderConsumption consumption) {
        return consumptionRepository.save(consumption);
    }

    @Override
    public OrderConsumption update(Long id, OrderConsumption consumption) {
        OrderConsumption current = getById(id);
        if (consumption.getBaseQuantity() != null) {
            current.changeBaseQuantity(consumption.getBaseQuantity());
        }
        if (consumption.getTotalCost() != null) {
            current.changeTotalCost(consumption.getTotalCost());
        }
        return consumptionRepository.save(current);
    }

    @Override
    public void deleteById(Long id) {
        consumptionRepository.delete(getById(id));
    }
}