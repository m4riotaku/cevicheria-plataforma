package com.cevicheria.platform.dto;

import com.cevicheria.platform.model.enums.PlatformRole;

public record UserAccountUpdateRequest(
        String userName,
        String email,
        String currentPassword,
        String newPassword,
        Boolean active,
        PlatformRole platformRole)
{
    public UserAccountUpdateRequest(String userName, String email, String currentPassword, String newPassword, Boolean active)
    {
        this(userName, email, currentPassword, newPassword, active, null);
    }
}
