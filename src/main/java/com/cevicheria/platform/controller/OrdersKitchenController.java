package com.cevicheria.platform.controller;

import com.cevicheria.platform.dto.OrdersItemView;
import com.cevicheria.platform.dto.OrdersKitchenRequest;
import com.cevicheria.platform.service.OrdersKitchenService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders-kitchen")
public class OrdersKitchenController {
    private final OrdersKitchenService service;

    @GetMapping("/queue")
    public List<OrdersItemView> queue(@RequestParam Long businessId, @RequestParam Long branchId,
            @RequestParam Long areaId) {
        return service.queue(businessId, branchId, areaId);
    }

    @PutMapping("/items/{id}/status")
    public OrdersItemView update(@PathVariable Long id, @RequestParam Long businessId,
            @RequestParam Long branchId, @RequestBody OrdersKitchenRequest r) {
        return service.changeStatus(businessId, branchId, id, r);
    }
}
