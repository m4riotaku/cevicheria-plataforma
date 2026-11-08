package com.cevicheria.platform.dto;

public record PasswordChangeRequest(String currentPassword, String newPassword) {}
