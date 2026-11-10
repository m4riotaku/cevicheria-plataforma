package com.cevicheria.platform.controller;

import com.cevicheria.platform.dto.AccountRequest;
import com.cevicheria.platform.dto.AccountView;
import com.cevicheria.platform.dto.RegisterRequest;
import com.cevicheria.platform.dto.RegisterView;
import com.cevicheria.platform.dto.RequirementRequest;
import com.cevicheria.platform.dto.RequirementView;
import com.cevicheria.platform.dto.RuleRequest;
import com.cevicheria.platform.dto.RuleView;
import com.cevicheria.platform.dto.SeriesRequest;
import com.cevicheria.platform.dto.SeriesView;
import com.cevicheria.platform.service.BillingConfigurationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/billing")
@RequiredArgsConstructor
public class BillingConfigurationController {
    private final BillingConfigurationService service;

    @GetMapping("/accounts")
    public List<AccountView> accounts(@RequestParam Long businessId, @RequestParam Long branchId) {
        return service.accounts(businessId, branchId);
    }
    @GetMapping("/accounts/{id}")
    public AccountView account(@RequestParam Long businessId, @RequestParam Long branchId, @PathVariable Long id) {
        return service.account(businessId, branchId, id);
    }
    @PostMapping("/accounts")
    public ResponseEntity<AccountView> createAccount(@RequestBody AccountRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createAccount(r));
    }
    @PutMapping("/accounts/{id}")
    public AccountView updateAccount(@PathVariable Long id, @RequestBody AccountRequest r) {
        return service.updateAccount(id, r);
    }
    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<Void> disableAccount(@PathVariable Long id, @RequestParam Long businessId, @RequestParam Long branchId) {
        service.disableAccount(businessId, branchId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/registers")
    public List<RegisterView> registers(@RequestParam Long businessId, @RequestParam Long branchId) {
        return service.registers(businessId, branchId);
    }
    @GetMapping("/registers/{id}")
    public RegisterView register(@RequestParam Long businessId, @RequestParam Long branchId, @PathVariable Long id) {
        return service.register(businessId, branchId, id);
    }
    @PostMapping("/registers")
    public ResponseEntity<RegisterView> createRegister(@RequestBody RegisterRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createRegister(r));
    }
    @PutMapping("/registers/{id}")
    public RegisterView updateRegister(@PathVariable Long id, @RequestBody RegisterRequest r) {
        return service.updateRegister(id, r);
    }
    @DeleteMapping("/registers/{id}")
    public ResponseEntity<Void> disableRegister(@PathVariable Long id, @RequestParam Long businessId, @RequestParam Long branchId) {
        service.disableRegister(businessId, branchId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/series")
    public List<SeriesView> series(@RequestParam Long businessId, @RequestParam Long branchId) {
        return service.series(businessId, branchId);
    }
    @GetMapping("/series/{id}")
    public SeriesView seriesOne(@RequestParam Long businessId, @RequestParam Long branchId, @PathVariable Long id) {
        return service.singleSeries(businessId, branchId, id);
    }
    @PostMapping("/series")
    public ResponseEntity<SeriesView> createSeries(@RequestBody SeriesRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createSeries(r));
    }
    @PutMapping("/series/{id}")
    public SeriesView updateSeries(@PathVariable Long id, @RequestBody SeriesRequest r) {
        return service.updateSeries(id, r);
    }
    @DeleteMapping("/series/{id}")
    public ResponseEntity<Void> disableSeries(@PathVariable Long id, @RequestParam Long businessId, @RequestParam Long branchId) {
        service.disableSeries(businessId, branchId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/commission-rules")
    public List<RuleView> rules(@RequestParam Long businessId) { return service.rules(businessId); }
    @GetMapping("/commission-rules/{id}")
    public RuleView rule(@RequestParam Long businessId, @PathVariable Long id) { return service.rule(businessId, id); }
    @PostMapping("/commission-rules")
    public ResponseEntity<RuleView> createRule(@RequestBody RuleRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createRule(r));
    }
    @PutMapping("/commission-rules/{id}")
    public RuleView updateRule(@PathVariable Long id, @RequestBody RuleRequest r) { return service.updateRule(id, r); }
    @DeleteMapping("/commission-rules/{id}")
    public ResponseEntity<Void> disableRule(@RequestParam Long businessId, @PathVariable Long id) {
        service.disableRule(businessId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/commission-rules/{id}/requirement")
    public RequirementView requirement(@RequestParam Long businessId, @PathVariable Long id) {
        return service.requirement(businessId, id);
    }
    @PutMapping("/commission-rules/{id}/requirement")
    public RequirementView saveRequirement(@PathVariable Long id, @RequestBody RequirementRequest request) {
        return service.saveRequirement(id, request);
    }
    @DeleteMapping("/commission-rules/{id}/requirement")
    public ResponseEntity<Void> deleteRequirement(@RequestParam Long businessId, @PathVariable Long id) {
        service.deleteRequirement(businessId, id);
        return ResponseEntity.noContent().build();
    }
}
