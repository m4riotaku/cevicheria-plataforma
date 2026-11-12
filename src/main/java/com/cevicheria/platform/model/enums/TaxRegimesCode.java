package com.cevicheria.platform.model.enums;

public enum TaxRegimesCode
{
    NRUS,
    RER,
    RMT,
    RG;

    public static boolean isValid(String value) {
        if (value == null) {
            return false;
        }

        for (TaxRegimesCode code : values()) {
            if (code.name().equalsIgnoreCase(value.trim())) {
                return true;
            }
        }
        return false;
    }
}
