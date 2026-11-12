package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.BranchOpeningHour;
import com.cevicheria.platform.model.id.BranchOpeningHourId;

public interface BranchOpeningHourRepository extends JpaRepository<BranchOpeningHour, BranchOpeningHourId> {
    List<BranchOpeningHour> findByIdBusinessIdAndIdBranchIdOrderByIdWeekdayAscIdShiftNumberAsc(
                Long businessId, Long branchId);
}
