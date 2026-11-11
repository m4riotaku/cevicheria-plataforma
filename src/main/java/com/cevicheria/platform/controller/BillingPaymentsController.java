package com.cevicheria.platform.controller;

import com.cevicheria.platform.dto.ApplicationRequest;
import com.cevicheria.platform.dto.ApplicationView;
import com.cevicheria.platform.dto.PaymentRequest;
import com.cevicheria.platform.dto.PaymentView;
import com.cevicheria.platform.service.BillingPaymentsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/billing")
public class BillingPaymentsController {
    private final BillingPaymentsService service;
    @GetMapping("/payments")
    public List<PaymentView> payments(@RequestParam Long businessId, @RequestParam Long branchId) {
        return service.payments(businessId, branchId);
    }
    @GetMapping("/payments/{id}")
    public PaymentView payment(@RequestParam Long businessId, @RequestParam Long branchId, @PathVariable Long id) {
        return service.payment(businessId, branchId, id);
    }
    @PostMapping("/payments")
    public ResponseEntity<PaymentView> createPayment(@RequestBody PaymentRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createPayment(r));
    }
    @PutMapping("/payments/{id}/verify")
    public PaymentView verifyPayment(@RequestParam Long businessId, @RequestParam Long branchId, @PathVariable Long id) {
        return service.verifyPayment(businessId, branchId, id);
    }
    @GetMapping("/payment-applications")
    public List<ApplicationView> applications(@RequestParam Long businessId, @RequestParam Long branchId) {
        return service.applications(businessId, branchId);
    }
    @GetMapping("/payment-applications/{id}")
    public ApplicationView application(@RequestParam Long businessId, @RequestParam Long branchId, @PathVariable Long id) {
        return service.application(businessId, branchId, id);
    }
    @PostMapping("/payment-applications")
    public ResponseEntity<ApplicationView> createApplication(@RequestBody ApplicationRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createApplication(r));
    }
    @PutMapping("/payment-applications/{id}/reverse")
    public ApplicationView reverseApplication(@RequestParam Long businessId, @RequestParam Long branchId, @PathVariable Long id) {
        return service.reverseApplication(businessId, branchId, id);
    }
}
