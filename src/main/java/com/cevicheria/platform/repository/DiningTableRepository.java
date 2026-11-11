package com.cevicheria.platform.repository;

import com.cevicheria.platform.model.DiningTable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiningTableRepository extends JpaRepository<DiningTable, Long> {
    List<DiningTable> findByBusinessIdAndBranchIdOrderByIdAsc(Long businessId, Long branchId);
    boolean existsByBusinessIdAndBranchIdAndNumber(Long businessId, Long branchId, String number);
}
