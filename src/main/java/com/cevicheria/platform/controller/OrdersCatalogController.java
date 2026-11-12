package com.cevicheria.platform.controller;

import com.cevicheria.platform.dto.*;
import com.cevicheria.platform.service.OrdersCatalogService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders-catalog")
public class OrdersCatalogController {
    private final OrdersCatalogService service;

    @GetMapping("/areas")
    public List<OrdersAreaView> areas(@RequestParam Long businessId, @RequestParam Long branchId) {
        return service.areas(businessId, branchId);
    }

    @GetMapping("/areas/{id}")
    public OrdersAreaView area(@PathVariable Long id, @RequestParam Long businessId, @RequestParam Long branchId) {
        return service.area(businessId, branchId, id);
    }

    @PostMapping("/areas")
    public ResponseEntity<OrdersAreaView> createArea(@RequestBody OrdersAreaRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createArea(r));
    }

    @PutMapping("/areas/{id}")
    public OrdersAreaView updateArea(@PathVariable Long id, @RequestBody OrdersAreaRequest r) {
        return service.updateArea(id, r);
    }

    @DeleteMapping("/areas/{id}")
    public ResponseEntity<Void> deactivateArea(@PathVariable Long id, @RequestParam Long businessId,
            @RequestParam Long branchId) {
        service.deactivateArea(businessId, branchId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/areas/{id}/kds")
    public OrdersKdsView kds(@PathVariable Long id, @RequestParam Long businessId, @RequestParam Long branchId) {
        return service.kds(businessId, branchId, id);
    }

    @PutMapping("/areas/{id}/kds")
    public OrdersKdsView configureKds(@PathVariable Long id, @RequestParam Long businessId,
            @RequestParam Long branchId, @RequestBody OrdersKdsRequest r) {
        return service.configureKds(businessId, branchId, id, r);
    }

    @GetMapping("/tables")
    public List<OrdersTableView> tables(@RequestParam Long businessId, @RequestParam Long branchId) {
        return service.tables(businessId, branchId);
    }

    @GetMapping("/tables/{id}")
    public OrdersTableView table(@PathVariable Long id, @RequestParam Long businessId, @RequestParam Long branchId) {
        return service.table(businessId, branchId, id);
    }

    @PostMapping("/tables")
    public ResponseEntity<OrdersTableView> createTable(@RequestBody OrdersTableRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createTable(r));
    }

    @PutMapping("/tables/{id}")
    public OrdersTableView updateTable(@PathVariable Long id, @RequestBody OrdersTableRequest r) {
        return service.updateTable(id, r);
    }

    @DeleteMapping("/tables/{id}")
    public ResponseEntity<Void> deactivateTable(@PathVariable Long id, @RequestParam Long businessId,
            @RequestParam Long branchId) {
        service.deactivateTable(businessId, branchId, id);
        return ResponseEntity.noContent().build();
    }
}
