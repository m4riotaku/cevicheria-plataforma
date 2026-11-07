package com.cevicheria.platform.dto;

import java.time.LocalDateTime;

public record BranchConfigurationResponse(
        Long businessId,
        Long branchId,
        boolean saleByWeightEnabled,
        boolean stockAlertEnabled,
        Integer expirationAlertDays,
        LocalDateTime createdAt) {
}
