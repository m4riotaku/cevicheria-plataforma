package com.cevicheria.platform.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class JwtTokenHash
{
    private JwtTokenHash() {}

    public static byte[] hash(String token)
    {
        try
        {
            return MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.US_ASCII));
        } catch (NoSuchAlgorithmException ex)
        {
            throw new IllegalStateException("SHA-256 no disponible", ex);
        }
    }
}
