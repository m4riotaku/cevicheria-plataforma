package com.cevicheria.platform.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.cevicheria.platform.model.enums.EmployeeSettlementStates;

public record SettlementView(
        Long id,
        Long businessId,
        Long branchId,
        Long employeeId,
        LocalDate periodStart,
        LocalDate periodEnd,
        BigDecimal total,
        EmployeeSettlementStates status
) {}
