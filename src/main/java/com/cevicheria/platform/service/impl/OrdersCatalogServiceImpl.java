package com.cevicheria.platform.service.impl;

import com.cevicheria.platform.dto.*;
import com.cevicheria.platform.model.*;
import com.cevicheria.platform.model.enums.DiningTableStatus;
import com.cevicheria.platform.model.enums.KdsSortOrder;
import com.cevicheria.platform.model.id.PreparationAreaKdsSettingId;
import com.cevicheria.platform.repository.*;
import com.cevicheria.platform.service.OrdersCatalogService;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrdersCatalogServiceImpl implements OrdersCatalogService {
    private final OrdersSupport guard;
    private final EntityManager em;
    private final PreparationAreaRepository areas;
    private final DiningTableRepository tables;
    private final PreparationAreaKdsSettingRepository settings;
    private final CustomerOrderRepository orders;

    @Override
    public List<OrdersAreaView> areas(Long businessId, Long branchId) {
        guard.read(businessId, branchId);
        return areas.findByBusinessIdAndBranchIdOrderByIdAsc(businessId, branchId)
                .stream().map(this::areaView).toList();
    }

    @Override
    public OrdersAreaView area(Long businessId, Long branchId, Long id) {
        guard.read(businessId, branchId);
        return areaView(localArea(businessId, branchId, id));
    }

    @Override @Transactional
    public OrdersAreaView createArea(OrdersAreaRequest r) {
        guard.check(r != null, "Solicitud obligatoria");
        guard.write(r.businessId(), r.branchId(), "CONFIGURAR");
        PreparationArea area = new PreparationArea(guard.business(r.businessId()),
                guard.branch(r.businessId(), r.branchId()), r.name());
        area.changePrinterDestination(r.printerDestination());
        long count = areas.findByBusinessId(r.businessId()).stream()
                .filter(a -> a.getName().equalsIgnoreCase(area.getName())).count();
        guard.conflict(count > 0, "Ya existe un área con este nombre");
        return areaView(areas.saveAndFlush(area));
    }

    @Override @Transactional
    public OrdersAreaView updateArea(Long id, OrdersAreaRequest r) {
        guard.check(r != null, "Solicitud obligatoria");
        guard.write(r.businessId(), r.branchId(), "CONFIGURAR");
        PreparationArea area = localArea(r.businessId(), r.branchId(), id);
        guard.conflict(areas.findByBusinessId(r.businessId()).stream()
                .anyMatch(a -> !a.getId().equals(id) && a.getName().equalsIgnoreCase(r.name())),
                "Ya existe un área con este nombre");
        area.changeName(r.name());
        area.changePrinterDestination(r.printerDestination());
        area.activate();
        return areaView(area);
    }

    @Override @Transactional
    public void deactivateArea(Long businessId, Long branchId, Long id) {
        guard.write(businessId, branchId, "CONFIGURAR");
        localArea(businessId, branchId, id).deactivate();
    }

    @Override
    public OrdersKdsView kds(Long businessId, Long branchId, Long areaId) {
        guard.read(businessId, branchId);
        localArea(businessId, branchId, areaId);
        PreparationAreaKdsSetting setting = settings.findById(new PreparationAreaKdsSettingId(businessId, branchId, areaId))
                .orElse(null);
        if (setting == null) return new OrdersKdsView(areaId, KdsSortOrder.CONFIRMACION, 15);
        return kdsView(setting);
    }

    @Override @Transactional
    public OrdersKdsView configureKds(Long businessId, Long branchId, Long areaId, OrdersKdsRequest r) {
        guard.check(r != null, "Solicitud obligatoria");
        guard.write(businessId, branchId, "CONFIGURAR");
        PreparationArea area = localArea(businessId, branchId, areaId);
        PreparationAreaKdsSettingId id = new PreparationAreaKdsSettingId(businessId, branchId, areaId);
        PreparationAreaKdsSetting setting = settings.findById(id).orElse(null);
        if (setting == null) {
            setting = new PreparationAreaKdsSetting(guard.business(businessId),
                    guard.branch(businessId, branchId), area, r.sortOrder(), r.alertMinutes());
            em.persist(setting);
        } else {
            setting.changeSortOrder(r.sortOrder());
            setting.changeAlertMinutes(r.alertMinutes());
        }
        return kdsView(setting);
    }

    @Override
    public List<OrdersTableView> tables(Long businessId, Long branchId) {
        guard.read(businessId, branchId);
        return tables.findByBusinessIdAndBranchIdOrderByIdAsc(businessId, branchId)
                .stream().map(this::tableView).toList();
    }

    @Override
    public OrdersTableView table(Long businessId, Long branchId, Long id) {
        guard.read(businessId, branchId);
        return tableView(localTable(businessId, branchId, id));
    }

    @Override @Transactional
    public OrdersTableView createTable(OrdersTableRequest r) {
        guard.check(r != null, "Solicitud obligatoria");
        guard.write(r.businessId(), r.branchId(), "MESAS");
        guard.conflict(tables.existsByBusinessIdAndBranchIdAndNumber(r.businessId(), r.branchId(), r.number()),
                "Ya existe una mesa con ese número");
        DiningTable table = new DiningTable(guard.business(r.businessId()),
                guard.branch(r.businessId(), r.branchId()), r.number(), r.capacity());
        table.changeZone(r.zone());
        if (r.status() != null) {
            guard.check(r.status() == DiningTableStatus.DISPONIBLE || r.status() == DiningTableStatus.INACTIVA,
                    "Una mesa nueva debe estar disponible o inactiva");
            table.changeStatus(r.status());
        }
        return tableView(tables.saveAndFlush(table));
    }

    @Override @Transactional
    public OrdersTableView updateTable(Long id, OrdersTableRequest r) {
        guard.check(r != null, "Solicitud obligatoria");
        guard.write(r.businessId(), r.branchId(), "MESAS");
        DiningTable table = localTable(r.businessId(), r.branchId(), id);
        guard.conflict(tables.findByBusinessIdAndBranchIdOrderByIdAsc(r.businessId(), r.branchId()).stream()
                .anyMatch(t -> !t.getId().equals(id) && t.getNumber().equalsIgnoreCase(r.number())),
                "Ya existe una mesa con ese número");
        table.changeNumber(r.number());
        table.changeZone(r.zone());
        table.changeCapacity(r.capacity());
        if (r.status() != null && r.status() != table.getStatus()) {
            guard.check(r.status() == DiningTableStatus.DISPONIBLE || r.status() == DiningTableStatus.INACTIVA,
                    "El estado OCUPADA o RESERVADA se administra desde el flujo del pedido");
            guard.conflict(hasOpenOrders(r.businessId(), r.branchId(), id), "Hay pedidos abiertos asociados a la mesa");
            table.changeStatus(r.status());
        }
        return tableView(table);
    }

    @Override @Transactional
    public void deactivateTable(Long businessId, Long branchId, Long id) {
        guard.write(businessId, branchId, "MESAS");
        guard.conflict(hasOpenOrders(businessId, branchId, id), "Hay pedidos abiertos asociados a la mesa");
        localTable(businessId, branchId, id).changeStatus(DiningTableStatus.INACTIVA);
    }

    private boolean hasOpenOrders(Long businessId, Long branchId, Long tableId) {
        return orders.findByBusinessIdAndBranchIdOrderByIdDesc(businessId, branchId).stream()
                .anyMatch(o -> o.getDiningTable() != null && Objects.equals(o.getDiningTable().getId(), tableId)
                        && o.getStatus() != com.cevicheria.platform.model.enums.OrderStatus.CERRADO
                        && o.getStatus() != com.cevicheria.platform.model.enums.OrderStatus.ANULADO
                        && o.getStatus() != com.cevicheria.platform.model.enums.OrderStatus.RECHAZADO
                        && o.getStatus() != com.cevicheria.platform.model.enums.OrderStatus.FUSIONADO);
    }

    private PreparationArea localArea(Long b, Long br, Long id) {
        return guard.local(PreparationArea.class, id, b, br, PreparationArea::getBusiness, PreparationArea::getBranch);
    }

    private DiningTable localTable(Long b, Long br, Long id) {
        return guard.local(DiningTable.class, id, b, br, DiningTable::getBusiness, DiningTable::getBranch);
    }

    private OrdersAreaView areaView(PreparationArea a) {
        return new OrdersAreaView(a.getId(), a.getBusiness().getId(), a.getBranch().getId(), a.getName(), a.isActive(), a.getPrinterDestination());
    }

    private OrdersTableView tableView(DiningTable t) {
        return new OrdersTableView(t.getId(), t.getBusiness().getId(), t.getBranch().getId(), t.getNumber(), t.getZone(), t.getCapacity(), t.getStatus());
    }

    private OrdersKdsView kdsView(PreparationAreaKdsSetting s) {
        return new OrdersKdsView(s.getPreparationArea().getId(), s.getSortOrder(), s.getAlertMinutes());
    }
}
