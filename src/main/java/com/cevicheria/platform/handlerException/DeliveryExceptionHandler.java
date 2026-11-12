package com.cevicheria.platform.handlerException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.cevicheria.platform.controller.ReservationCancellationTermsController;
import com.cevicheria.platform.controller.DeliveryController;
import com.cevicheria.platform.controller.DeliveryZoneController;
import com.cevicheria.platform.controller.ReservationController;

@RestControllerAdvice(assignableTypes = {DeliveryController.class, DeliveryZoneController.class, ReservationController.class, ReservationCancellationTermsController.class})
public class DeliveryExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleInvalidBody(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(new ApiError(400, "El cuerpo de la solicitud contiene datos inválidos"));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleIntegrityConflict(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError(409, "La operación entra en conflicto con registros relacionados"));
    }
}
