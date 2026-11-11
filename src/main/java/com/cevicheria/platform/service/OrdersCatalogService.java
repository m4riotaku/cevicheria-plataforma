package com.cevicheria.platform.service;

import com.cevicheria.platform.dto.*;
import java.util.List;
public interface OrdersCatalogService {

 List<OrdersAreaView> areas(Long businessId, Long branchId);
 OrdersAreaView area(Long businessId, Long branchId, Long id);
 OrdersAreaView createArea(OrdersAreaRequest request);
 OrdersAreaView updateArea(Long id, OrdersAreaRequest request);
 void deactivateArea(Long businessId, Long branchId, Long id);
 OrdersKdsView kds(Long businessId, Long branchId, Long areaId);
 OrdersKdsView configureKds(Long businessId, Long branchId, Long areaId, OrdersKdsRequest request);
 List<OrdersTableView> tables(Long businessId, Long branchId);
 OrdersTableView table(Long businessId, Long branchId, Long id);
 OrdersTableView createTable(OrdersTableRequest request);
 OrdersTableView updateTable(Long id, OrdersTableRequest request);
 void deactivateTable(Long businessId, Long branchId, Long id);
 
}
