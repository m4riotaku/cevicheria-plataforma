package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    private String firstNames;

    private String lastNames;

    private String documentType;

    private String documentNumber;

    private String phone;

    private String email;

    private LocalDate hireDate;

    private LocalDate terminationDate;

    private boolean active = true;

    private String notes;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public Employee(Business business, String firstNames, String lastNames, String documentType, String documentNumber, LocalDate hireDate) {
        changeBusiness(business);
        changeFirstNames(firstNames);
        changeLastNames(lastNames);
        changeDocumentType(documentType);
        changeDocumentNumber(documentNumber);
        changeHireDate(hireDate);
    }

    private void changeBusiness(Business value) {
        if (value == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        this.business = value;
    }

    public void changeFirstNames(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El nombres es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El nombres no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 100) {
            throw new InvalidEntityDataException("El nombres no puede superar 100 caracteres");
        }
        this.firstNames = normalized;
    }

    public void changeLastNames(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El apellidos es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El apellidos no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 100) {
            throw new InvalidEntityDataException("El apellidos no puede superar 100 caracteres");
        }
        this.lastNames = normalized;
    }

    public void changeDocumentType(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El tipo de documento es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El tipo de documento no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 10) {
            throw new InvalidEntityDataException("El tipo de documento no puede superar 10 caracteres");
        }
        this.documentType = normalized;
    }

    public void changeDocumentNumber(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El número de documento es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El número de documento no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 20) {
            throw new InvalidEntityDataException("El número de documento no puede superar 20 caracteres");
        }
        this.documentNumber = normalized;
    }

    public void changePhone(String value) {
        if (value == null || value.isBlank()) {
            this.phone = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 30) {
            throw new InvalidEntityDataException("El teléfono no puede superar 30 caracteres");
        }
        this.phone = normalized;
    }

    public void changeEmail(String value) {
        if (value == null || value.isBlank()) {
            this.email = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 150) {
            throw new InvalidEntityDataException("El correo electrónico no puede superar 150 caracteres");
        }
        if (!normalized.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new InvalidEntityDataException("El correo electrónico no tiene un formato válido");
        }
        this.email = normalized;
    }

    public void changeHireDate(LocalDate value) {
        if (value == null) {
            throw new InvalidEntityDataException("La hire fecha es obligatoria");
        }
        this.hireDate = value;
    }

    public void changeTerminationDate(LocalDate value) {
        if (value != null && this.hireDate != null && value.isBefore(this.hireDate)) {
            throw new InvalidEntityDataException("La fecha de cese no puede ser anterior a la contratación");
        }
        this.terminationDate = value;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void changeNotes(String value) {
        if (value == null || value.isBlank()) {
            this.notes = null;
            return;
        }
        String normalized = value.trim();
        this.notes = normalized;
    }

    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private List<EmployeeBranchAssignment> branchAssignments = new ArrayList<>();

    public List<EmployeeBranchAssignment> getBranchAssignments() {
        return List.copyOf(this.branchAssignments);
    }
}