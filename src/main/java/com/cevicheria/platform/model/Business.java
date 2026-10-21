
package com.cevicheria.platform.model;

import java.time.LocalDateTime;
import java.util.Map;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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
public class Business
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private boolean active = true;

    private String legalName;

    private String ruc;

    private String currencyCode = "PEN";

    private String businessFormat;

    private double taxRate = 0.18;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> settings;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public Business(String name, String legalName, String ruc, String businessFormat)
    {
        changeName(name);
        changeLegalName(legalName);
        changeRuc(ruc);
        changeBusinessFormat(businessFormat);
    }

    public void changeName(String newBusinessName)
    {
        if (newBusinessName == null || newBusinessName.isBlank())
        {
            throw new InvalidEntityDataException("El nombre del negocio es obligatorio");
        }

        if (newBusinessName.trim().length() > 120)
        {
            throw new InvalidEntityDataException("El nombre del negocio no puede superar los 120 caracteres");
        }

        this.name = newBusinessName;
    }

    public void changeLegalName(String newLegalName)
    {
        if (newLegalName == null || newLegalName.isBlank())
        {
            throw new InvalidEntityDataException("El nombre legal es obligatorio");
        }

        if (newLegalName.trim().length() > 200)
        {
            throw new InvalidEntityDataException("El nombre legal no puede superar los 200 caracteres");
        }

        this.legalName = newLegalName;
    }

    public void changeRuc(String newRuc)
    {
        if (newRuc == null || newRuc.isBlank())
        {
            throw new InvalidEntityDataException("El RUC es obligatorio");
        }

        if (!newRuc.trim().matches("[0-9]{11}"))
        {
            throw new InvalidEntityDataException("El RUC debe contener exactamente 11 digitos numericos");
        }

        this.ruc = newRuc;
    }

    public void changeBusinessFormat(String newBusinessFormat)
    {
        if (newBusinessFormat == null || newBusinessFormat.isBlank())
        {
            throw new InvalidEntityDataException("El formato del negocio es obligatorio");
        }

        if (newBusinessFormat.length() > 80)
        {
            throw new InvalidEntityDataException("El formato del negocio no puede superar los 80 caracteres");
        }

        this.businessFormat = newBusinessFormat;
    }

    public void activate()
    {
        this.active = true;
    }

    public void deactivate()
    {
        this.active = false;
    }
}
