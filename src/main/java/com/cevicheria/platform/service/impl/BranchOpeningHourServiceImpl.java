package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.BranchOpeningHour;
import com.cevicheria.platform.model.id.BranchOpeningHourId;
import com.cevicheria.platform.repository.BranchOpeningHourRepository;
import com.cevicheria.platform.repository.BranchRepository;
import com.cevicheria.platform.service.BranchOpeningHourService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BranchOpeningHourServiceImpl implements BranchOpeningHourService {

    private final BranchOpeningHourRepository branchOpeningHourRepository;
    private final BranchRepository branchRepository;

    @Override
    public List<BranchOpeningHour> getByBranch(Long businessId, Long branchId) {
        return branchOpeningHourRepository.findByIdBusinessIdAndIdBranchIdOrderByIdWeekdayAscIdShiftNumberAsc(businessId, branchId);
    }

    @Override
    public BranchOpeningHour getById(BranchOpeningHourId id) {
        return branchOpeningHourRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BranchOpeningHour", "ID", id));
    }

    @Override
    @Transactional
    public BranchOpeningHour save(BranchOpeningHour branchOpeningHour) {
        var id = branchOpeningHour.getId();
        branchRepository.findByIdAndBusinessId(id.getBranchId(), id.getBusinessId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch", "ID", id.getBranchId()));
        if (branchOpeningHourRepository.existsById(id)) {
            throw new InvalidEntityDataException("Ya existe un horario para ese día y turno");
        }
        return branchOpeningHourRepository.save(branchOpeningHour);
    }

    @Override
    @Transactional
    public BranchOpeningHour update(BranchOpeningHourId id, BranchOpeningHour branchOpeningHour) {
        BranchOpeningHour current = getById(id);
        if (!id.equals(branchOpeningHour.getId())) {
            throw new InvalidEntityDataException("No se puede cambiar la clave de un horario");
        }
        current.changeHours(branchOpeningHour.getOpensAt(), branchOpeningHour.getClosesAt());
        return branchOpeningHourRepository.save(current);
    }

    @Override
    @Transactional
    public void deleteById(BranchOpeningHourId id) {
        branchOpeningHourRepository.delete(getById(id));
    }
}
