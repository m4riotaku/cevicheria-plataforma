package com.cevicheria.platform.inventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.InventoryMovement;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {

    List<InventoryMovement> findByInventoryOperationId(Long inventoryOperationId);

    List<InventoryMovement> findByIngredientIdOrderByOccurredAtDesc(Long ingredientId);
}