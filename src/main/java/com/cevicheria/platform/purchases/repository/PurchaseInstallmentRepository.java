package com.cevicheria.platform.purchases.repository;

import com.cevicheria.platform.purchases.model.PurchaseInstallment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PurchaseInstallmentRepository extends JpaRepository<PurchaseInstallment, Long> {

    List<PurchaseInstallment> findByPurchaseIdOrderByNumber(Long purchaseId);

    // cuotas vencidas en un rango de negocio/sucursal
    List<PurchaseInstallment> findByBusinessIdAndBranchIdAndDueOnLessThanEqual(
            Long businessId, Long branchId, LocalDate date);
}
