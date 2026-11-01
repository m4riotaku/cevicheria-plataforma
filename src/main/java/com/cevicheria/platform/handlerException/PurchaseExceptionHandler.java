package com.cevicheria.platform.handlerException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.cevicheria.platform.controller.SupplierCategoryController;
import com.cevicheria.platform.controller.SupplierBrandController;
import com.cevicheria.platform.controller.SupplierCategoryLinkController;
import com.cevicheria.platform.controller.SupplierBrandLinkController;
import com.cevicheria.platform.controller.PurchaseOrderItemController;
import com.cevicheria.platform.controller.PurchaseInstallmentController;
import com.cevicheria.platform.controller.SupplierController;
import com.cevicheria.platform.controller.PurchaseItemController;
import com.cevicheria.platform.controller.SupplyScheduleController;
import com.cevicheria.platform.controller.PurchaseController;
import com.cevicheria.platform.controller.SupplierIngredientController;
import com.cevicheria.platform.controller.PurchaseOrderController;

@RestControllerAdvice(assignableTypes = {PurchaseController.class, PurchaseItemController.class, PurchaseInstallmentController.class, PurchaseOrderController.class, PurchaseOrderItemController.class, SupplierController.class, SupplierIngredientController.class, SupplyScheduleController.class, SupplierCategoryController.class, SupplierBrandController.class, SupplierCategoryLinkController.class, SupplierBrandLinkController.class})
public class PurchaseExceptionHandler {
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
