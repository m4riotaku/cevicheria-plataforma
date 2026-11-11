package com.cevicheria.platform.repository;

import com.cevicheria.platform.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findByBusinessIdAndBranchIdOrderByIdDesc(Long businessId, Long branchId);
    boolean existsByBusinessIdAndBranchIdAndNumber(Long businessId, Long branchId, String number);
    boolean existsByBusinessIdAndRequestKey(Long businessId, String requestKey);
}
