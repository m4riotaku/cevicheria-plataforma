package com.cevicheria.platform.dto;

import java.time.LocalDate;

public record EmployeeResponse(
        Long id,
        Long businessId,
        String firstNames,
        String lastNames,
        String documentType,
        String documentNumber,
        LocalDate hireDate,
        LocalDate terminationDate,
        String phone,
        String email,
        Boolean active,
        String notes
) {}
