package com.cevicheria.platform.dto;

import java.time.LocalDate;

public record EmployeeRequest(
        String firstNames,
        String lastNames,
        String documentType,
        String documentNumber,
        LocalDate hireDate,
        String phone,
        String email,
        LocalDate terminationDate,
        String notes,
        Boolean active
) {}
