package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.BranchOpeningHour;
import com.cevicheria.platform.model.id.BranchOpeningHourId;

public interface BranchOpeningHourService {

    List<BranchOpeningHour> getByBranch(Long businessId, Long branchId);
    
    BranchOpeningHour getById(BranchOpeningHourId id);
    
    BranchOpeningHour save(BranchOpeningHour branchOpeningHour);
    
    BranchOpeningHour update(BranchOpeningHourId id, BranchOpeningHour branchOpeningHour);
    
    void deleteById(BranchOpeningHourId id);
}
