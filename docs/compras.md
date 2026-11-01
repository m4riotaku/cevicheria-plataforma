# Compras

Recursos: purchases, purchase-items, purchase-installments, purchase-orders,
purchase-order-items, suppliers, supplier-ingredients, supply-schedules,
supplier-categories, supplier-brands y asociaciones de categorías/marcas.

GET de colección filtra por businessId; los recursos de sucursal también reciben branchId.
POST crea (201); GET por ID consulta; PATCH actualiza campos presentes (200);
DELETE elimina (204). PATCH conserva campos omitidos o null.
Las relaciones estructurales no se pueden cambiar. Las respuestas usan DTOs con IDs.
Datos inválidos devuelven 400, registros inexistentes 404 y conflictos 409.
Las asociaciones usan /suppliers/{supplierId}/categories o /brands;
POST/DELETE añaden /{targetId}. Todas reciben businessId.

Las operaciones son CRUD; no automatizan inventario, pagos ni transiciones de estado.
businessId delimita los datos y no reemplaza la autorización del usuario.
Las pruebas MockMvc ejecutan controladores y servicios reales sobre H2,
sin filtros de autenticación. No equivalen a una prueba de la aplicación con MySQL.
