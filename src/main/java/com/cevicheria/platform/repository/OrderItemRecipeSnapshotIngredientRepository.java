package com.cevicheria.platform.repository;

import com.cevicheria.platform.model.OrderItemRecipeSnapshotIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cevicheria.platform.model.id.OrderItemRecipeSnapshotIngredientId;
import java.util.List;

public interface OrderItemRecipeSnapshotIngredientRepository extends JpaRepository<OrderItemRecipeSnapshotIngredient, OrderItemRecipeSnapshotIngredientId> {
    List<OrderItemRecipeSnapshotIngredient> findByOrderItemId(Long orderItemId);
}
