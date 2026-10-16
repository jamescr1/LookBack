package com.jamescr1.backend.repository;

import com.jamescr1.backend.entity.HistoricalPrice;
import com.jamescr1.backend.entity.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricalPriceRepository extends JpaRepository<HistoricalPrice, Long> {
    List<HistoricalPrice> findAllByInvestment(Investment investment);
}
