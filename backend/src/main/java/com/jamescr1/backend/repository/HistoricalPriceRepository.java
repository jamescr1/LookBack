package com.jamescr1.backend.repository;

import com.jamescr1.backend.entity.HistoricalPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricalPriceRepository extends JpaRepository<HistoricalPrice, Long> {
}
