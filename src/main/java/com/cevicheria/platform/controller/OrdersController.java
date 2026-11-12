package com.cevicheria.platform.controller;

import com.cevicheria.platform.dto.*;
import com.cevicheria.platform.service.OrdersService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrdersController {
    private final OrdersService service;

    @GetMapping
    public List<OrdersView> orders(@RequestParam Long businessId, @RequestParam Long branchId) {
        return service.orders(businessId, branchId);
    }

    @GetMapping("/{id}")
    public OrdersView order(@PathVariable Long id, @RequestParam Long businessId, @RequestParam Long branchId) {
        return service.order(businessId, branchId, id);
    }

    @PostMapping
    public ResponseEntity<OrdersView> create(@RequestBody OrdersCreateRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createOrder(r));
    }

    @PutMapping("/{id}")
    public OrdersView update(@PathVariable Long id, @RequestParam Long businessId, @RequestParam Long branchId,
            @RequestBody OrdersUpdateRequest r) {
        return service.updateOrder(businessId, branchId, id, r);
    }

    @PutMapping("/{id}/confirm")
    public OrdersView confirm(@PathVariable Long id, @RequestParam Long businessId, @RequestParam Long branchId) {
        return service.confirm(businessId, branchId, id);
    }

    @PutMapping("/{id}/close")
    public OrdersView close(@PathVariable Long id, @RequestParam Long businessId, @RequestParam Long branchId) {
        return service.close(businessId, branchId, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id, @RequestParam Long businessId,
            @RequestParam Long branchId, @RequestBody(required = false) OrdersCancelRequest r) {
        service.cancel(businessId, branchId, id, r);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{orderId}/items")
    public List<OrdersItemView> items(@PathVariable Long orderId, @RequestParam Long businessId,
            @RequestParam Long branchId) {
        return service.items(businessId, branchId, orderId);
    }

    @GetMapping("/items/{id}")
    public OrdersItemView item(@PathVariable Long id, @RequestParam Long businessId, @RequestParam Long branchId) {
        return service.item(businessId, branchId, id);
    }

    @PostMapping("/items")
    public ResponseEntity<OrdersItemView> addItem(@RequestBody OrdersItemRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addItem(r));
    }

    @PutMapping("/items/{id}")
    public OrdersItemView updateItem(@PathVariable Long id, @RequestParam Long businessId,
            @RequestParam Long branchId, @RequestBody OrdersItemUpdateRequest r) {
        return service.updateItem(businessId, branchId, id, r);
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> cancelItem(@PathVariable Long id, @RequestParam Long businessId,
            @RequestParam Long branchId) {
        service.cancelItem(businessId, branchId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/items/{id}/modifiers")
    public List<OrdersModifierView> modifiers(@PathVariable Long id, @RequestParam Long businessId,
            @RequestParam Long branchId) {
        return service.modifiers(businessId, branchId, id);
    }

    @PostMapping("/items/modifiers")
    public ResponseEntity<OrdersModifierView> addModifier(@RequestBody OrdersModifierRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addModifier(r));
    }

    @DeleteMapping("/items/modifiers/{id}")
    public ResponseEntity<Void> removeModifier(@PathVariable Long id, @RequestParam Long businessId,
            @RequestParam Long branchId) {
        service.removeModifier(businessId, branchId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/items/{id}/recipe-snapshot")
    public OrdersRecipeSnapshotView snapshot(@PathVariable Long id, @RequestParam Long businessId,
            @RequestParam Long branchId) {
        return service.snapshot(businessId, branchId, id);
    }

    @GetMapping("/items/{id}/recipe-ingredients")
    public List<OrdersRecipeIngredientView> recipeIngredients(@PathVariable Long id, @RequestParam Long businessId,
            @RequestParam Long branchId) {
        return service.recipeIngredients(businessId, branchId, id);
    }
}
