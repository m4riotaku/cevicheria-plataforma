package com.cevicheria.platform.dto;

public record UserAccountUpdateRequest(String userName, String email, String currentPassword, String newPassword, Boolean active)
{
}
