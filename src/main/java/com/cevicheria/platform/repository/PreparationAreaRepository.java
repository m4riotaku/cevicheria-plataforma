package com.cevicheria.platform.repository;

import com.cevicheria.platform.model.PreparationArea;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PreparationAreaRepository extends JpaRepository<PreparationArea, Long> {
    List<PreparationArea> findByBusinessId(Long businessId);
    List<PreparationArea> findByBusinessIdAndBranchIdOrderByIdAsc(Long businessId, Long branchId);
}
