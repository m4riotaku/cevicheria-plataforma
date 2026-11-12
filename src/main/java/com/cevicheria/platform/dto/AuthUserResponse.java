package com.cevicheria.platform.dto;

import com.cevicheria.platform.model.enums.PlatformRole;

public record AuthUserResponse(Long id, String userName, String email, PlatformRole platformRole)
{
}
