package com.cevicheria.platform.dto;

import java.time.LocalTime;

public record BranchOpeningHourResponse(
        Long businessId,
        Long branchId,
        Integer weekday,
        Integer shiftNumber,
        LocalTime opensAt,
        LocalTime closesAt) {
}
