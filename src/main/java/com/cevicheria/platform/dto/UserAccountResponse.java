package com.cevicheria.platform.dto;

import java.time.LocalDateTime;

public record UserAccountResponse(Long id,String userName, String email, boolean active, LocalDateTime createAt)
{
}