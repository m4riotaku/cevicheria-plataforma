package com.cevicheria.platform.delivery.model.enums;

public enum ReservationStatus {
    PENDIENTE,
    CONFIRMADA,
    SENTADA,    // El cliente ya llegó y está en la mesa
    COMPLETADA,
    CANCELADA,
    NO_SHOW     // El cliente no se presentó
}
