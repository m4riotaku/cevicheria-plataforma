package com.cevicheria.platform.controller;

import com.cevicheria.platform.dto.BusinessRoleRequest;
import com.cevicheria.platform.dto.BusinessRoleResponse;
import com.cevicheria.platform.service.BusinessRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/businesses/{businessId}/business-roles")
public class BusinessRoleController
{
    private final BusinessRoleService service;

    @GetMapping
    public List<BusinessRoleResponse> list(@PathVariable Long businessId)
    {
        return service.list(businessId);
    }

    @PostMapping
    public ResponseEntity<BusinessRoleResponse> assign(@PathVariable Long businessId,
                                                       @RequestBody BusinessRoleRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.assign(businessId, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long businessId, @PathVariable Long id)
    {
        service.deactivate(businessId, id);
        return ResponseEntity.noContent().build();
    }
}
