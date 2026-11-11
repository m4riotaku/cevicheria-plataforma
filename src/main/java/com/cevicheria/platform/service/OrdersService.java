package com.cevicheria.platform.service;

import com.cevicheria.platform.dto.*;
import java.util.List;
public interface OrdersService {

 List<OrdersView> orders(Long businessId, Long branchId);
 OrdersView order(Long businessId, Long branchId, Long id);
 OrdersView createOrder(OrdersCreateRequest request);
 OrdersView updateOrder(Long businessId, Long branchId, Long id, OrdersUpdateRequest request);
 OrdersView confirm(Long businessId, Long branchId, Long id);
 OrdersView close(Long businessId, Long branchId, Long id);
 void cancel(Long businessId, Long branchId, Long id, OrdersCancelRequest request);
 List<OrdersItemView> items(Long businessId, Long branchId, Long orderId);
 OrdersItemView item(Long businessId, Long branchId, Long id);
 OrdersItemView addItem(OrdersItemRequest request);
 OrdersItemView updateItem(Long businessId, Long branchId, Long id, OrdersItemUpdateRequest request);
 void cancelItem(Long businessId, Long branchId, Long id);
 List<OrdersModifierView> modifiers(Long businessId, Long branchId, Long itemId);
 OrdersModifierView addModifier(OrdersModifierRequest request);
 void removeModifier(Long businessId, Long branchId, Long id);
 OrdersRecipeSnapshotView snapshot(Long businessId, Long branchId, Long itemId);
 List<OrdersRecipeIngredientView> recipeIngredients(Long businessId, Long branchId, Long itemId);
 
}
