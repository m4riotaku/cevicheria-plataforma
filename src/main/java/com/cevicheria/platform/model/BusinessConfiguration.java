package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.enums.InvoiceEnvironment;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
public class BusinessConfiguration {

    @Id
    private Long businessId;

    private boolean reservationsEnabled = false;

    @Enumerated(EnumType.STRING)
    private InvoiceEnvironment invoiceEnvironment = InvoiceEnvironment.PRUEBAS;

    private String billingProvider;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id", insertable = false, updatable = false)
    private Business business;

    public BusinessConfiguration(
            Business business,
            InvoiceEnvironment invoiceEnvironment,
            String billingProvider) {
        changeBusiness(business);
        changeInvoiceEnvironment(invoiceEnvironment);
        changeBillingProvider(billingProvider);
    }

    private void changeBusiness(Business business) {
        if (business == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        if (business.getId() == null) {
            throw new InvalidEntityDataException("El negocio debe estar registrado antes de realizar la asociación");
        }
        this.business = business;
        this.businessId = business.getId();
    }

    public void changeInvoiceEnvironment(InvoiceEnvironment invoiceEnvironment) {
        if (invoiceEnvironment == null) {
            throw new InvalidEntityDataException("El entorno de facturación es obligatorio");
        }
        this.invoiceEnvironment = invoiceEnvironment;
    }

    public void changeBillingProvider(String billingProvider) {
        if (billingProvider == null || billingProvider.isBlank()) {
            this.billingProvider = null;
            return;
        }
        String normalizedValue = billingProvider.trim();
        if (normalizedValue.length() > 120) {
            throw new InvalidEntityDataException("El proveedor de facturación no puede superar 120 caracteres");
        }
        this.billingProvider = normalizedValue;
    }

    public void enableReservations() {
        this.reservationsEnabled = true;
    }

    public void disableReservations() {
        this.reservationsEnabled = false;
    }

}
