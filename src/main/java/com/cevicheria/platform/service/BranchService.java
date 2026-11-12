package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.Branch;
import com.cevicheria.platform.model.Employee;

public interface BranchService {

    List<Branch> getAllByBusiness(Long businessId);
    
    List<Branch> getActiveByBusiness(Long businessId);
    
    Branch getById(Long businessId, Long id);

    Employee getResponsibleEmployee(Long businessId, Long employeeId);
    
    Branch save(Branch branch);
    
    Branch update(Long businessId, Long id, Branch branch);
    
    void deleteById(Long businessId, Long id);
}
