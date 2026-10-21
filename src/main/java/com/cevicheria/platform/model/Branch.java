
package com.cevicheria.platform.model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.generator.EventType;
import org.hibernate.type.SqlTypes;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Branch
{
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

    @Getter(AccessLevel.NONE)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> schedule;

    @Getter(AccessLevel.NONE)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> settings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsible_employee_id")
    private Employee responsibleEmployee;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public Branch(
            Business business,
            String nameBranch,
            String addressBranch,
            String ubigeoCodeBranch,
            String departmentBranch,
            String provinceBranch,
            String districtBranch,
            String phoneBranch,
            String menuSlugBranch,
            Employee responsibleEmployeeBranch)
    {
        changeBusiness(business);
        changeNameBranch(nameBranch);
        changeAddress(addressBranch);
        changeUbigeoCode(ubigeoCodeBranch);
        changeDepartment(departmentBranch);
        changeProvince(provinceBranch);
        changeDistrict(districtBranch);
        changePhone(phoneBranch);
        changeMenuSlug(menuSlugBranch);
        changeResponsibleEmployee(responsibleEmployeeBranch);
    }

    public void changeBusiness(Business business)
    {
        if (business == null)
        {
            throw new InvalidEntityDataException("Se debe ingresar un negocio para la sucursal");
        }

        if (this.id != null)
        {
            throw new InvalidEntityDataException("No se puede cambiar el negocio de una sucursal registrada");
        }

        this.business = business;
    }

    public void changeNameBranch(String nameBranch)
    {
        if (nameBranch == null || nameBranch.isBlank())
        {
            throw new InvalidEntityDataException("El nombre de la sucursal es obligatorio");
        }

        String normalizedName = nameBranch.trim();

        if (normalizedName.length() > 120)
        {
            throw new InvalidEntityDataException("El nombre de la sucursal no puede superar los 120 caracteres");
        }

        this.name = normalizedName;
    }

    public void changeAddress(String addressBranch)
    {
        if (addressBranch == null || addressBranch.isBlank())
        {
            throw new InvalidEntityDataException("La direccion de la sucursal es obligatoria");
        }

        if (addressBranch.trim().length() > 250)
        {
            throw new InvalidEntityDataException("La direccion no puede superar los 250 caracteres");
        }

        this.address = addressBranch;
    }

    public void changeUbigeoCode(String ubigeoCodeBranch)
    {
        if (ubigeoCodeBranch == null || ubigeoCodeBranch.isBlank())
        {
            this.ubigeoCode = null;
            return;
        }

        if (!ubigeoCodeBranch.trim().matches("[0-9]{6}"))
        {
            throw new InvalidEntityDataException("El codigo de ubigeo debe contener exactamente 6 digitos numericos");
        }

        this.ubigeoCode = ubigeoCodeBranch;
    }

    public void changeDepartment(String departmentBranch)
    {
        if (departmentBranch == null || departmentBranch.isBlank())
        {
            this.department = null;
            return;
        }

        if (departmentBranch.trim().length() > 80)
        {
            throw new InvalidEntityDataException("El departamento no puede superar los 80 caracteres");
        }
    }

    public void changeProvince(String provinceBranch)
    {
        if (provinceBranch == null || provinceBranch.isBlank())
        {
            this.province = null;
            return;
        }

        if (provinceBranch.trim().length() > 80)
        {
            throw new InvalidEntityDataException("La provincia no puede superar los 80 caracteres");
        }

        this.province = provinceBranch;
    }

    public void changeDistrict(String districtBranch)
    {
        if (districtBranch == null || districtBranch.isBlank())
        {
            this.district = null;
            return;
        }

        if (districtBranch.trim().length() > 80)
        {
            throw new InvalidEntityDataException("El distrito no puede superar los 80 caracteres");
        }

        this.district = districtBranch;
    }

    public void changePhone(String phoneBranch)
    {
        if (phoneBranch == null || phoneBranch.isBlank())
        {
            this.phone = null;
            return;
        }

        if (!phoneBranch.trim().matches("[0-9]{9}"))
        {
            throw new InvalidEntityDataException("El celular debe contener exactamente 9 digitos numericos");
        }

        this.phone = phoneBranch;
    }

    public void changeTimezone(String newTimezone)
    {
        if (newTimezone == null || newTimezone.isBlank())
        {
            throw new InvalidEntityDataException("La zona horaria es obligatoria");
        }

        if (newTimezone.trim().length() > 50)
        {
            throw new InvalidEntityDataException("La zona horaria no puede superar los 50 caracteres");
        }

        this.timezone = newTimezone;
    }

    public void changeMenuSlug(String menuSlugBranch)
    {
        if (menuSlugBranch == null || menuSlugBranch.isBlank())
        {
            this.menuSlug = null;
            return;
        }

        String normalizedSlug = menuSlugBranch.trim().toLowerCase();

        if (normalizedSlug.length() > 100)
        {
            throw new InvalidEntityDataException("El slug del menu no puede superar los 100 caracteres");
        }

        if (!normalizedSlug.matches("[a-z0-9]+(-[a-z0-9]+)*"))
        {
            throw new InvalidEntityDataException("El slug solo admite letras minusculas, numeros y guiones");
        }

        this.menuSlug = normalizedSlug;
    }

    public void changeResponsibleEmployee(Employee employee)
    {
        this.responsibleEmployee = employee;
    }

    public void removeResponsibleEmployee()
    {
        this.responsibleEmployee = null;
    }

    public Map<String, Object> getSchedule()
    {
        return schedule == null ? null : Collections.unmodifiableMap(schedule);
    }

    public Map<String, Object> getSettings()
    {
        return settings == null ? null : Collections.unmodifiableMap(settings);
    }

    public void changeSchedule(Map<String, Object> newSchedule)
    {
        this.schedule = newSchedule == null ? null : new LinkedHashMap<>(newSchedule);
    }

    public void changeSettings(Map<String, Object> newSettings)
    {
        this.settings = newSettings == null ? null : new LinkedHashMap<>(newSettings);
    }

    public void activate()
    {
        this.active = true;
    }

    public void deactivate()
    {
        this.active = false;
    }

    public void enableDineIn()
    {
        this.dineInEnabled = true;
    }

    public void disableDineIn()
    {
        if (!takeoutEnabled && !deliveryEnabled)
        {
            throw new InvalidEntityDataException("La sucursal debe mantener al menos un canal de atencion habilitado");
        }

        this.dineInEnabled = false;
    }

    public void enableTakeout()
    {
        this.takeoutEnabled = true;
    }

    public void disableTakeout()
    {
        if (!dineInEnabled && !deliveryEnabled)
        {
            throw new InvalidEntityDataException("La sucursal debe mantener al menos un canal de atencion habilitado");
        }

        this.takeoutEnabled = false;
    }

    public void enableDelivery()
    {
        this.deliveryEnabled = true;
    }

    public void disableDelivery()
    {
        if (!dineInEnabled && !takeoutEnabled)
        {
            throw new InvalidEntityDataException("La sucursal debe mantener al menos un canal de atencion habilitado");
        }

        this.deliveryEnabled = false;
    }

    public void enableReservations()
    {
        this.reservationsEnabled = true;
    }

    public void disableReservations()
    {
        this.reservationsEnabled = false;
    }

    public void enablePublicMenu()
    {
        this.publicMenuEnabled = true;
    }

    public void disablePublicMenu()
    {
        this.publicMenuEnabled = false;
    }
}
