package com.cevicheria.platform.repository;

import java.util.Optional;

import java.util.List;

import com.cevicheria.platform.model.enums.PurchaseOrderStatus;

import com.cevicheria.platform.model.PurchaseOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    List<PurchaseOrder> findByBusinessIdAndBranchId(Long businessId, Long branchId);

    List<PurchaseOrder> findByBusinessIdAndBranchIdAndStatus(Long businessId, Long branchId, PurchaseOrderStatus status);

    Optional<PurchaseOrder> findByBusinessIdAndBranchIdAndNumber(Long businessId, Long branchId, String number);

    boolean existsByBusinessIdAndBranchIdAndNumber(Long businessId, Long branchId, String number);
    Optional<PurchaseOrder> findByIdAndBusinessId(Long id, Long businessId);
}
