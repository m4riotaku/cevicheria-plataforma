package com.cevicheria.platform.dto;



public record EmployeeBranchAssignmentResponse(
        Long id,
        Long businessId,
        Long branchId,
        Long employeeId,
        boolean active
) {}
