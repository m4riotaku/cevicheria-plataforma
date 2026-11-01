# Delivery

Recursos: deliveries, delivery-zones, reservations y condiciones de cancelación.

GET de colección filtra por businessId y branchId.
POST crea (201); GET por ID consulta; PATCH actualiza campos presentes (200);
DELETE elimina (204). PATCH conserva campos omitidos o null.
Las relaciones estructurales no se pueden cambiar. Las respuestas usan DTOs con IDs.
Datos inválidos devuelven 400, registros inexistentes 404 y conflictos 409.
Las condiciones usan /reservations/{reservationId}/cancellation-terms,
con businessId y branchId: GET consulta, PUT reemplaza (null borra) y DELETE elimina.
Estados de delivery: PENDIENTE, ASIGNADO, EN_RUTA, ENTREGADO, FALLIDO.
Estados de reserva: PENDIENTE, CONFIRMADA, ATENDIDA, CANCELADA, NO_ASISTIO.

Las operaciones son CRUD; no automatizan inventario, pagos ni transiciones de estado.
businessId delimita los datos y no reemplaza la autorización del usuario.
Las pruebas MockMvc ejecutan controladores y servicios reales sobre H2,
sin filtros de autenticación. No equivalen a una prueba de la aplicación con MySQL.
