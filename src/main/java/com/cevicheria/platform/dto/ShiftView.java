package com.cevicheria.platform.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.cevicheria.platform.model.enums.CashShiftStates;

public record ShiftView(
        Long id,
        Long businessId,
        Long branchId,
        Long cashRegisterId,
        Long responsibleEmployeeId,
        CashShiftStates status,
        LocalDateTime openedAt,
        LocalDateTime closedAt,
        BigDecimal openingFund,
        BigDecimal expectedCash,
        BigDecimal countedCash,
        BigDecimal difference
) {}
