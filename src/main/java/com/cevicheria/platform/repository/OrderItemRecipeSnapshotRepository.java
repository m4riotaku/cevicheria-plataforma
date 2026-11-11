package com.cevicheria.platform.repository;

import com.cevicheria.platform.model.OrderItemRecipeSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cevicheria.platform.model.id.OrderItemRecipeSnapshotId;

public interface OrderItemRecipeSnapshotRepository extends JpaRepository<OrderItemRecipeSnapshot, OrderItemRecipeSnapshotId> {
    
}
