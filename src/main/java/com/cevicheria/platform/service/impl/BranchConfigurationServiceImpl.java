package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.BranchConfiguration;
import com.cevicheria.platform.model.id.BranchConfigurationId;
import com.cevicheria.platform.repository.BranchConfigurationRepository;
import com.cevicheria.platform.repository.BranchRepository;
import com.cevicheria.platform.service.BranchConfigurationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BranchConfigurationServiceImpl implements BranchConfigurationService {

    private final BranchConfigurationRepository branchConfigurationRepository;
    private final BranchRepository branchRepository;

    @Override
    public List<BranchConfiguration> getAllByBusiness(Long businessId) {
        return branchConfigurationRepository.findByIdBusinessId(businessId);
    }

    @Override
    public BranchConfiguration getById(BranchConfigurationId id) {
        return branchConfigurationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BranchConfiguration", "ID", id));
    }

    @Override
    @Transactional
    public BranchConfiguration save(BranchConfiguration branchConfiguration) {
        var id = branchConfiguration.getId();
        branchRepository.findByIdAndBusinessId(id.getBranchId(), id.getBusinessId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch", "ID", id.getBranchId()));
        if (branchConfigurationRepository.existsById(id)) {
            throw new InvalidEntityDataException("La sucursal ya tiene una configuración");
        }
        return branchConfigurationRepository.save(branchConfiguration);
    }

    @Override
    @Transactional
    public BranchConfiguration update(BranchConfigurationId id, BranchConfiguration branchConfiguration) {
        BranchConfiguration current = getById(id);
        if (!id.equals(branchConfiguration.getId())) {
            throw new InvalidEntityDataException("No se puede cambiar la sucursal de una configuración");
        }
        current.changeExpirationAlertDays(branchConfiguration.getExpirationAlertDays());
        if (branchConfiguration.isSaleByWeightEnabled()) current.enableSaleByWeight();
        else current.disableSaleByWeight();
        if (branchConfiguration.isStockAlertEnabled()) current.enableStockAlert();
        else current.disableStockAlert();
        return branchConfigurationRepository.save(current);
    }

    @Override
    @Transactional
    public void deleteById(BranchConfigurationId id) {
        branchConfigurationRepository.delete(getById(id));
    }
}
