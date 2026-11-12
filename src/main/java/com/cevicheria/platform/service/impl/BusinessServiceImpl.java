package com.cevicheria.platform.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.Business;
import com.cevicheria.platform.repository.BusinessRepository;
import com.cevicheria.platform.service.BusinessService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;

    @Override
    public List<Business> getAll() {
        return businessRepository.findAll();
    }

    @Override
    public List<Business> getActive() {
        return businessRepository.findByActiveTrue();
    }

    @Override
    public Business getById(Long id) {
        return businessRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Business", "ID", id));
    }

    @Override
    @Transactional
    public Business save(Business business) {
        if (business.getId() != null) {
            throw new InvalidEntityDataException("El registro ya tiene un identificador");
        }
        validateUnique(business, null);
        return businessRepository.save(business);
    }

    @Override
    @Transactional
    public Business update(Long id, Business business) {
        Business current = getById(id);
        validateUnique(business, id);
        current.changeName(business.getName());
        current.changeLegalName(business.getLegalName());
        current.changeRuc(business.getRuc());
        current.changeCurrencyCode(business.getCurrencyCode());
        current.changeBusinessFormat(business.getBusinessFormat());
        if (business.isActive()) {
            current.activate();
        } else {
            current.deactivate();
        }
        return businessRepository.save(current);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        businessRepository.delete(getById(id));
    }

    private void validateUnique(Business business, Long id) {
        if (businessRepository.findByName(business.getName())
                .filter(value -> !Objects.equals(value.getId(), id)).isPresent()) {
            throw new InvalidEntityDataException("Ya existe un negocio con ese nombre");
        }
        if (business.getRuc() != null && businessRepository.findByRuc(business.getRuc())
                .filter(value -> !Objects.equals(value.getId(), id)).isPresent()) {
            throw new InvalidEntityDataException("Ya existe un negocio con ese RUC");
        }
    }
}
