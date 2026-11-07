package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.Business;

public interface BusinessService {

    List<Business> getAll();
    
    List<Business> getActive();
    
    Business getById(Long id);
    
    Business save(Business business);
    
    Business update(Long id, Business business);
    
    void deleteById(Long id);
}
