package com.cevicheria.platform.handlerException;

public class UnauthenticatedRequestException extends RuntimeException
{
    public UnauthenticatedRequestException(String message)
    {
        super(message);
    }
}
