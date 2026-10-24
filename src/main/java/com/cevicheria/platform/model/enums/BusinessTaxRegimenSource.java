package com.cevicheria.platform.model.enums;

public enum BusinessTaxRegimenSource
{
    DECLARACION_NEGOCIO,
    CONSULTA_SUNAT,
    VERIFICACION_MANUAL;

    public static boolean isValid(String value) {
        if (value == null) {
            return false;
        }

        for (BusinessTaxRegimenSource source : values()) {
            if (source.name().equalsIgnoreCase(value.trim())) {
                return true;
            }
        }
        return false;
    }
}
