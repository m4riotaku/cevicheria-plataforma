package com.cevicheria.platform.inventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.OrderConsumption;

public interface OrderConsumptionRepository extends JpaRepository<OrderConsumption, Long> {

    List<OrderConsumption> findByOrderItemId(Long orderItemId);

    List<OrderConsumption> findByIngredientIdOrderByConsumedAtDesc(Long ingredientId);
}