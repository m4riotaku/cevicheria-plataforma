package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cevicheria.platform.model.ReceiptSeries;

@Repository
public interface ReceiptSeriesRepository extends JpaRepository<ReceiptSeries,Long>
{
}
