package com.cevicheria.platform.controller;

import com.cevicheria.platform.dto.SecurityCatalogRequest;
import com.cevicheria.platform.dto.SecurityCatalogResponse;
import com.cevicheria.platform.service.SecurityCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SecurityCatalogController
{

    private final SecurityCatalogService service;

    @GetMapping("/security/permissions")
    public List<SecurityCatalogResponse> permissions()
    {
        return service.permissions();
    }

    @GetMapping("/security/permissions/{id}")
    public SecurityCatalogResponse permission(@PathVariable Long id)
    {
        return service.permission(id);
    }

    @PostMapping("/security/permissions")
    public ResponseEntity<SecurityCatalogResponse> createPermission(@RequestBody SecurityCatalogRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createPermission(request));
    }

    @PatchMapping("/security/permissions/{id}")
    public SecurityCatalogResponse updatePermission(@PathVariable Long id, @RequestBody SecurityCatalogRequest request)
    {
        return service.updatePermission(id, request);
    }

    @DeleteMapping("/security/permissions/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id)
    {
        service.deletePermission(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/operational-functions")
    public List<SecurityCatalogResponse> functions()
    {
        return service.functions();
    }

    @GetMapping("/operational-functions/{id}")
    public SecurityCatalogResponse function(@PathVariable Long id)
    {
        return service.function(id);
    }

    @PostMapping("/operational-functions")
    public ResponseEntity<SecurityCatalogResponse> createFunction(@RequestBody SecurityCatalogRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createFunction(request));
    }

    @PatchMapping("/operational-functions/{id}")
    public SecurityCatalogResponse updateFunction(@PathVariable Long id, @RequestBody SecurityCatalogRequest request)
    {
        return service.updateFunction(id, request);
    }

    @DeleteMapping("/operational-functions/{id}")
    public ResponseEntity<Void> deleteFunction(@PathVariable Long id)
    {
        service.deleteFunction(id);
        return ResponseEntity.noContent().build();
    }
}
