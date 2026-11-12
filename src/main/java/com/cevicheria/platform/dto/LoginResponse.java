package com.cevicheria.platform.dto;

public record LoginResponse(String token, String tokenType, long expiresInSeconds)
{
}
