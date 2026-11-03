package com.cevicheria.platform;

import com.cevicheria.platform.controller.*;
import com.cevicheria.platform.handlerException.GlobalExceptionHandler;
import com.cevicheria.platform.handlerException.DeliveryExceptionHandler;
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
class DeliveryIntegrationTests {
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
                context.getBean(DeliveryController.class),
                context.getBean(DeliveryZoneController.class),
                context.getBean(ReservationController.class),
                context.getBean(ReservationCancellationTermsController.class))
                .setControllerAdvice(new GlobalExceptionHandler(), new DeliveryExceptionHandler()).build();
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
    @ValueSource(strings = {"deliveries", "delivery-zones", "reservations"})
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
    void deliveryStatusIsPersistedAsTextAndQueryableAsEnum() throws Exception {
        Map<String, Object> body = body("deliveries");
        body.put("status", "EN_RUTA");
        long id = create("deliveries", json.writeValueAsString(body)).get("id").asLong();
        em.clear();
        assertEquals(DeliveryStatus.EN_RUTA, deliveries.findById(id).orElseThrow().getStatus());
        assertEquals(1, deliveries.findByBusinessIdAndBranchIdAndStatus(
                business.getId(), branch.getId(), DeliveryStatus.EN_RUTA).size());
        assertEquals("EN_RUTA", em.createNativeQuery("select status from delivery where id = :id")
                .setParameter("id", id).getSingleResult());
        mvc.perform(get("/deliveries/filter/status").param("businessId", business.getId().toString())
                        .param("branchId", branch.getId().toString()).param("status", "EN_RUTA"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(id));
        mvc.perform(get("/deliveries/filter/status").param("businessId", business.getId().toString())
                        .param("branchId", branch.getId().toString()).param("status", "DESCONOCIDO"))
                .andExpect(status().isBadRequest());
        mvc.perform(patch("/deliveries/" + id).param("businessId", business.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"NO_EXISTE\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void reservationStatusIsQueryableAsEnum() throws Exception {
        Map<String, Object> body = body("reservations");
        body.put("status", "ATENDIDA");
        create("reservations", json.writeValueAsString(body));
        em.clear();
        assertEquals(1, reservations.findByBusinessIdAndBranchIdAndStatus(
                business.getId(), branch.getId(), ReservationStatus.ATENDIDA).size());
    }

    @Test
    void rejectsCrossBusinessAndCrossBranchRelations() throws Exception {
        Business other = persist(new Business("Otro", "Otro negocio", "20111111111", "RESTAURANTE"));
        Branch otherBranch = persist(new Branch(other, "Otro local", "Otra dirección"));
        Map<String, Object> invalid = body("deliveries");
        invalid.put("branchId", otherBranch.getId());
        mvc.perform(post("/deliveries").contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejectsAZoneFromAnotherBranchOfTheSameBusiness() throws Exception {
        Branch otherBranch = persist(new Branch(business, "Sur", "Otra dirección"));
        DeliveryZone otherZone = persist(new DeliveryZone(business, otherBranch, "Sur", 30));
        Map<String, Object> invalid = body("deliveries");
        invalid.put("deliveryZoneId", otherZone.getId());
        mvc.perform(post("/deliveries").contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejectsInvalidReservationPeriodOnPartialUpdate() throws Exception {
        long id = create("reservations", json.writeValueAsString(body("reservations"))).get("id").asLong();
        mvc.perform(patch("/reservations/" + id).param("businessId", business.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON).content("{\"startsAt\":\"2026-11-01T14:00:00\"}"))
                .andExpect(status().isBadRequest());
    }




    @Test
    void cancellationTermsReplaceAndClearOptionalFields() throws Exception {
        long reservationId = create("reservations", json.writeValueAsString(body("reservations"))).get("id").asLong();
        String url = "/reservations/" + reservationId + "/cancellation-terms";
        mvc.perform(put(url).param("businessId", business.getId().toString())
                        .param("branchId", branch.getId().toString()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullRefundBeforeHours\":24,\"noShowRetentionAmount\":20,\"policyNotes\":\"Condiciones\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.fullRefundBeforeHours").value(24));
        em.clear();
        mvc.perform(get(url).param("businessId", business.getId().toString())
                        .param("branchId", branch.getId().toString()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.policyNotes").value("Condiciones"));
        mvc.perform(get(url).param("businessId", business.getId().toString()).param("branchId", "999999"))
                .andExpect(status().isNotFound());
        mvc.perform(put(url).param("businessId", business.getId().toString())
                        .param("branchId", branch.getId().toString()).contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.fullRefundBeforeHours").doesNotExist());
        mvc.perform(delete(url).param("businessId", business.getId().toString())
                        .param("branchId", branch.getId().toString())).andExpect(status().isNoContent());
        mvc.perform(get(url).param("businessId", business.getId().toString())
                        .param("branchId", branch.getId().toString())).andExpect(status().isNotFound());
    }



    @Test
    void deliveryZoneNameIsUniqueAcrossBranchesOfBusiness() throws Exception {
        Branch otherBranch = persist(new Branch(business, "Sur", "Otra dirección"));
        Map<String, Object> payload = body("delivery-zones");
        payload.put("branchId", otherBranch.getId());
        payload.put("name", zone.getName());
        mvc.perform(post("/delivery-zones").contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(payload)))
                .andExpect(status().isConflict());
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
