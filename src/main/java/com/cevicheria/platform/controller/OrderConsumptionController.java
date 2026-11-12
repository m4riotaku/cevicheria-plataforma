package com.cevicheria.platform.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cevicheria.platform.model.OrderConsumption;
import com.cevicheria.platform.service.OrderConsumptionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/order-consumptions")
@RequiredArgsConstructor
public class OrderConsumptionController {
    private final OrderConsumptionService consumptionService;

    @GetMapping
    public List<OrderConsumption> getAll(@RequestParam Long businessId) { return consumptionService.getAllByBusiness(businessId); }

    @GetMapping("/order-item/{orderItemId}")
    public List<OrderConsumption> getByOrderItem(@RequestParam Long businessId, @PathVariable Long orderItemId) { return consumptionService.getByOrderItem(businessId, orderItemId); }

    @GetMapping("/ingredient/{ingredientId}")
    public List<OrderConsumption> getByIngredient(@RequestParam Long businessId, @PathVariable Long ingredientId) { return consumptionService.getByIngredient(businessId, ingredientId); }

    @GetMapping("/{id}")
    public OrderConsumption getById(@PathVariable Long id) { return consumptionService.getById(id); }

    @PostMapping
    public ResponseEntity<OrderConsumption> create(@RequestBody OrderConsumption consumption) { return ResponseEntity.status(HttpStatus.CREATED).body(consumptionService.save(consumption)); }

    @PutMapping("/{id}")
    public OrderConsumption update(@PathVariable Long id, @RequestBody OrderConsumption consumption) { return consumptionService.update(id, consumption); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { consumptionService.deleteById(id); return ResponseEntity.noContent().build(); }
}