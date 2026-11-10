package com.cevicheria.platform.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.cevicheria.platform.model.enums.EmployeeIncentiveCriterion;
import com.cevicheria.platform.model.enums.EmployeeIncentiveType;

public record IncentiveRequest(
        Long businessId,
        Long branchId,
        Long employeeId,
        EmployeeIncentiveType type,
        Long sourcePaymentId,
        Long commissionRuleId,
        Long settlementId,
        LocalDate recordDate,
        BigDecimal amount,
        EmployeeIncentiveCriterion criterion,
        String operationKey,
        String notes,
        Long orderItemId
) {}
