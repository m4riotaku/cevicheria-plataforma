package com.cevicheria.platform.repository;

import com.cevicheria.platform.model.OrderItemModifier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderItemModifierRepository extends JpaRepository<OrderItemModifier, Long> {
    List<OrderItemModifier> findByOrderItemIdOrderByIdAsc(Long orderItemId);
}
