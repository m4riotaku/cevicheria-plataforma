package com.cevicheria.platform.handlerException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex)
    {
        return error(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({BadRequestException.class, InvalidEntityDataException.class})
    public ResponseEntity<ApiError> handleBadRequest(RuntimeException ex)
    {
        return error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ApiError> handleConflict(ResourceConflictException ex)
    {
        return error(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(ForbiddenOperationException.class)
    public ResponseEntity<ApiError> handleForbidden(ForbiddenOperationException ex)
    {
        return error(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(UnauthenticatedRequestException.class)
    public ResponseEntity<ApiError> handleUnauthenticated(UnauthenticatedRequestException ex)
    {
        return error(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex)
    {
        return error(HttpStatus.BAD_REQUEST,
                "El parámetro '" + ex.getName() + "' no tiene un formato válido");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleInvalidBody(HttpMessageNotReadableException ex)
    {
        return error(HttpStatus.BAD_REQUEST,
                "El cuerpo de la solicitud contiene datos inválidos");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleIntegrityConflict(DataIntegrityViolationException ex)
    {
        return error(HttpStatus.CONFLICT,
                "La operación entra en conflicto con registros relacionados o valores duplicados");
    }

    private ResponseEntity<ApiError> error(HttpStatus status, String message)
    {
        return ResponseEntity.status(status)
                .body(new ApiError(status.value(), message));
    }
}
