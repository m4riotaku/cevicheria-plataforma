package com.cevicheria.platform;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cevicheria.platform.controller.BranchConfigurationController;
import com.cevicheria.platform.controller.BranchController;
import com.cevicheria.platform.controller.BranchOpeningHourController;
import com.cevicheria.platform.controller.BusinessConfigurationController;
import com.cevicheria.platform.controller.BusinessController;
import com.cevicheria.platform.handlerException.BusinessExceptionHandler;
import com.cevicheria.platform.handlerException.GlobalExceptionHandler;

import tools.jackson.databind.json.JsonMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class BusinessControllerIntegrationTests {

    @Autowired ApplicationContext context;

    @Test
    void businessEndpointsExposeCrudAndUseScopedIdentifiers() throws Exception {
        MockMvc mvc = MockMvcBuilders.standaloneSetup(
                        context.getBean(BusinessController.class),
                        context.getBean(BranchController.class),
                        context.getBean(BusinessConfigurationController.class),
                        context.getBean(BranchConfigurationController.class),
                        context.getBean(BranchOpeningHourController.class))
                .setControllerAdvice(new GlobalExceptionHandler(), new BusinessExceptionHandler())
                .build();

        String businessBody = """
                {"name":"Cevichería API","legalName":"Empresa","businessFormat":"RESTAURANTE"}
                """;
        long businessId = idOf(mvc.perform(post("/businesses").contentType(MediaType.APPLICATION_JSON)
                .content(businessBody)).andExpect(status().isCreated()).andExpect(jsonPath("$.currencyCode").value("PEN")));
        String business = "/businesses/" + businessId;
        String scope = "?businessId=" + businessId;
        mvc.perform(put(business).contentType(MediaType.APPLICATION_JSON)
                        .content(businessBody.replace("Cevichería API", "Cevichería Actualizada")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Cevichería Actualizada"));
        mvc.perform(get("/businesses/active")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").isNumber());

        mvc.perform(post("/branches" + scope).contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Centro","address":"Jr. Lima 123","responsibleEmployeeId":999999}
                                """))
                .andExpect(status().isBadRequest());

        long branchId = idOf(mvc.perform(post("/branches" + scope).contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Centro","address":"Jr. Lima 123"}
                                """))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.businessId").value(businessId)));
        String branch = "/branches/" + branchId + scope;
        mvc.perform(get("/branches/" + branchId + "?businessId=" + (businessId + 1)))
                .andExpect(status().isNotFound());
        mvc.perform(put(branch).contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Centro","address":"Jr. Lima 456","dineInEnabled":false,
                                 "takeoutEnabled":false,"deliveryEnabled":true}
                                """))
                .andExpect(status().isOk()).andExpect(jsonPath("$.deliveryEnabled").value(true));
        mvc.perform(get(branch)).andExpect(status().isOk()).andExpect(jsonPath("$.address").value("Jr. Lima 456"));

        String businessConfig = "/business-configurations/" + businessId;
        mvc.perform(post("/business-configurations" + scope).contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.invoiceEnvironment").value("PRUEBAS"));
        mvc.perform(put(businessConfig).contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"invoiceEnvironment":"PRODUCCION","reservationsEnabled":true}
                                """))
                .andExpect(status().isOk()).andExpect(jsonPath("$.reservationsEnabled").value(true));
        mvc.perform(get(businessConfig)).andExpect(status().isOk())
                .andExpect(jsonPath("$.invoiceEnvironment").value("PRODUCCION"));

        String branchScope = scope + "&branchId=" + branchId;
        String branchConfig = "/branch-configurations/" + branchId + scope;
        mvc.perform(post("/branch-configurations" + branchScope).contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"expirationAlertDays":5}
                                """))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.branchId").value(branchId));
        mvc.perform(put(branchConfig).contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"expirationAlertDays":7}
                                """))
                .andExpect(status().isOk()).andExpect(jsonPath("$.expirationAlertDays").value(7));
        mvc.perform(get("/branch-configurations" + scope)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].expirationAlertDays").value(7));

        String hours = "/branch-opening-hours";
        String shift = hours + "/1/1" + branchScope;
        mvc.perform(post(hours + branchScope).contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"weekday":1,"opensAt":"09:00","closesAt":"12:00"}
                                """))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.shiftNumber").value(1));
        mvc.perform(put(shift).contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"opensAt":"14:00","closesAt":"18:00"}
                                """))
                .andExpect(status().isOk());
        mvc.perform(put(shift).contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"opensAt":"18:00","closesAt":"14:00"}
                                """))
                .andExpect(status().isBadRequest());
        mvc.perform(get(hours + branchScope)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].weekday").value(1));

        mvc.perform(delete(business)).andExpect(status().isConflict());
        for (String url : new String[]{shift, branchConfig, businessConfig, branch, business}) {
            mvc.perform(delete(url)).andExpect(status().isNoContent());
            mvc.perform(get(url)).andExpect(status().isNotFound());
        }
    }

    private long idOf(ResultActions result) throws Exception {
        return JsonMapper.builder().build().readTree(result.andReturn().getResponse().getContentAsByteArray())
                .get("id").asLong();
    }
}
