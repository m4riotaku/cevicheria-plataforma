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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    private String name;

    private boolean active = true;

    private String address;

    private String ubigeoCode;

    private String department;

    private String province;

    private String district;

    private String phone;

    private String timezone = "America/Lima";

    private boolean dineInEnabled = true;

    private boolean takeoutEnabled = true;

    private boolean deliveryEnabled = false;

    private boolean reservationsEnabled = false;

    private boolean publicMenuEnabled = false;

    private String menuSlug;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "responsible_employee_id")
    private Employee responsibleEmployee;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public Branch(Business business, String name, String address) {
        changeBusiness(business);
        changeNameBranch(name);
        changeAddress(address);
    }

    private void changeBusiness(Business value) {
        if (value == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        this.business = value;
    }

    public void changeNameBranch(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El nombre de la sucursal es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El nombre de la sucursal no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 120) {
            throw new InvalidEntityDataException("El nombre de la sucursal no puede superar 120 caracteres");
        }
        this.name = normalized;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void changeAddress(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("La dirección es obligatoria");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("La dirección no puede estar vacía");
        }
        String normalized = value.trim();
        if (normalized.length() > 250) {
            throw new InvalidEntityDataException("La dirección no puede superar 250 caracteres");
        }
        this.address = normalized;
    }

    public void changeUbigeoCode(String value) {
        if (value == null || value.isBlank()) {
            this.ubigeoCode = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() != 6) {
            throw new InvalidEntityDataException("El código de ubigeo debe contener exactamente 6 caracteres");
        }
        if (!normalized.matches("[0-9]+")) {
            throw new InvalidEntityDataException("El código de ubigeo solo admite dígitos numéricos");
        }
        this.ubigeoCode = normalized;
    }

    public void changeDepartment(String value) {
        if (value == null || value.isBlank()) {
            this.department = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 80) {
            throw new InvalidEntityDataException("El departamento no puede superar 80 caracteres");
        }
        this.department = normalized;
    }

    public void changeProvince(String value) {
        if (value == null || value.isBlank()) {
            this.province = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 80) {
            throw new InvalidEntityDataException("La provincia no puede superar 80 caracteres");
        }
        this.province = normalized;
    }

    public void changeDistrict(String value) {
        if (value == null || value.isBlank()) {
            this.district = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 80) {
            throw new InvalidEntityDataException("El distrito no puede superar 80 caracteres");
        }
        this.district = normalized;
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

    public void changeTimezone(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("La zona horaria es obligatoria");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("La zona horaria no puede estar vacía");
        }
        String normalized = value.trim();
        if (normalized.length() > 50) {
            throw new InvalidEntityDataException("La zona horaria no puede superar 50 caracteres");
        }
        this.timezone = normalized;
    }

    public void enableDineIn() {
        this.dineInEnabled = true;
    }

    public void disableDineIn() {
        if (!this.takeoutEnabled && !this.deliveryEnabled) {
            throw new InvalidEntityDataException("El local debe mantener al menos un canal de atención habilitado");
        }
        this.dineInEnabled = false;
    }

    public void enableTakeout() {
        this.takeoutEnabled = true;
    }

    public void disableTakeout() {
        if (!this.dineInEnabled && !this.deliveryEnabled) {
            throw new InvalidEntityDataException("El local debe mantener al menos un canal de atención habilitado");
        }
        this.takeoutEnabled = false;
    }

    public void enableDelivery() {
        this.deliveryEnabled = true;
    }

    public void disableDelivery() {
        if (!this.dineInEnabled && !this.takeoutEnabled) {
            throw new InvalidEntityDataException("El local debe mantener al menos un canal de atención habilitado");
        }
        this.deliveryEnabled = false;
    }

    public void enableReservations() {
        this.reservationsEnabled = true;
    }

    public void disableReservations() {
        this.reservationsEnabled = false;
    }

    public void enablePublicMenu() {
        this.publicMenuEnabled = true;
    }

    public void disablePublicMenu() {
        this.publicMenuEnabled = false;
    }

    public void changeMenuSlug(String value) {
        if (value == null || value.isBlank()) {
            this.menuSlug = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 100) {
            throw new InvalidEntityDataException("El slug del menú no puede superar 100 caracteres");
        }
        String slug = normalized.toLowerCase();
        if (!slug.matches("[a-z0-9]+(-[a-z0-9]+)*")) {
            throw new InvalidEntityDataException("El slug solo admite letras minúsculas, números y guiones internos");
        }
        this.menuSlug = slug;
    }

    public void changeResponsibleEmployee(Employee value) {
        this.responsibleEmployee = value;
    }

    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY)
    private List<BranchOpeningHour> openingHours = new ArrayList<>();

    public List<BranchOpeningHour> getOpeningHours() {
        return List.copyOf(this.openingHours);
    }

}
