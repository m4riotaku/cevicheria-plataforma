package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalesReceiptIntegrationAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String provider;

    private String externalOperationId;

    private String requestReference;

    private String responseCode;

    private String responseMessage;

    @Generated(event = EventType.INSERT)
    private LocalDateTime attemptedAt;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sales_receipt_id")
    private SalesReceipt salesReceipt;

    public SalesReceiptIntegrationAttempt(
            Business business,
            Branch branch,
            SalesReceipt salesReceipt,
            String provider,
            String externalOperationId,
            String requestReference,
            String responseCode,
            String responseMessage) {
        changeBusiness(business);
        changeBranch(branch);
        changeSalesReceipt(salesReceipt);
        changeProvider(provider);
        changeExternalOperationId(externalOperationId);
        changeRequestReference(requestReference);
        changeResponseCode(responseCode);
        changeResponseMessage(responseMessage);
        if (business.getId() != null && branch.getBusiness() != null
                && branch.getBusiness().getId() != null
                && !business.getId().equals(branch.getBusiness().getId())) {
            throw new InvalidEntityDataException("El local no pertenece al negocio seleccionado");
        }
    }

    private void changeBusiness(Business business) {
        if (business == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        this.business = business;
    }

    private void changeBranch(Branch branch) {
        if (branch == null) {
            throw new InvalidEntityDataException("El local es obligatorio");
        }
        this.branch = branch;
    }

    private void changeSalesReceipt(SalesReceipt salesReceipt) {
        if (salesReceipt == null) {
            throw new InvalidEntityDataException("El comprobante de venta es obligatorio");
        }
        this.salesReceipt = salesReceipt;
    }

    private void changeProvider(String provider) {
        if (provider == null) {
            throw new InvalidEntityDataException("El proveedor es obligatorio");
        }
        if (provider.isBlank()) {
            throw new InvalidEntityDataException("El proveedor no puede estar vacío");
        }
        String normalizedValue = provider.trim();
        if (normalizedValue.length() > 120) {
            throw new InvalidEntityDataException("El proveedor no puede superar 120 caracteres");
        }
        this.provider = normalizedValue;
    }

    private void changeExternalOperationId(String externalOperationId) {
        if (externalOperationId == null || externalOperationId.isBlank()) {
            this.externalOperationId = null;
            return;
        }
        String normalizedValue = externalOperationId.trim();
        if (normalizedValue.length() > 150) {
            throw new InvalidEntityDataException("El identificador externo no puede superar 150 caracteres");
        }
        this.externalOperationId = normalizedValue;
    }

    private void changeRequestReference(String requestReference) {
        if (requestReference == null || requestReference.isBlank()) {
            this.requestReference = null;
            return;
        }
        String normalizedValue = requestReference.trim();
        if (normalizedValue.length() > 150) {
            throw new InvalidEntityDataException("La referencia de solicitud no puede superar 150 caracteres");
        }
        this.requestReference = normalizedValue;
    }

    private void changeResponseCode(String responseCode) {
        if (responseCode == null || responseCode.isBlank()) {
            this.responseCode = null;
            return;
        }
        String normalizedValue = responseCode.trim();
        if (normalizedValue.length() > 30) {
            throw new InvalidEntityDataException("El código de respuesta no puede superar 30 caracteres");
        }
        this.responseCode = normalizedValue;
    }

    private void changeResponseMessage(String responseMessage) {
        if (responseMessage == null || responseMessage.isBlank()) {
            this.responseMessage = null;
            return;
        }
        String normalizedValue = responseMessage.trim();
        if (normalizedValue.length() > 500) {
            throw new InvalidEntityDataException("El mensaje de respuesta no puede superar 500 caracteres");
        }
        this.responseMessage = normalizedValue;
    }

}
