package com.cevicheria.platform.repository;

import java.util.Optional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cevicheria.platform.model.PurchaseItem;


@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {

    List<PurchaseItem> findByPurchaseId(Long purchaseId);

    List<PurchaseItem> findByIngredientIdAndWarehouseId(Long ingredientId, Long warehouseId);
    Optional<PurchaseItem> findByIdAndBusinessId(Long id, Long businessId);
    List<PurchaseItem> findByBusinessIdAndBranchId(Long businessId, Long branchId);
}
