package com.cevicheria.platform.dto;

public record LoginResponse(String token, String refreshToken, String tokenType, long expiresInSeconds)
{
}