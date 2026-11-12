package com.cevicheria.platform.service;
import com.cevicheria.platform.dto.BusinessRoleRequest;
import com.cevicheria.platform.dto.BusinessRoleResponse;
import java.util.List;

public interface BusinessRoleService
{
    List<BusinessRoleResponse> list(Long businessId);
    BusinessRoleResponse assign(Long businessId, BusinessRoleRequest request);
    void deactivate(Long businessId, Long id);
}
