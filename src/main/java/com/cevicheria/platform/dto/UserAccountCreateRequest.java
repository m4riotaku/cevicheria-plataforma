package com.cevicheria.platform.dto;

import com.cevicheria.platform.model.enums.PlatformRole;

public record UserAccountCreateRequest(
        String userName,
        String email,
        String newPassword,
        PlatformRole platformRole)
{
    public UserAccountCreateRequest(String userName, String email, String newPassword)
    {
        this(userName, email, newPassword, null);
    }
}
