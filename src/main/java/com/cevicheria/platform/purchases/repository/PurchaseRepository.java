package com.cevicheria.platform.purchases.repository;

import com.cevicheria.platform.purchases.model.Purchase;
import com.cevicheria.platform.purchases.model.enums.PurchaseStatus;
import com.cevicheria.platform.purchases.model.enums.PurchaseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findByBusinessIdAndBranchId(Long businessId, Long branchId);

    List<Purchase> findByBusinessIdAndBranchIdAndStatus(Long businessId, Long branchId, PurchaseStatus status);

    Optional<Purchase> findByBusinessIdAndSupplierIdAndTypeAndSeriesAndNumber(
            Long businessId, Long supplierId, PurchaseType type, String series, String number);

    boolean existsByBusinessIdAndSupplierIdAndTypeAndSeriesAndNumber(
            Long businessId, Long supplierId, PurchaseType type, String series, String number);
}
