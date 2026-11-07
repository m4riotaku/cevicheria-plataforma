package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.Branch;

public interface BranchService {

    List<Branch> getAllByBusiness(Long businessId);
    
    List<Branch> getActiveByBusiness(Long businessId);
    
    Branch getById(Long businessId, Long id);
    
    Branch save(Branch branch);
    
    Branch update(Long businessId, Long id, Branch branch);
    
    void deleteById(Long businessId, Long id);
}
