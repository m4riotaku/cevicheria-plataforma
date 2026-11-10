package com.cevicheria.platform.service;

import com.cevicheria.platform.dto.MovementRequest;
import com.cevicheria.platform.dto.MovementView;
import com.cevicheria.platform.dto.ShiftCloseRequest;
import com.cevicheria.platform.dto.ShiftRequest;
import com.cevicheria.platform.dto.ShiftView;
import java.util.List;

public interface BillingCashService {
    List<ShiftView> shifts(Long businessId, Long branchId);
    ShiftView shift(Long businessId, Long branchId, Long id);
    ShiftView open(ShiftRequest request);
    ShiftView close(Long businessId, Long branchId, Long id, ShiftCloseRequest request);
    List<MovementView> movements(Long businessId, Long branchId);
    MovementView movement(Long businessId, Long branchId, Long id);
    MovementView recordMovement(MovementRequest request);
}
