package com.cevicheria.platform.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.Branch;
import com.cevicheria.platform.model.Employee;
import com.cevicheria.platform.repository.BranchRepository;
import com.cevicheria.platform.repository.BusinessRepository;
import com.cevicheria.platform.repository.EmployeeRepository;
import com.cevicheria.platform.service.BranchService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final BusinessRepository businessRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public List<Branch> getAllByBusiness(Long businessId) {
        return branchRepository.findByBusinessId(businessId);
    }

    @Override
    public List<Branch> getActiveByBusiness(Long businessId) {
        return branchRepository.findByBusinessIdAndActiveTrue(businessId);
    }

    @Override
    public Branch getById(Long businessId, Long id) {
        return branchRepository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch", "ID", id));
    }

    @Override
    public Employee getResponsibleEmployee(Long businessId, Long employeeId) {
        if (employeeId == null) {
            throw new InvalidEntityDataException("El responsable debe ser un empleado registrado en el negocio");
        }
        return employeeRepository.findById(employeeId)
                .filter(employee -> Objects.equals(employee.getBusiness().getId(), businessId))
                .orElseThrow(() -> new InvalidEntityDataException(
                        "El responsable debe ser un empleado registrado en el negocio"));
    }

    @Override
    @Transactional
    public Branch save(Branch branch) {
        if (branch.getId() != null) {
            throw new InvalidEntityDataException("El registro ya tiene un identificador");
        }
        Long businessId = branch.getBusiness().getId();
        if (businessId == null) {
            throw new InvalidEntityDataException("El negocio debe estar registrado");
        }
        businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business", "ID", businessId));
        validateBranch(branch, null);
        return branchRepository.save(branch);
    }

    @Override
    @Transactional
    public Branch update(Long businessId, Long id, Branch branch) {
        Branch current = getById(businessId, id);
        if (!Objects.equals(businessId, branch.getBusiness().getId())) {
            throw new InvalidEntityDataException("No se puede cambiar el negocio de una sucursal");
        }
        validateBranch(branch, id);
        current.changeNameBranch(branch.getName());
        current.changeAddress(branch.getAddress());
        current.changeUbigeoCode(branch.getUbigeoCode());
        current.changeDepartment(branch.getDepartment());
        current.changeProvince(branch.getProvince());
        current.changeDistrict(branch.getDistrict());
        current.changePhone(branch.getPhone());
        current.changeTimezone(branch.getTimezone());
        current.changeMenuSlug(branch.getMenuSlug());
        current.changeResponsibleEmployee(branch.getResponsibleEmployee());

        // Habilitar primero los canales nuevos antes de deshabilitar los anteriores.
        if (branch.isDineInEnabled()) current.enableDineIn();
        if (branch.isTakeoutEnabled()) current.enableTakeout();
        if (branch.isDeliveryEnabled()) current.enableDelivery();
        if (!branch.isDineInEnabled()) current.disableDineIn();
        if (!branch.isTakeoutEnabled()) current.disableTakeout();
        if (!branch.isDeliveryEnabled()) current.disableDelivery();

        if (branch.isReservationsEnabled()) current.enableReservations();
        else current.disableReservations();
        if (branch.isPublicMenuEnabled()) current.enablePublicMenu();
        else current.disablePublicMenu();
        if (branch.isActive()) current.activate();
        else current.deactivate();

        return branchRepository.save(current);
    }

    @Override
    @Transactional
    public void deleteById(Long businessId, Long id) {
        branchRepository.delete(getById(businessId, id));
    }

    private void validateBranch(Branch branch, Long id) {
        Long businessId = branch.getBusiness().getId();
        if (branchRepository.findByBusinessIdAndName(businessId, branch.getName())
                .filter(value -> !Objects.equals(value.getId(), id)).isPresent()) {
            throw new InvalidEntityDataException("Ya existe una sucursal con ese nombre en el negocio");
        }
        if (branch.getMenuSlug() != null && branchRepository.findByMenuSlug(branch.getMenuSlug())
                .filter(value -> !Objects.equals(value.getId(), id)).isPresent()) {
            throw new InvalidEntityDataException("El slug del menú ya está en uso");
        }
        if (branch.getResponsibleEmployee() != null) {
            getResponsibleEmployee(businessId, branch.getResponsibleEmployee().getId());
        }
    }
}
