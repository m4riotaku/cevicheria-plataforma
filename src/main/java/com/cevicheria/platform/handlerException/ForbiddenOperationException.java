package com.cevicheria.platform.handlerException;

public class ForbiddenOperationException extends RuntimeException
{
    public ForbiddenOperationException(String message)
    {
        super(message);
    }
}
