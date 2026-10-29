package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.ComboOption;
import com.cevicheria.platform.repository.ComboOptionRepository;
import com.cevicheria.platform.service.ComboOptionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComboOptionServiceImpl implements ComboOptionService {

    private final ComboOptionRepository comboOptionRepository;

    @Override
    public List<ComboOption> getAllByBusiness(Long businessId) {
        return comboOptionRepository.findByBusinessId(businessId);
    }

    @Override
    public List<ComboOption> getByComboProduct(Long businessId, Long comboProductId) {
        return comboOptionRepository.findByBusinessIdAndComboProductId(businessId, comboProductId);
    }

    @Override
    public List<ComboOption> getActiveByComboProduct(Long businessId, Long comboProductId) {
        return comboOptionRepository.findByBusinessIdAndComboProductIdAndActiveTrue(businessId, comboProductId);
    }

    @Override
    public List<ComboOption> getByOptionProduct(Long businessId, Long optionProductId) {
        return comboOptionRepository.findByBusinessIdAndOptionProductId(businessId, optionProductId);
    }

    @Override
    public ComboOption getById(Long id) {
        return comboOptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ComboOption", "ID", id));
    }

    @Override
    public ComboOption save(ComboOption comboOption) {
        return comboOptionRepository.save(comboOption);
    }

    @Override
    public ComboOption update(Long id, ComboOption comboOption) {
        ComboOption current = getById(id);
        if (comboOption.getPositionNumber() != null) {
            current.changePositionNumber(comboOption.getPositionNumber());
        }
        if (comboOption.getPositionName() != null) {
            current.changePositionName(comboOption.getPositionName());
        }
        if (comboOption.getFraction() != null) {
            current.changeFraction(comboOption.getFraction());
        }
        if (comboOption.getSurcharge() != null) {
            current.changeSurcharge(comboOption.getSurcharge());
        }
        if (comboOption.isActive()) {
            current.activate();
        } else {
            current.deactivate();
        }
        return comboOptionRepository.save(current);
    }

    @Override
    public void deleteById(Long id) {
        comboOptionRepository.delete(getById(id));
    }
}