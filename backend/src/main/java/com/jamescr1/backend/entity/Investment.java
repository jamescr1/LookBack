package com.jamescr1.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "investment",
        uniqueConstraints = {
        @UniqueConstraint(
                name = "uq_investment_exchange_ticker",
                columnNames = {"exchange", "ticker"}
        )
    }
)
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String ticker;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 100)
    private String fundProvider;

    @Column(nullable = false, length = 20)
    private String exchange;

    @Column(nullable = false, unique = true, length = 50)
    private String alphaVantageSymbol;

    @Column(nullable = false, length = 3, columnDefinition = "CHAR(3)")
    private String tradingCurrency;

    private LocalDate inceptionDate;

    @Column(precision = 7, scale = 4)
    private BigDecimal ongoingChargePercent;

    @Column(length = 50)
    private String region;
}
