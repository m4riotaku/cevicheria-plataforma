package com.cevicheria.platform.dto;

public record UserAccountCreateRequest(String userName,String email, String newPassword)
{
}
