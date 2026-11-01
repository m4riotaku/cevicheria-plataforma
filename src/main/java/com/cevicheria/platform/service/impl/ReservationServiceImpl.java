package com.cevicheria.platform.service.impl;

import com.cevicheria.platform.model.enums.ReservationStatus;

import java.util.List;
import java.util.Objects;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cevicheria.platform.dto.ReservationRequest;
import com.cevicheria.platform.dto.ReservationResponse;
import com.cevicheria.platform.handlerException.BadRequestException;
import com.cevicheria.platform.handlerException.ResourceConflictException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.Branch;
import com.cevicheria.platform.model.Business;
import com.cevicheria.platform.model.DiningTable;
import com.cevicheria.platform.model.Reservation;
import com.cevicheria.platform.repository.ReservationRepository;
import com.cevicheria.platform.service.ReservationService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository repository;
    private final EntityManager entityManager;

    @Override
    public List<ReservationResponse> getByStatus(Long businessId, Long branchId, ReservationStatus status) {
        validateId(businessId);
        validateId(branchId);
        if (status == null) {
            throw new BadRequestException("El estado es obligatorio");
        }
        return repository.findByBusinessIdAndBranchIdAndStatus(businessId, branchId, status)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public List<ReservationResponse> getAll(Long businessId, Long branchId) {
        validateId(businessId);
        validateId(branchId);
        return repository.findByBusinessIdAndBranchId(businessId, branchId).stream().map(this::toResponse).toList();
    }

    @Override
    public ReservationResponse getById(Long businessId, Long id) {
        return toResponse(find(businessId, id));
    }

    @Override
    @Transactional
    public ReservationResponse create(ReservationRequest request) {
        validateRequest(request);
        Business business = require(Business.class, request.businessId());
        Branch branch = require(Branch.class, request.branchId());
        Reservation current = new Reservation(business, branch, request.contactName(), request.phone(), request.startsAt(), request.endsAt(), request.guestCount());
        applyChanges(current, request);
        validateRelations(current);
        return toResponse(repository.saveAndFlush(current));
    }

    @Override
    @Transactional
    public ReservationResponse update(Long businessId, Long id, ReservationRequest request) {
        validateRequest(request);
        if (request.businessId() == null
                && request.branchId() == null
                && request.diningTableId() == null
                && request.contactName() == null
                && request.phone() == null
                && request.startsAt() == null
                && request.endsAt() == null
                && request.guestCount() == null
                && request.toleranceMinutes() == null
                && request.requiredDeposit() == null
                && request.status() == null
                && request.notes() == null) {
            throw new BadRequestException("Debes enviar al menos un dato para actualizar");
        }
        Reservation current = find(businessId, id);
        if (request.businessId() != null && !Objects.equals(request.businessId(), current.getBusiness().getId())) {
            throw new BadRequestException("El campo businessId no se puede modificar");
        }
        if (request.branchId() != null && !Objects.equals(request.branchId(), current.getBranch().getId())) {
            throw new BadRequestException("El campo branchId no se puede modificar");
        }
        applyChanges(current, request);
        validateRelations(current);
        return toResponse(repository.saveAndFlush(current));
    }

    @Override
    @Transactional
    public void delete(Long businessId, Long id) {
        repository.delete(find(businessId, id));
        repository.flush();
    }

    private Reservation find(Long businessId, Long id) {
        validateId(businessId);
        validateId(id);
        return repository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "ID", id));
    }

    private void applyChanges(Reservation current, ReservationRequest request) {
        if (request.diningTableId() != null) {
            current.changeDiningTable(require(DiningTable.class, request.diningTableId()));
        }
        if (request.contactName() != null) {
            current.changeContactName(request.contactName());
        }
        if (request.phone() != null) {
            current.changePhone(request.phone());
        }
        if (request.startsAt() != null) {
            current.changeStartsAt(request.startsAt());
        }
        if (request.endsAt() != null) {
            current.changeEndsAt(request.endsAt());
        }
        if (request.guestCount() != null) {
            current.changeGuestCount(request.guestCount());
        }
        if (request.toleranceMinutes() != null) {
            current.changeToleranceMinutes(request.toleranceMinutes());
        }
        if (request.requiredDeposit() != null) {
            current.changeRequiredDeposit(request.requiredDeposit());
        }
        if (request.status() != null) {
            current.changeStatus(request.status());
        }
        if (request.notes() != null) {
            current.changeNotes(request.notes());
        }
    }

    private void validateRelations(Reservation current) {
        if (current.getBranch() != null && (!Objects.equals(current.getBusiness().getId(), current.getBranch().getBusiness().getId()))) {
            throw new BadRequestException("La relación branch no pertenece al mismo ámbito");
        }
        if (current.getDiningTable() != null
                && (!Objects.equals(current.getBusiness().getId(), current.getDiningTable().getBusiness().getId())
                || !Objects.equals(current.getBranch().getId(), current.getDiningTable().getBranch().getId()))) {
            throw new BadRequestException("La relación diningTable no pertenece al mismo ámbito");
        }
        if (!current.getEndsAt().isAfter(current.getStartsAt())) {
            throw new BadRequestException("La fecha de fin debe ser posterior al inicio");
        }
    }

    private <T> T require(Class<T> type, Long id) {
        validateId(id);
        T value = entityManager.find(type, id);
        if (value == null) {
            throw new ResourceNotFoundException(type.getSimpleName(), "ID", id);
        }
        return value;
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("El ID debe ser un número mayor que cero");
        }
    }

    private void validateRequest(ReservationRequest request) {
        if (request == null) {
            throw new BadRequestException("Los datos son obligatorios");
        }
    }

    private ReservationResponse toResponse(Reservation current) {
        return new ReservationResponse(
                current.getId(),
                current.getBusiness() == null ? null : current.getBusiness().getId(),
                current.getBranch() == null ? null : current.getBranch().getId(),
                current.getCustomer() == null ? null : current.getCustomer().getId(),
                current.getDiningTable() == null ? null : current.getDiningTable().getId(),
                current.getContactName(),
                current.getPhone(),
                current.getStartsAt(),
                current.getEndsAt(),
                current.getGuestCount(),
                current.getToleranceMinutes(),
                current.getRequiredDeposit(),
                current.getStatus(),
                current.getNotes(),
                current.getCreatedAt());
    }
}
