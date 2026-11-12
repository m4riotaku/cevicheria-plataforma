package com.cevicheria.platform.handlerException;

public class ResourceConflictException extends RuntimeException
{
    public ResourceConflictException(String message)
    {
        super(message);
    }
}
