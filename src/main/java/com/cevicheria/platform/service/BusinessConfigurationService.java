package com.cevicheria.platform.service;

import com.cevicheria.platform.model.BusinessConfiguration;

public interface BusinessConfigurationService {

    BusinessConfiguration getById(Long businessId);
    
    BusinessConfiguration save(BusinessConfiguration businessConfiguration);
    
    BusinessConfiguration update(Long businessId, BusinessConfiguration businessConfiguration);
    
    void deleteById(Long businessId);
}
