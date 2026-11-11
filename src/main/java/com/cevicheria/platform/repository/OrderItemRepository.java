package com.cevicheria.platform.repository;

import com.cevicheria.platform.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByBusinessIdAndBranchIdAndOrderIdOrderByIdAsc(Long businessId, Long branchId, Long orderId);
    List<OrderItem> findByBusinessIdAndBranchIdAndPreparationAreaIdAndStatusInOrderByIdAsc(Long businessId, Long branchId, Long areaId, java.util.Collection<com.cevicheria.platform.model.enums.OrderItemStatus> statuses);
}
