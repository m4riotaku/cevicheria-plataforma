package com.cevicheria.platform.service.impl;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.BusinessConfiguration;
import com.cevicheria.platform.repository.BusinessConfigurationRepository;
import com.cevicheria.platform.repository.BusinessRepository;
import com.cevicheria.platform.service.BusinessConfigurationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BusinessConfigurationServiceImpl implements BusinessConfigurationService {

    private final BusinessConfigurationRepository businessConfigurationRepository;
    private final BusinessRepository businessRepository;

    @Override
    public BusinessConfiguration getById(Long businessId) {
        return businessConfigurationRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("BusinessConfiguration", "ID", businessId));
    }

    @Override
    @Transactional
    public BusinessConfiguration save(BusinessConfiguration businessConfiguration) {
        if (businessConfiguration.getBusinessId() != null) {
            throw new InvalidEntityDataException("El registro ya tiene un identificador");
        }
        Long businessId = businessConfiguration.getBusiness().getId();
        if (businessId == null) {
            throw new InvalidEntityDataException("El negocio debe estar registrado");
        }
        businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business", "ID", businessId));
        if (businessConfigurationRepository.existsById(businessId)) {
            throw new InvalidEntityDataException("El negocio ya tiene una configuración");
        }
        return businessConfigurationRepository.save(businessConfiguration);
    }

    @Override
    @Transactional
    public BusinessConfiguration update(Long businessId, BusinessConfiguration businessConfiguration) {
        BusinessConfiguration current = getById(businessId);
        if (!Objects.equals(businessId, businessConfiguration.getBusiness().getId())) {
            throw new InvalidEntityDataException("No se puede cambiar el negocio de una configuración");
        }
        current.changeInvoiceEnvironment(businessConfiguration.getInvoiceEnvironment());
        current.changeBillingProvider(businessConfiguration.getBillingProvider());
        if (businessConfiguration.isReservationsEnabled()) current.enableReservations();
        else current.disableReservations();
        return businessConfigurationRepository.save(current);
    }

    @Override
    @Transactional
    public void deleteById(Long businessId) {
        businessConfigurationRepository.delete(getById(businessId));
    }
}
