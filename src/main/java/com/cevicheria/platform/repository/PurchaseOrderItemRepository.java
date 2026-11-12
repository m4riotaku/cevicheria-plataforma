package com.cevicheria.platform.repository;

import java.util.Optional;

import java.util.List;

import com.cevicheria.platform.model.PurchaseOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem, Long> {

    List<PurchaseOrderItem> findByPurchaseOrderId(Long purchaseOrderId);

    List<PurchaseOrderItem> findByIngredientId(Long ingredientId);
    Optional<PurchaseOrderItem> findByIdAndBusinessId(Long id, Long businessId);
    List<PurchaseOrderItem> findByBusinessIdAndBranchId(Long businessId, Long branchId);
}
