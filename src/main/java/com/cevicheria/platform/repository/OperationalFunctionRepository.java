package com.cevicheria.platform.repository;

import com.cevicheria.platform.model.OperationalFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationalFunctionRepository extends JpaRepository<OperationalFunction, Long>
{
    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);
}
