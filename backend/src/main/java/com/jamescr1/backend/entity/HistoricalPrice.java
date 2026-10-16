package com.jamescr1.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "historical_price",
        uniqueConstraints = {
        @UniqueConstraint(
                name = "uq_historical_price_investment_date",
                columnNames = {"investment_id", "price_date"}
        )
    }
)
public class HistoricalPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "investment_id", nullable = false)
    private Investment investment;

    @Column(nullable = false)
    private LocalDate priceDate;

    @Column(nullable = false, precision = 18, scale = 6)
    private BigDecimal openPrice;

    @Column(nullable = false, precision = 18, scale = 6)
    private BigDecimal highPrice;

    @Column(nullable = false, precision = 18, scale = 6)
    private BigDecimal lowPrice;

    @Column(nullable = false, precision = 18, scale = 6)
    private BigDecimal closePrice;

    @Column(nullable = false, precision = 18, scale = 6)
    private BigDecimal adjustedClose;

    @Column(nullable = false)
    private Long volume;

    @Column(nullable = false, precision = 18, scale = 6)
    private BigDecimal dividendAmount;
}
