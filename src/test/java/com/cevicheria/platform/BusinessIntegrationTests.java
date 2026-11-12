package com.cevicheria.platform;

import java.time.LocalTime;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.Branch;
import com.cevicheria.platform.model.BranchConfiguration;
import com.cevicheria.platform.model.BranchOpeningHour;
import com.cevicheria.platform.model.Business;
import com.cevicheria.platform.model.BusinessConfiguration;
import com.cevicheria.platform.model.enums.InvoiceEnvironment;
import com.cevicheria.platform.service.BranchConfigurationService;
import com.cevicheria.platform.service.BranchOpeningHourService;
import com.cevicheria.platform.service.BranchService;
import com.cevicheria.platform.service.BusinessConfigurationService;
import com.cevicheria.platform.service.BusinessService;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BusinessIntegrationTests {

    @Autowired BusinessService businesses;
    @Autowired BranchService branches;
    @Autowired BusinessConfigurationService businessConfigurations;
    @Autowired BranchConfigurationService branchConfigurations;
    @Autowired BranchOpeningHourService openingHours;
    @Autowired EntityManager entityManager;

    @Test
    void persistsChangesAndDeletesAllFiveResources() {
        var business = businesses.save(new Business("Cevichería", "Empresa", "20123456789", "RESTAURANTE"));
        Long businessId = business.getId();
        var branch = branches.save(new Branch(business, "Centro", "Jr. Lima 123"));
        Long branchId = branch.getId();
        businessConfigurations.save(new BusinessConfiguration(business, InvoiceEnvironment.PRUEBAS, "Proveedor"));
        var branchConfig = branchConfigurations.save(new BranchConfiguration(business, branch, 5));
        var hour = openingHours.save(new BranchOpeningHour(
                business, branch, 1, 1, LocalTime.of(9, 0), LocalTime.of(12, 0)));
        reload();

        var updatedBusiness = new Business("Cevichería Centro", "Empresa", null, "RESTAURANTE");
        businesses.update(businessId, updatedBusiness);
        var updatedBranch = new Branch(business, "Centro", "Jr. Lima 456");
        updatedBranch.enableDelivery();
        updatedBranch.disableDineIn();
        updatedBranch.disableTakeout();
        branches.update(businessId, branchId, updatedBranch);
        businessConfigurations.update(businessId,
                new BusinessConfiguration(business, InvoiceEnvironment.PRODUCCION, null));
        branchConfigurations.update(branchConfig.getId(), new BranchConfiguration(business, branch, null));
        openingHours.update(hour.getId(), new BranchOpeningHour(
                business, branch, 1, 1, LocalTime.of(14, 0), LocalTime.of(18, 0)));
        reload();

        assertNull(businesses.getById(businessId).getRuc());
        var persistedBranch = branches.getById(businessId, branchId);
        assertTrue(persistedBranch.isDeliveryEnabled());
        assertFalse(persistedBranch.isDineInEnabled());
        assertFalse(persistedBranch.isTakeoutEnabled());
        assertEquals(InvoiceEnvironment.PRODUCCION, businessConfigurations.getById(businessId).getInvoiceEnvironment());
        assertNull(branchConfigurations.getById(branchConfig.getId()).getExpirationAlertDays());
        assertEquals(LocalTime.of(14, 0), openingHours.getByBranch(businessId, branchId).getFirst().getOpensAt());

        openingHours.deleteById(hour.getId());
        branchConfigurations.deleteById(branchConfig.getId());
        businessConfigurations.deleteById(businessId);
        branches.deleteById(businessId, branchId);
        businesses.deleteById(businessId);
        reload();
        assertTrue(openingHours.getByBranch(businessId, branchId).isEmpty());
        assertTrue(branchConfigurations.getAllByBusiness(businessId).isEmpty());
        assertThrows(ResourceNotFoundException.class, () -> businessConfigurations.getById(businessId));
        assertTrue(branches.getAllByBusiness(businessId).isEmpty());
        assertThrows(ResourceNotFoundException.class, () -> businesses.getById(businessId));
    }

    @Test
    void anotherBusinessCannotReadBranchById() {
        var first = businesses.save(new Business("Primero", "Empresa", null, "RESTAURANTE"));
        var second = businesses.save(new Business("Segundo", "Empresa", null, "RESTAURANTE"));
        var branch = branches.save(new Branch(first, "Centro", "Jr. Lima 123"));

        assertThrows(ResourceNotFoundException.class, () -> branches.getById(second.getId(), branch.getId()));
    }

    @Test
    void creatingDuplicateOpeningHourCannotReplaceExistingShift() {
        var business = businesses.save(new Business("Cevichería", "Empresa", null, "RESTAURANTE"));
        var branch = branches.save(new Branch(business, "Centro", "Jr. Lima 123"));
        openingHours.save(new BranchOpeningHour(business, branch, 1, 1, LocalTime.of(9, 0), LocalTime.of(12, 0)));
        reload();

        assertThrows(InvalidEntityDataException.class, () -> openingHours.save(
                new BranchOpeningHour(business, branch, 1, 1, LocalTime.of(14, 0), LocalTime.of(18, 0))));
        assertEquals(LocalTime.of(9, 0),
                openingHours.getByBranch(business.getId(), branch.getId()).getFirst().getOpensAt());
    }

    private void reload() {
        entityManager.flush();
        entityManager.clear();
    }
}
