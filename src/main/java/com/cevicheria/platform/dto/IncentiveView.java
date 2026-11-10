package com.cevicheria.platform.dto;

import java.math.BigDecimal;
import com.cevicheria.platform.model.enums.EmployeeIncentiveCriterion;
import com.cevicheria.platform.model.enums.EmployeeIncentiveType;

public record IncentiveView(
        Long id,
        Long businessId,
        Long branchId,
        Long employeeId,
        EmployeeIncentiveType type,
        BigDecimal amount,
        EmployeeIncentiveCriterion criterion,
        String operationKey
) {}
