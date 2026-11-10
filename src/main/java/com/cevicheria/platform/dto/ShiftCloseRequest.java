package com.cevicheria.platform.dto;

import java.math.BigDecimal;

public record ShiftCloseRequest(
        BigDecimal countedCash,
        String notes
) {}
