package com.cevicheria.platform.service;

import com.cevicheria.platform.dto.*;
import java.util.List;
public interface OrdersKitchenService {

 List<OrdersItemView> queue(Long businessId, Long branchId, Long areaId);
 OrdersItemView changeStatus(Long businessId, Long branchId, Long itemId, OrdersKitchenRequest request);
 
}
