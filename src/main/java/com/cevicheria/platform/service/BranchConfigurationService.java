package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.BranchConfiguration;
import com.cevicheria.platform.model.id.BranchConfigurationId;

public interface BranchConfigurationService {

    List<BranchConfiguration> getAllByBusiness(Long businessId);

    BranchConfiguration getById(BranchConfigurationId id);
    
    BranchConfiguration save(BranchConfiguration branchConfiguration);
    
    BranchConfiguration update(BranchConfigurationId id, BranchConfiguration branchConfiguration);
    
    void deleteById(BranchConfigurationId id);
}
