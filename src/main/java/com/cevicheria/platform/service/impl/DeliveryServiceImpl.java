package com.cevicheria.platform.service.impl;

import com.cevicheria.platform.model.enums.DeliveryStatus;

import java.util.List;
import java.util.Objects;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.cevicheria.platform.model.OrderItem;
import com.cevicheria.platform.model.enums.OrderStatus;
import com.cevicheria.platform.model.enums.OrderItemStatus;
import com.cevicheria.platform.model.enums.OrderBillStates;
import com.cevicheria.platform.model.enums.SalesChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cevicheria.platform.dto.DeliveryRequest;
import com.cevicheria.platform.dto.DeliveryResponse;
import com.cevicheria.platform.handlerException.BadRequestException;
import com.cevicheria.platform.handlerException.ResourceConflictException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.Branch;
import com.cevicheria.platform.model.Business;
import com.cevicheria.platform.model.CustomerOrder;
import com.cevicheria.platform.model.Delivery;
import com.cevicheria.platform.model.DeliveryZone;
import com.cevicheria.platform.model.Employee;
import com.cevicheria.platform.repository.DeliveryRepository;
import com.cevicheria.platform.service.DeliveryService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository repository;
    private final EntityManager entityManager;

    @Override
    public List<DeliveryResponse> getByStatus(Long businessId, Long branchId, DeliveryStatus status) {
        validateId(businessId);
        validateId(branchId);
        if (status == null) {
            throw new BadRequestException("El estado es obligatorio");
        }
        return repository.findByBusinessIdAndBranchIdAndStatus(businessId, branchId, status)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public List<DeliveryResponse> getAll(Long businessId, Long branchId) {
        validateId(businessId);
        validateId(branchId);
        return repository.findByBusinessIdAndBranchId(businessId, branchId).stream().map(this::toResponse).toList();
    }

    @Override
    public DeliveryResponse getById(Long businessId, Long id) {
        return toResponse(find(businessId, id));
    }

    @Override
    @Transactional
    public DeliveryResponse create(DeliveryRequest request) {
        validateRequest(request);
        Business business = require(Business.class, request.businessId());
        Branch branch = require(Branch.class, request.branchId());
        CustomerOrder order = require(CustomerOrder.class, request.orderId());
        entityManager.lock(order, LockModeType.PESSIMISTIC_WRITE);
        if (order.getSalesChannel() != SalesChannel.DELIVERY) throw new BadRequestException("El pedido debe ser de canal DELIVERY");
        if (order.getStatus() == OrderStatus.CERRADO || order.getStatus() == OrderStatus.ANULADO || order.getStatus() == OrderStatus.ENTREGADO)
            throw new BadRequestException("El pedido ya está finalizado");
        ensureNotBilled(order);
        if (request.status() != null && request.status() != DeliveryStatus.PENDIENTE && request.status() != DeliveryStatus.ASIGNADO)
            throw new BadRequestException("El envío debe crearse pendiente o asignado");
        DeliveryZone deliveryZone = require(DeliveryZone.class, request.deliveryZoneId());
        if (!deliveryZone.isActive()) throw new BadRequestException("La zona de delivery está inactiva");
        if (order.getSubtotal().subtract(order.getDiscount()).compareTo(deliveryZone.getMinimumOrderAmount()) < 0)
            throw new BadRequestException("El pedido no alcanza el importe mínimo de la zona de delivery");
        Delivery current = new Delivery(business, branch, order, deliveryZone, request.driverName(), request.address(), request.estimatedMinutes());
        current.changeDeliveryFeeSnapshot(request.deliveryFeeSnapshot() == null ? deliveryZone.getFee() : request.deliveryFeeSnapshot());
        applyChanges(current, request);
        validateRelations(current);
        synchronizeFee(current);
        return toResponse(repository.saveAndFlush(current));
    }

    @Override
    @Transactional
    public DeliveryResponse update(Long businessId, Long id, DeliveryRequest request) {
        validateRequest(request);
        if (request.businessId() == null
                && request.branchId() == null
                && request.orderId() == null
                && request.deliveryZoneId() == null
                && request.driverEmployeeId() == null
                && request.driverName() == null
                && request.driverPhone() == null
                && request.address() == null
                && request.deliveryFeeSnapshot() == null
                && request.estimatedMinutes() == null
                && request.cashOnDeliveryAmount() == null
                && request.dispatchedAt() == null
                && request.deliveredAt() == null
                && request.status() == null
                && request.addressReference() == null) {
            throw new BadRequestException("Debes enviar al menos un dato para actualizar");
        }
        Delivery current = find(businessId, id);
        entityManager.lock(current.getOrder(), LockModeType.PESSIMISTIC_WRITE);
        entityManager.lock(current, LockModeType.PESSIMISTIC_WRITE);
        if (current.getStatus() == DeliveryStatus.ENTREGADO) throw new ResourceConflictException("El envío ya fue entregado");
        if (request.deliveryFeeSnapshot() != null && request.deliveryFeeSnapshot().compareTo(current.getDeliveryFeeSnapshot()) != 0)
            ensureNotBilled(current.getOrder());
        validateTransition(current, request.status());
        if (request.businessId() != null && !Objects.equals(request.businessId(), current.getBusiness().getId())) {
            throw new BadRequestException("El campo businessId no se puede modificar");
        }
        if (request.branchId() != null && !Objects.equals(request.branchId(), current.getBranch().getId())) {
            throw new BadRequestException("El campo branchId no se puede modificar");
        }
        if (request.orderId() != null && !Objects.equals(request.orderId(), current.getOrder().getId())) {
            throw new BadRequestException("El campo orderId no se puede modificar");
        }
        if (request.deliveryZoneId() != null && !Objects.equals(request.deliveryZoneId(), current.getDeliveryZone().getId())) {
            throw new BadRequestException("El campo deliveryZoneId no se puede modificar");
        }
        applyChanges(current, request);
        if (current.getStatus() == DeliveryStatus.EN_RUTA && current.getDispatchedAt() == null)
            current.changeDispatchedAt(LocalDateTime.now());
        if (current.getStatus() == DeliveryStatus.ENTREGADO) {
            if (current.getDeliveredAt() == null) current.changeDeliveredAt(LocalDateTime.now());
            List<OrderItem> items = entityManager.createQuery("select i from OrderItem i where i.order.id = :id and i.status <> :cancelled", OrderItem.class)
                    .setParameter("id", current.getOrder().getId()).setParameter("cancelled", OrderItemStatus.ANULADO).getResultList();
            if (items.isEmpty() || items.stream().anyMatch(i -> i.getStatus() != OrderItemStatus.LISTO && i.getStatus() != OrderItemStatus.ENTREGADO))
                throw new BadRequestException("Todos los productos deben estar listos antes de entregar el envío");
            for (OrderItem item : items) {
                item.changeStatus(OrderItemStatus.ENTREGADO);
                item.changeDeliveredAt(current.getDeliveredAt());
            }
            current.getOrder().changeStatus(OrderStatus.ENTREGADO);
        }
        validateRelations(current);
        synchronizeFee(current);
        return toResponse(repository.saveAndFlush(current));
    }

    @Override
    @Transactional
    public void delete(Long businessId, Long id) {
        Delivery current = find(businessId, id);
        entityManager.lock(current.getOrder(), LockModeType.PESSIMISTIC_WRITE);
        if (current.getStatus() == DeliveryStatus.ENTREGADO) throw new ResourceConflictException("El envío ya fue entregado");
        ensureNotBilled(current.getOrder());
        current.changeDeliveryFeeSnapshot(BigDecimal.ZERO);
        synchronizeFee(current);
        repository.delete(current);
        repository.flush();
    }

    private void ensureNotBilled(CustomerOrder order) {
        Long count = entityManager.createQuery("select count(b) from OrderBill b where b.order.id = :id and b.status <> :status", Long.class)
                .setParameter("id", order.getId()).setParameter("status", OrderBillStates.ANULADA).getSingleResult();
        if (count > 0) throw new ResourceConflictException("Configura el envío y su tarifa antes de generar la cuenta");
    }

    private void synchronizeFee(Delivery delivery) {
        CustomerOrder order = delivery.getOrder();
        order.changeAmounts(order.getSubtotal(), order.getDiscount(), order.getTax(), delivery.getDeliveryFeeSnapshot(),
                order.getSubtotal().subtract(order.getDiscount()).add(order.getTax()).add(delivery.getDeliveryFeeSnapshot()));
    }

    private void validateTransition(Delivery current, DeliveryStatus target) {
        if (target == null || target == current.getStatus()) return;
        boolean valid = switch (current.getStatus()) {
            case PENDIENTE -> target == DeliveryStatus.ASIGNADO || target == DeliveryStatus.EN_RUTA;
            case ASIGNADO -> target == DeliveryStatus.EN_RUTA;
            case EN_RUTA -> target == DeliveryStatus.ENTREGADO || target == DeliveryStatus.FALLIDO;
            case FALLIDO -> target == DeliveryStatus.ASIGNADO;
            case ENTREGADO -> false;
        };
        if (!valid) throw new BadRequestException("Transición de delivery no permitida");
        if (target == DeliveryStatus.EN_RUTA && current.getOrder().getStatus() != OrderStatus.LISTO)
            throw new BadRequestException("El pedido debe estar listo antes de despacharse");
    }

    private Delivery find(Long businessId, Long id) {
        validateId(businessId);
        validateId(id);
        return repository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery", "ID", id));
    }

    private void applyChanges(Delivery current, DeliveryRequest request) {
        if (request.driverEmployeeId() != null) {
            current.changeDriverEmployee(require(Employee.class, request.driverEmployeeId()));
        }
        if (request.driverName() != null) {
            current.changeDriverName(request.driverName());
        }
        if (request.driverPhone() != null) {
            current.changeDriverPhone(request.driverPhone());
        }
        if (request.address() != null) {
            current.changeAddress(request.address());
        }
        if (request.deliveryFeeSnapshot() != null) {
            current.changeDeliveryFeeSnapshot(request.deliveryFeeSnapshot());
        }
        if (request.estimatedMinutes() != null) {
            current.changeEstimatedMinutes(request.estimatedMinutes());
        }
        if (request.cashOnDeliveryAmount() != null) {
            current.changeCashOnDeliveryAmount(request.cashOnDeliveryAmount());
        }
        if (request.dispatchedAt() != null) {
            current.changeDispatchedAt(request.dispatchedAt());
        }
        if (request.deliveredAt() != null) {
            current.changeDeliveredAt(request.deliveredAt());
        }
        if (request.status() != null) {
            current.changeStatus(request.status());
        }
        if (request.addressReference() != null) {
            current.changeAddressReference(request.addressReference());
        }
    }

    private void validateRelations(Delivery current) {
        if (current.getBranch() != null && (!Objects.equals(current.getBusiness().getId(), current.getBranch().getBusiness().getId()))) {
            throw new BadRequestException("La relación branch no pertenece al mismo ámbito");
        }
        if (current.getOrder() != null
                && (!Objects.equals(current.getBusiness().getId(), current.getOrder().getBusiness().getId())
                || !Objects.equals(current.getBranch().getId(), current.getOrder().getBranch().getId()))) {
            throw new BadRequestException("La relación order no pertenece al mismo ámbito");
        }
        if (current.getDeliveryZone() != null
                && (!Objects.equals(current.getBusiness().getId(), current.getDeliveryZone().getBusiness().getId())
                || !Objects.equals(current.getBranch().getId(), current.getDeliveryZone().getBranch().getId()))) {
            throw new BadRequestException("La relación deliveryZone no pertenece al mismo ámbito");
        }
        if (current.getDriverEmployee() != null && (!Objects.equals(current.getBusiness().getId(), current.getDriverEmployee().getBusiness().getId()))) {
            throw new BadRequestException("La relación driverEmployee no pertenece al mismo ámbito");
        }
        if (current.getDeliveredAt() != null && (current.getDispatchedAt() == null || current.getDeliveredAt().isBefore(current.getDispatchedAt()))) {
            throw new BadRequestException("La entrega debe ser posterior a la salida");
        }
        repository.findByOrder(current.getOrder())
                .filter(existing -> !Objects.equals(existing.getId(), current.getId()))
                .ifPresent(existing -> {
                    throw new ResourceConflictException("Ya existe un registro con esos datos");
                });
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

    private void validateRequest(DeliveryRequest request) {
        if (request == null) {
            throw new BadRequestException("Los datos son obligatorios");
        }
    }

    private DeliveryResponse toResponse(Delivery current) {
        return new DeliveryResponse(
                current.getId(),
                current.getBusiness() == null ? null : current.getBusiness().getId(),
                current.getBranch() == null ? null : current.getBranch().getId(),
                current.getOrder() == null ? null : current.getOrder().getId(),
                current.getDeliveryZone() == null ? null : current.getDeliveryZone().getId(),
                current.getDriverEmployee() == null ? null : current.getDriverEmployee().getId(),
                current.getDriverName(),
                current.getDriverPhone(),
                current.getAddress(),
                current.getDeliveryFeeSnapshot(),
                current.getEstimatedMinutes(),
                current.getCashOnDeliveryAmount(),
                current.getDispatchedAt(),
                current.getDeliveredAt(),
                current.getStatus(),
                current.getAddressReference(),
                current.getCreatedAt());
    }
}
