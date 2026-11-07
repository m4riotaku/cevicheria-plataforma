package com.cevicheria.platform.dto;

import java.time.LocalTime;

public record BranchOpeningHourRequest(
        LocalTime opensAt,
        LocalTime closesAt) {
}
