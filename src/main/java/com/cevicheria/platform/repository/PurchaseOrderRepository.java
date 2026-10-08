package com.cevicheria.platform.repository;

import com.cevicheria.platform.model.PurchaseOrder;
import com.cevicheria.platform.model.enums.PurchaseOrderStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    List<PurchaseOrder> findByBusinessIdAndBranchId(Long businessId, Long branchId);

    List<PurchaseOrder> findByBusinessIdAndBranchIdAndStatus(Long businessId, Long branchId, PurchaseOrderStatus status);

    Optional<PurchaseOrder> findByBusinessIdAndBranchIdAndNumber(Long businessId, Long branchId, String number);

    boolean existsByBusinessIdAndBranchIdAndNumber(Long businessId, Long branchId, String number);
}
