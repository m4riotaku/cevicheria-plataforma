package com.cevicheria.platform.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SettlementRequest(
        Long businessId,
        Long branchId,
        Long employeeId,
        LocalDate periodStart,
        LocalDate periodEnd,
        BigDecimal total
) {}
