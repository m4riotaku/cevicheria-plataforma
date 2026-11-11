package com.cevicheria.platform.service.impl;

import com.cevicheria.platform.dto.OrdersItemView;
import com.cevicheria.platform.dto.OrdersKitchenRequest;
import com.cevicheria.platform.model.*;
import com.cevicheria.platform.model.enums.*;
import com.cevicheria.platform.repository.OrderItemRepository;
import com.cevicheria.platform.service.OrdersKitchenService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrdersKitchenServiceImpl implements OrdersKitchenService {
    private final OrdersSupport guard;
    private final OrderItemRepository items;

    @Override
    public List<OrdersItemView> queue(Long b, Long br, Long areaId) {
        guard.write(b, br, "COMANDAS");
        guard.local(PreparationArea.class, areaId, b, br, PreparationArea::getBusiness, PreparationArea::getBranch);
        return items.findByBusinessIdAndBranchIdAndPreparationAreaIdAndStatusInOrderByIdAsc(b, br, areaId,
                List.of(OrderItemStatus.CONFIRMADO, OrderItemStatus.PREPARANDO))
                .stream().map(this::view).toList();
    }

    @Override @Transactional
    public OrdersItemView changeStatus(Long b, Long br, Long itemId, OrdersKitchenRequest request) {
        guard.check(request != null && request.status() != null, "El estado es obligatorio");
        guard.check(request.status() == OrderItemStatus.PREPARANDO
                || request.status() == OrderItemStatus.LISTO
                || request.status() == OrderItemStatus.ENTREGADO,
                "Estado de cocina inválido");
        guard.write(b, br, request.status() == OrderItemStatus.ENTREGADO ? "PEDIDOS" : "PREPARACION");
        OrderItem item = guard.local(OrderItem.class, itemId, b, br, OrderItem::getBusiness, OrderItem::getBranch);
        CustomerOrder order = item.getOrder();
        guard.check(order.getStatus() == OrderStatus.CONFIRMADO || order.getStatus() == OrderStatus.PREPARANDO
                || order.getStatus() == OrderStatus.LISTO, "El pedido no está en preparación");
        LocalDateTime now = LocalDateTime.now();
        switch (request.status()) {
            case PREPARANDO -> {
                guard.check(item.getStatus() == OrderItemStatus.CONFIRMADO, "El detalle debe estar confirmado");
                if (request.preparedByEmployeeId() != null) {
                    item.changePreparedByEmployee(guard.employee(b, br, request.preparedByEmployeeId()));
                }
                item.changeStatus(OrderItemStatus.PREPARANDO);
                item.changePreparationStartedAt(now);
            }
            case LISTO -> {
                guard.check(item.getStatus() == OrderItemStatus.PREPARANDO, "El detalle debe estar en preparación");
                item.changeStatus(OrderItemStatus.LISTO);
                item.changeReadyAt(now);
            }
            case ENTREGADO -> {
                guard.check(item.getStatus() == OrderItemStatus.LISTO, "El detalle debe estar listo");
                item.changeStatus(OrderItemStatus.ENTREGADO);
                item.changeDeliveredAt(now);
            }
            default -> throw new IllegalStateException("Estado de cocina no admitido");
        }
        List<OrderItem> all = items.findByBusinessIdAndBranchIdAndOrderIdOrderByIdAsc(b, br, order.getId()).stream()
                .filter(i -> i.getStatus() != OrderItemStatus.ANULADO).toList();
        if (all.stream().allMatch(i -> i.getStatus() == OrderItemStatus.ENTREGADO)) {
            order.changeStatus(OrderStatus.ENTREGADO);
        } else if (all.stream().allMatch(i -> i.getStatus() == OrderItemStatus.LISTO
                || i.getStatus() == OrderItemStatus.ENTREGADO)) {
            order.changeStatus(OrderStatus.LISTO);
        } else if (all.stream().anyMatch(i -> i.getStatus() == OrderItemStatus.PREPARANDO
                || i.getStatus() == OrderItemStatus.LISTO || i.getStatus() == OrderItemStatus.ENTREGADO)) {
            order.changeStatus(OrderStatus.PREPARANDO);
        }
        return view(item);
    }

    private OrdersItemView view(OrderItem i) {
        return new OrdersItemView(i.getId(), i.getOrder().getId(), i.getProduct().getId(),
                i.getPreparationArea() == null ? null : i.getPreparationArea().getId(),
                i.getDescription(), i.getQuantity(), i.getUnitPrice(), i.getSubtotal(), i.getTax(),
                i.getTotal(), i.getStatus());
    }
}
