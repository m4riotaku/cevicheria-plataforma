package com.cevicheria.platform.repository;

import java.util.Optional;

import java.util.List;

import com.cevicheria.platform.model.enums.PurchaseType;

import com.cevicheria.platform.model.enums.PurchaseStatus;

import com.cevicheria.platform.model.Purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findByBusinessIdAndBranchId(Long businessId, Long branchId);

    List<Purchase> findByBusinessIdAndBranchIdAndStatus(Long businessId, Long branchId, PurchaseStatus status);

    Optional<Purchase> findByBusinessIdAndSupplierIdAndTypeAndSeriesAndNumber(
            Long businessId, Long supplierId, PurchaseType type, String series, String number);

    boolean existsByBusinessIdAndSupplierIdAndTypeAndSeriesAndNumber(
            Long businessId, Long supplierId, PurchaseType type, String series, String number);
    Optional<Purchase> findByIdAndBusinessId(Long id, Long businessId);
}
