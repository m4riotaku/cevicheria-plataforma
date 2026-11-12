package com.cevicheria.platform.repository;

import java.util.Optional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cevicheria.platform.model.PurchaseInstallment;

import java.time.LocalDate;

@Repository
public interface PurchaseInstallmentRepository extends JpaRepository<PurchaseInstallment, Long> {
    Optional<PurchaseInstallment> findByBusinessIdAndBranchIdAndPurchaseIdAndNumber(
            Long businessId, Long branchId, Long purchaseId, Integer number);

    List<PurchaseInstallment> findByPurchaseIdOrderByNumber(Long purchaseId);

    // cuotas vencidas en un rango de negocio/sucursal
    List<PurchaseInstallment> findByBusinessIdAndBranchIdAndDueOnLessThanEqual(
            Long businessId, Long branchId, LocalDate date);
    Optional<PurchaseInstallment> findByIdAndBusinessId(Long id, Long businessId);
    List<PurchaseInstallment> findByBusinessIdAndBranchId(Long businessId, Long branchId);
}
