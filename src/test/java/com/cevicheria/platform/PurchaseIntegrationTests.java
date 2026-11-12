package com.cevicheria.platform;

import com.cevicheria.platform.controller.*;
import com.cevicheria.platform.handlerException.GlobalExceptionHandler;
import com.cevicheria.platform.handlerException.PurchaseExceptionHandler;
import com.cevicheria.platform.model.*;
import com.cevicheria.platform.model.enums.*;
import com.cevicheria.platform.repository.DeliveryRepository;
import com.cevicheria.platform.repository.ReservationRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
class PurchaseIntegrationTests {
    @Autowired EntityManager em;
    @Autowired ApplicationContext context;
    @Autowired DeliveryRepository deliveries;
    @Autowired ReservationRepository reservations;
    private MockMvc mvc;
    private final JsonMapper json = JsonMapper.builder().build();
    private Business business;
    private Branch branch;
    private Warehouse warehouse;
    private Ingredient ingredient;
    private Supplier supplier;
    private Purchase purchase;
    private PurchaseOrder purchaseOrder;
    private CustomerOrder order;
    private DeliveryZone zone;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(
                context.getBean(PurchaseController.class),
                context.getBean(PurchaseItemController.class),
                context.getBean(PurchaseInstallmentController.class),
                context.getBean(PurchaseOrderController.class),
                context.getBean(PurchaseOrderItemController.class),
                context.getBean(SupplierController.class),
                context.getBean(SupplierIngredientController.class),
                context.getBean(SupplyScheduleController.class),
                context.getBean(SupplierCategoryController.class),
                context.getBean(SupplierBrandController.class),
                context.getBean(SupplierCategoryLinkController.class),
                context.getBean(SupplierBrandLinkController.class))
                .setControllerAdvice(new GlobalExceptionHandler(), new PurchaseExceptionHandler()).build();
        business = persist(new Business("Cevichería", "Empresa", "20123456789", "RESTAURANTE"));
        branch = persist(new Branch(business, "Centro", "Jr. Lima 123"));
        warehouse = persist(new Warehouse(business, branch, "Almacén"));
        ingredient = persist(new Ingredient(business, "Pescado", "P001", "KG"));
        supplier = persist(new Supplier(business, "Proveedor", "RUC", "20987654321"));
        purchaseOrder = persist(new PurchaseOrder(business, branch, supplier, warehouse, "OC-BASE", LocalDate.now()));
        purchase = persist(new Purchase(business, branch, supplier, PurchaseType.FACTURA, "F001", "BASE", LocalDate.now()));
        order = persist(new CustomerOrder(business, branch, "P001", "request-1", SalesChannel.DELIVERY, LocalDateTime.now()));
        zone = persist(new DeliveryZone(business, branch, "Centro", 20));
        em.flush();
    }

    @ParameterizedTest
    @ValueSource(strings = {"purchases", "purchase-items", "purchase-installments", "purchase-orders", "purchase-order-items", "suppliers", "supplier-ingredients", "supply-schedules", "supplier-categories", "supplier-brands"})
    void crudPersistsAndScopesEveryResource(String route) throws Exception {
        String payload = json.writeValueAsString(body(route));
        JsonNode created = create(route, payload);
        long id = created.get("id").asLong();
        String url = "/" + route + "/" + id;
        em.clear();
        mvc.perform(get(url).param("businessId", business.getId().toString()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id));
        mvc.perform(get(url).param("businessId", "-1")).andExpect(status().isBadRequest());
        mvc.perform(get(url).param("businessId", "999999")).andExpect(status().isNotFound());
        mvc.perform(patch(url).param("businessId", business.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
        Map<String, Object> update = updateBody(route);
        mvc.perform(patch(url).param("businessId", business.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + update.keySet().iterator().next()).value(update.values().iterator().next()));
        mvc.perform(get("/" + route).param("businessId", business.getId().toString())
                        .param("branchId", branch.getId().toString()))
                .andExpect(status().isOk()).andExpect(jsonPath("$").isArray());
        mvc.perform(delete(url).param("businessId", business.getId().toString()))
                .andExpect(status().isNoContent());
        mvc.perform(get(url).param("businessId", business.getId().toString()))
                .andExpect(status().isNotFound());
    }






    @Test
    void rejectsDuplicatePurchaseAndInvalidTotals() throws Exception {
        String payload = json.writeValueAsString(body("purchases"));
        create("purchases", payload);
        mvc.perform(post("/purchases").contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isConflict());
        Map<String, Object> invalid = body("purchases");
        invalid.put("number", "OTRO");
        invalid.put("subtotal", 100);
        invalid.put("tax", 18);
        invalid.put("total", 119);
        mvc.perform(post("/purchases").contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejectsMissingReferencesAndKeepsImmutableScope() throws Exception {
        Map<String, Object> invalid = body("purchases");
        invalid.put("supplierId", 999999);
        mvc.perform(post("/purchases").contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(invalid)))
                .andExpect(status().isNotFound());
        long id = create("purchases", json.writeValueAsString(body("purchases"))).get("id").asLong();
        mvc.perform(patch("/purchases/" + id).param("businessId", business.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON).content("{\"branchId\":999999}"))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"categories", "brands"})
    void supplierAssociationsPersistCompositeKeysAndRejectDuplicates(String kind) throws Exception {
        long targetId = create("supplier-" + kind, json.writeValueAsString(body("supplier-" + kind))).get("id").asLong();
        String url = "/suppliers/" + supplier.getId() + "/" + kind + "/" + targetId;
        mvc.perform(post(url).param("businessId", business.getId().toString()))
                .andExpect(status().isCreated());
        em.clear();
        mvc.perform(get("/suppliers/" + supplier.getId() + "/" + kind)
                        .param("businessId", business.getId().toString()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
        mvc.perform(post(url).param("businessId", business.getId().toString()))
                .andExpect(status().isConflict());
        mvc.perform(post(url).param("businessId", "999999"))
                .andExpect(status().isNotFound());
        mvc.perform(delete(url).param("businessId", business.getId().toString()))
                .andExpect(status().isNoContent());
    }


    @Test
    void supplierDocumentUniquenessIncludesDocumentType() throws Exception {
        Map<String, Object> payload = body("suppliers");
        payload.put("documentNumber", supplier.getDocumentNumber());
        payload.put("documentType", "DEMO");
        create("suppliers", json.writeValueAsString(payload));
        mvc.perform(post("/suppliers").contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(payload)))
                .andExpect(status().isConflict());
    }

    @ParameterizedTest
    @ValueSource(strings = {"supplier-categories", "supplier-brands"})
    void rejectsDuplicateCatalogNamesWithinBusiness(String route) throws Exception {
        String payload = json.writeValueAsString(body(route));
        create(route, payload);
        mvc.perform(post("/" + route).contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isConflict());
    }


    @Test
    void rejectsDuplicateInstallmentNumberForPurchase() throws Exception {
        String payload = json.writeValueAsString(body("purchase-installments"));
        create("purchase-installments", payload);
        mvc.perform(post("/purchase-installments").contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isConflict());
    }

    @Test
    void rejectsInstallmentWithoutAmount() throws Exception {
        Map<String, Object> payload = body("purchase-installments");
        payload.remove("amount");
        mvc.perform(post("/purchase-installments").contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void acceptsPurchaseItemWithExplicitZeroRejectedQuantity() throws Exception {
        Map<String, Object> payload = body("purchase-items");
        payload.put("rejectedBaseQuantity", 0);
        JsonNode created = create("purchase-items", json.writeValueAsString(payload));
        assertEquals(0, created.get("rejectedBaseQuantity").decimalValue().signum());
    }

    @ParameterizedTest
    @ValueSource(strings = {"total", "empty-quantity", "rejection-reason", "missing-tare", "excessive-tare"})
    void rejectsPurchaseItemsThatViolateSqlChecks(String scenario) throws Exception {
        Map<String, Object> payload = body("purchase-items");
        switch (scenario) {
            case "total" -> payload.putAll(Map.of("subtotal", 100, "tax", 18, "total", 119));
            case "empty-quantity" -> payload.put("acceptedBaseQuantity", 0);
            case "rejection-reason" -> payload.put("rejectedBaseQuantity", 1);
            case "missing-tare" -> payload.put("grossWeight", 3);
            case "excessive-tare" -> payload.putAll(Map.of("grossWeight", 3, "tareWeight", 4));
            default -> throw new IllegalArgumentException(scenario);
        }
        mvc.perform(post("/purchase-items").contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }

    private JsonNode create(String route, String payload) throws Exception {
        return json.readTree(mvc.perform(post("/" + route).contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString());
    }

    private <T> T persist(T entity) {
        em.persist(entity);
        return entity;
    }

    private Map<String, Object> body(String route) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("businessId", business.getId());
        if (!route.equals("suppliers") && !route.startsWith("supplier-")) body.put("branchId", branch.getId());
        switch (route) {
            case "purchases" -> body.putAll(Map.of("supplierId", supplier.getId(), "type", "FACTURA",
                    "series", "F001", "number", "NUEVA", "recordDate", "2026-10-31"));
            case "purchase-orders" -> body.putAll(Map.of("supplierId", supplier.getId(), "warehouseId", warehouse.getId(),
                    "number", "OC-NUEVA", "recordDate", "2026-10-31"));
            case "purchase-items" -> body.putAll(Map.of("purchaseId", purchase.getId(), "ingredientId", ingredient.getId(),
                    "warehouseId", warehouse.getId(), "presentationQuantity", 2, "acceptedBaseQuantity", 2));
            case "purchase-installments" -> body.putAll(Map.of("purchaseId", purchase.getId(), "number", 1,
                    "dueOn", "2026-11-15", "amount", 50));
            case "purchase-order-items" -> body.putAll(Map.of("purchaseOrderId", purchaseOrder.getId(),
                    "ingredientId", ingredient.getId(), "quantity", 2, "estimatedBaseQuantity", 2));
            case "suppliers" -> body.putAll(Map.of("name", "Nuevo proveedor", "documentType", "RUC", "documentNumber", "20222222222"));
            case "supplier-categories", "supplier-brands" -> body.put("name", "Catálogo");
            case "supplier-ingredients" -> body.putAll(Map.of("supplierId", supplier.getId(), "ingredientId", ingredient.getId()));
            case "supply-schedules" -> body.putAll(Map.of("warehouseId", warehouse.getId(), "ingredientId", ingredient.getId(),
                    "supplierId", supplier.getId(), "baseQuantity", 5, "frequencyDays", 7, "nextDate", "2026-11-07"));
            case "deliveries" -> body.putAll(Map.of("orderId", order.getId(), "deliveryZoneId", zone.getId(),
                    "driverName", "Repartidor", "address", "Jr. Lima 123", "estimatedMinutes", 20));
            case "delivery-zones" -> body.putAll(Map.of("name", "Nueva zona", "estimatedMinutes", 15));
            case "reservations" -> body.putAll(Map.of("contactName", "Cliente", "phone", "999888777",
                    "startsAt", "2026-11-01T12:00:00", "endsAt", "2026-11-01T13:00:00", "guestCount", 4));
            default -> throw new IllegalArgumentException(route);
        }
        return body;
    }

    private Map<String, Object> updateBody(String route) {
        return switch (route) {
            case "purchases", "purchase-orders", "reservations" -> Map.of("notes", "Actualizado");
            case "purchase-items" -> Map.of("rejectionReason", "Revisado");
            case "purchase-installments" -> Map.of("number", 2);
            case "purchase-order-items" -> Map.of("quantity", 3);
            case "suppliers", "delivery-zones", "supplier-categories", "supplier-brands" -> Map.of("name", "Actualizado");
            case "supplier-ingredients" -> Map.of("brand", "Actualizado");
            case "supply-schedules" -> Map.of("frequencyDays", 10);
            case "deliveries" -> Map.of("status", "EN_RUTA");
            default -> throw new IllegalArgumentException(route);
        };
    }
}
