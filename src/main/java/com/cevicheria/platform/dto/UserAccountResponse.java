package com.cevicheria.platform.dto;

import com.cevicheria.platform.model.enums.PlatformRole;
import java.time.LocalDateTime;

public record UserAccountResponse(
        Long id,
        String userName,
        String email,
        boolean active,
        LocalDateTime createAt,
        PlatformRole platformRole)
{
}