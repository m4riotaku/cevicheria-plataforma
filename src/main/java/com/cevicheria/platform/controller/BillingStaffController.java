package com.cevicheria.platform.controller;

import com.cevicheria.platform.dto.IncentiveRequest;
import com.cevicheria.platform.dto.IncentiveView;
import com.cevicheria.platform.dto.SettlementRequest;
import com.cevicheria.platform.dto.SettlementView;
import com.cevicheria.platform.service.BillingStaffService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/billing")
public class BillingStaffController {
    private final BillingStaffService service;
    @GetMapping("/settlements")
    public List<SettlementView> settlements(@RequestParam Long businessId, @RequestParam Long branchId) {
        return service.settlements(businessId, branchId);
    }
    @GetMapping("/settlements/{id}")
    public SettlementView settlement(@RequestParam Long businessId, @RequestParam Long branchId, @PathVariable Long id) {
        return service.settlement(businessId, branchId, id);
    }
    @PostMapping("/settlements")
    public ResponseEntity<SettlementView> createSettlement(@RequestBody SettlementRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createSettlement(r));
    }
    @PutMapping("/settlements/{id}/approve")
    public SettlementView approve(@RequestParam Long businessId, @RequestParam Long branchId, @PathVariable Long id) {
        return service.approveSettlement(businessId, branchId, id);
    }
    @PutMapping("/settlements/{id}/paid")
    public SettlementView paid(@RequestParam Long businessId, @RequestParam Long branchId,
                               @PathVariable Long id, @RequestParam Long paymentId) {
        return service.paySettlement(businessId, branchId, id, paymentId);
    }
    @GetMapping("/incentives")
    public List<IncentiveView> incentives(@RequestParam Long businessId, @RequestParam Long branchId) {
        return service.incentives(businessId, branchId);
    }
    @GetMapping("/incentives/{id}")
    public IncentiveView incentive(@RequestParam Long businessId, @RequestParam Long branchId, @PathVariable Long id) {
        return service.incentive(businessId, branchId, id);
    }
    @PostMapping("/incentives")
    public ResponseEntity<IncentiveView> createIncentive(@RequestBody IncentiveRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createIncentive(r));
    }
}
