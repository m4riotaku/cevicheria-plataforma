package com.cevicheria.platform.purchases.repository;

import com.cevicheria.platform.purchases.model.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {

    List<PurchaseItem> findByPurchaseId(Long purchaseId);

    List<PurchaseItem> findByIngredientIdAndWarehouseId(Long ingredientId, Long warehouseId);
}
