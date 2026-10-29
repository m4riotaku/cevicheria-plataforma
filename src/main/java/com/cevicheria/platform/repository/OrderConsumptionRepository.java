package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.OrderConsumption;

public interface OrderConsumptionRepository extends JpaRepository<OrderConsumption, Long>
{
    List<OrderConsumption> findByBusinessIdOrderByCreatedAtDesc(Long businessId);

    List<OrderConsumption> findByOrderItemId(Long orderItemId);

    List<OrderConsumption> findByBusinessIdAndOrderItemId(Long businessId, Long orderItemId);

    List<OrderConsumption> findByBusinessIdAndIngredientId(Long businessId, Long ingredientId);
}