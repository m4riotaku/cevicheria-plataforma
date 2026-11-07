package com.cevicheria.platform.handlerException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cevicheria.platform.controller.BranchConfigurationController;
import com.cevicheria.platform.controller.BranchController;
import com.cevicheria.platform.controller.BranchOpeningHourController;
import com.cevicheria.platform.controller.BusinessConfigurationController;
import com.cevicheria.platform.controller.BusinessController;

@RestControllerAdvice(assignableTypes = {
        BusinessController.class, BranchController.class, BusinessConfigurationController.class,
        BranchConfigurationController.class, BranchOpeningHourController.class
})
public class BusinessExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleIntegrityConflict(DataIntegrityViolationException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError(409, "La operación entra en conflicto con registros relacionados"));
    }
}
