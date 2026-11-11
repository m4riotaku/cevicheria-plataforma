package com.cevicheria.platform.controller;

import com.cevicheria.platform.dto.MovementRequest;
import com.cevicheria.platform.dto.MovementView;
import com.cevicheria.platform.dto.ShiftCloseRequest;
import com.cevicheria.platform.dto.ShiftRequest;
import com.cevicheria.platform.dto.ShiftView;
import com.cevicheria.platform.service.BillingCashService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/billing")
public class BillingCashController {
    private final BillingCashService service;

    @GetMapping("/shifts")
    public List<ShiftView> shifts(@RequestParam Long businessId, @RequestParam Long branchId) {
        return service.shifts(businessId, branchId);
    }
    @GetMapping("/shifts/{id}")
    public ShiftView shift(@RequestParam Long businessId, @RequestParam Long branchId, @PathVariable Long id) {
        return service.shift(businessId, branchId, id);
    }
    @PostMapping("/shifts")
    public ResponseEntity<ShiftView> open(@RequestBody ShiftRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.open(r));
    }
    @PutMapping("/shifts/{id}/close")
    public ShiftView close(@RequestParam Long businessId, @RequestParam Long branchId,
                           @PathVariable Long id, @RequestBody ShiftCloseRequest r) {
        return service.close(businessId, branchId, id, r);
    }
    @GetMapping("/movements")
    public List<MovementView> movements(@RequestParam Long businessId, @RequestParam Long branchId) {
        return service.movements(businessId, branchId);
    }
    @GetMapping("/movements/{id}")
    public MovementView movement(@RequestParam Long businessId, @RequestParam Long branchId, @PathVariable Long id) {
        return service.movement(businessId, branchId, id);
    }
    @PostMapping("/movements")
    public ResponseEntity<MovementView> recordMovement(@RequestBody MovementRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.recordMovement(r));
    }
}
