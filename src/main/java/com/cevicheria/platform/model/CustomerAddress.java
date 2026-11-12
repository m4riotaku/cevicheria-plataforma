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
public class CustomerAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;

    private String addressLine;

    private String reference;

    private String ubigeoCode;

    private String department;

    private String province;

    private String district;

    private boolean isDefault = false;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public CustomerAddress(
            Business business,
            Customer customer,
            String label,
            String addressLine,
            String reference,
            String ubigeoCode,
            String department,
            String province,
            String district) {
        changeBusiness(business);
        changeCustomer(customer);
        changeLabel(label);
        changeAddressLine(addressLine);
        changeReference(reference);
        changeUbigeoCode(ubigeoCode);
        changeDepartment(department);
        changeProvince(province);
        changeDistrict(district);
    }

    private void changeBusiness(Business business) {
        if (business == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        this.business = business;
    }

    private void changeCustomer(Customer customer) {
        if (customer == null) {
            throw new InvalidEntityDataException("El cliente es obligatorio");
        }
        this.customer = customer;
    }

    public void changeLabel(String label) {
        if (label == null || label.isBlank()) {
            this.label = null;
            return;
        }
        String normalizedValue = label.trim();
        if (normalizedValue.length() > 80) {
            throw new InvalidEntityDataException("La etiqueta no puede superar 80 caracteres");
        }
        this.label = normalizedValue;
    }

    public void changeAddressLine(String addressLine) {
        if (addressLine == null) {
            throw new InvalidEntityDataException("La dirección es obligatoria");
        }
        if (addressLine.isBlank()) {
            throw new InvalidEntityDataException("La dirección no puede estar vacío");
        }
        String normalizedValue = addressLine.trim();
        if (normalizedValue.length() > 250) {
            throw new InvalidEntityDataException("La dirección no puede superar 250 caracteres");
        }
        this.addressLine = normalizedValue;
    }

    public void changeReference(String reference) {
        if (reference == null || reference.isBlank()) {
            this.reference = null;
            return;
        }
        String normalizedValue = reference.trim();
        if (normalizedValue.length() > 250) {
            throw new InvalidEntityDataException("La referencia de dirección no puede superar 250 caracteres");
        }
        this.reference = normalizedValue;
    }

    public void changeUbigeoCode(String ubigeoCode) {
        if (ubigeoCode == null || ubigeoCode.isBlank()) {
            this.ubigeoCode = null;
            return;
        }
        String normalizedValue = ubigeoCode.trim();
        if (normalizedValue.length() > 6) {
            throw new InvalidEntityDataException("El ubigeo no puede superar 6 caracteres");
        }
        if (!normalizedValue.matches("[0-9]{6}")) {
            throw new InvalidEntityDataException("El ubigeo debe contener 6 dígitos");
        }
        this.ubigeoCode = normalizedValue;
    }

    public void changeDepartment(String department) {
        if (department == null || department.isBlank()) {
            this.department = null;
            return;
        }
        String normalizedValue = department.trim();
        if (normalizedValue.length() > 80) {
            throw new InvalidEntityDataException("El departamento no puede superar 80 caracteres");
        }
        this.department = normalizedValue;
    }

    public void changeProvince(String province) {
        if (province == null || province.isBlank()) {
            this.province = null;
            return;
        }
        String normalizedValue = province.trim();
        if (normalizedValue.length() > 80) {
            throw new InvalidEntityDataException("La provincia no puede superar 80 caracteres");
        }
        this.province = normalizedValue;
    }

    public void changeDistrict(String district) {
        if (district == null || district.isBlank()) {
            this.district = null;
            return;
        }
        String normalizedValue = district.trim();
        if (normalizedValue.length() > 80) {
            throw new InvalidEntityDataException("El distrito no puede superar 80 caracteres");
        }
        this.district = normalizedValue;
    }

    public void markAsDefault() {
        this.isDefault = true;
    }

    public void removeDefault() {
        this.isDefault = false;
    }

}
