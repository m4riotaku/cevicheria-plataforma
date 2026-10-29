package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.OrderConsumption;

public interface OrderConsumptionService {

    List<OrderConsumption> getAllByBusiness(Long businessId);

    List<OrderConsumption> getByOrderItem(Long businessId, Long orderItemId);

    List<OrderConsumption> getByIngredient(Long businessId, Long ingredientId);

    OrderConsumption getById(Long id);

    OrderConsumption save(OrderConsumption consumption);

    OrderConsumption update(Long id, OrderConsumption consumption);

    void deleteById(Long id);
}