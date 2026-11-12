package com.cevicheria.platform.service;

import com.cevicheria.platform.dto.EmployeeBranchAssignmentRequest;
import com.cevicheria.platform.dto.EmployeeBranchAssignmentResponse;
import com.cevicheria.platform.dto.EmployeeRequest;
import com.cevicheria.platform.dto.EmployeeResponse;
import java.util.List;

public interface EmployeeManagementService {
    List<EmployeeResponse> list(Long businessId);
    List<EmployeeResponse> listBranch(Long businessId, Long branchId);
    EmployeeResponse find(Long businessId, Long id);
    EmployeeResponse create(Long businessId, EmployeeRequest request);
    EmployeeResponse update(Long businessId, Long id, EmployeeRequest request);
    void deactivate(Long businessId, Long id);
    List<EmployeeBranchAssignmentResponse> assignments(Long businessId, Long branchId);
    EmployeeBranchAssignmentResponse assignment(Long businessId, Long branchId, Long assignmentId);
    EmployeeBranchAssignmentResponse assign(Long businessId, Long branchId, EmployeeBranchAssignmentRequest request);
    EmployeeBranchAssignmentResponse changeAssignment(Long businessId, Long branchId, Long id,
                                                      EmployeeBranchAssignmentRequest request);
    void deactivateAssignment(Long businessId, Long branchId, Long id);
    List<Long> functions(Long businessId, Long branchId, Long assignmentId);
    void addFunction(Long businessId, Long branchId, Long assignmentId, Long functionId);
    void removeFunction(Long businessId, Long branchId, Long assignmentId, Long functionId);
}
