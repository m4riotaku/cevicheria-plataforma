package com.cevicheria.platform.dto;

import java.time.LocalTime;

public record BranchOpeningHourCreateRequest(
        Integer weekday,
        Integer shiftNumber,
        LocalTime opensAt,
        LocalTime closesAt) {
}
