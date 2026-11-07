package com.cevicheria.platform.security;

import com.cevicheria.platform.handlerException.BadRequestException;
import java.nio.charset.StandardCharsets;

public final class UserPasswordRules
{
    private static final int MIN_PASSWORD_CHARACTERS = 15;
    private static final int MAX_PASSWORD_BYTES = 72;

    private UserPasswordRules()
    {
    }

    public static void validate(String password)
    {
        if(password == null || password.isBlank())
        {
            throw new BadRequestException("La contraseña nueva es obligatoria");
        }

        if(password.codePointCount(0, password.length()) < MIN_PASSWORD_CHARACTERS)
        {
            throw new BadRequestException("La contraseña nueva debe contener al menos 15 caracteres");
        }

        if(password.getBytes(StandardCharsets.UTF_8).length > MAX_PASSWORD_BYTES)
        {
            throw new BadRequestException("La contraseña nueva no puede superar 72 bytes UTF-8");
        }
    }
}
