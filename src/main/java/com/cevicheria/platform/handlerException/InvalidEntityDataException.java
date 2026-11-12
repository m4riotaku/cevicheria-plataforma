package com.cevicheria.platform.handlerException;

public class InvalidEntityDataException extends RuntimeException
{
    public InvalidEntityDataException(String message)
    {
        super(message);
    }
}
