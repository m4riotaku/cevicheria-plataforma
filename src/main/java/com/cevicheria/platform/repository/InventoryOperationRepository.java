package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.InventoryOperation;
import com.cevicheria.platform.model.enums.InventoryOperationStatus;

public interface InventoryOperationRepository extends JpaRepository<InventoryOperation, Long> {

    List<InventoryOperation> findByBusinessIdOrderByCreatedAtDesc(Long businessId);

    List<InventoryOperation> findByBusinessIdAndBranchIdOrderByCreatedAtDesc(Long businessId, Long branchId);

    List<InventoryOperation> findByBusinessIdAndStatusOrderByCreatedAtDesc(
            Long businessId, InventoryOperationStatus status);

    java.util.Optional<InventoryOperation> findByBusinessIdAndOperationKey(Long businessId, String operationKey);
}