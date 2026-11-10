package com.cevicheria.platform.security;

import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

@Component
public class RefreshTokenCodec
{
    private final SecureRandom random = new SecureRandom();

    public String generate(String sessionId)
    {
        byte[] secret = new byte[32];
        random.nextBytes(secret);
        return sessionId + "." + Base64.getUrlEncoder().withoutPadding().encodeToString(secret);
    }

    public String sessionId(String token)
    {
        if (token == null || token.length() > 120) return null;
        int separator = token.indexOf('.');
        if (separator != 36 || token.indexOf('.', separator + 1) != -1) return null;
        try
        {
            String uuid = UUID.fromString(token.substring(0, separator)).toString();
            byte[] randomPart = Base64.getUrlDecoder().decode(token.substring(separator + 1));
            if (randomPart.length != 32) return null;
            return uuid;
        } catch (IllegalArgumentException ex)
        {
            return null;
        }
    }

    public byte[] hash(String token)
    {
        try
        {
            return MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.US_ASCII));
        } catch (NoSuchAlgorithmException ex)
        {
            throw new IllegalStateException("SHA-256 no disponible", ex);
        }
    }

    public boolean matches(String token, byte[] expectedHash)
    {
        return MessageDigest.isEqual(hash(token), expectedHash);
    }
}
