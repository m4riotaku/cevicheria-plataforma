package com.cevicheria.platform.purchases.repository;

import com.cevicheria.platform.purchases.model.SupplySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SupplyScheduleRepository extends JpaRepository<SupplySchedule, Long> {

    List<SupplySchedule> findByBusinessIdAndBranchIdAndActive(Long businessId, Long branchId, boolean active);

    // abastecimientos vencidos o proximos a vencer (para alertas)
    List<SupplySchedule> findByBusinessIdAndActiveAndNextDateLessThanEqual(
            Long businessId, boolean active, LocalDate date);
}
