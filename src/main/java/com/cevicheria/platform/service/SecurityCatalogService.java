package com.cevicheria.platform.service;

import com.cevicheria.platform.dto.SecurityCatalogRequest;
import com.cevicheria.platform.dto.SecurityCatalogResponse;
import java.util.List;

public interface SecurityCatalogService {
    List<SecurityCatalogResponse> permissions();
    SecurityCatalogResponse permission(Long id);
    SecurityCatalogResponse createPermission(SecurityCatalogRequest request);
    SecurityCatalogResponse updatePermission(Long id, SecurityCatalogRequest request);
    void deletePermission(Long id);
    List<SecurityCatalogResponse> functions();
    SecurityCatalogResponse function(Long id);
    SecurityCatalogResponse createFunction(SecurityCatalogRequest request);
    SecurityCatalogResponse updateFunction(Long id, SecurityCatalogRequest request);
    void deleteFunction(Long id);
}
