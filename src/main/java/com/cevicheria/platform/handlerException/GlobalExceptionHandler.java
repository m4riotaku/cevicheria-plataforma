package com.cevicheria.platform.handlerException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler({BadRequestException.class, InvalidEntityDataException.class})
    public ResponseEntity<ApiError> handleBadRequest(RuntimeException ex)
    {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ApiError> handleConflict(ResourceConflictException ex)
    {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiError(HttpStatus.CONFLICT.value(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMisMatch(MethodArgumentTypeMismatchException ex)
    {
        String paramRequest = ex.getName();
        Object valueRequest = ex.getValue();
        String message;
        if((ex.getRequiredType() == Long.class) && (valueRequest instanceof Long valueCast && valueCast < 0))
        {
           message = "El parametro: " + paramRequest + " debe ser un numero valido";
        }
        else
        {
            message = "El parametro: " + paramRequest + " no tiene un formato valido";
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(HttpStatus.BAD_REQUEST.value(), message));
    }
}
