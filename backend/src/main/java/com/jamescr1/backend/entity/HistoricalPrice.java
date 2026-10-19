package com.jamescr1.backend.entity;

import com.jamescr1.backend.client.dto.MonthlyAdjustedDataPoint;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
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

    public void updateFrom(String dateString, MonthlyAdjustedDataPoint apiPriceData) {
        //converting to localdate type so it can be inserted into db record
        LocalDate dateToInsert = LocalDate.parse(dateString);
        //updating date
        this.setPriceDate(dateToInsert);
        //updating price data
        updatePriceData(apiPriceData);
    }

    private void updatePriceData(MonthlyAdjustedDataPoint apiPriceData) {
        this.setOpenPrice(apiPriceData.open());
        this.setHighPrice(apiPriceData.high());
        this.setLowPrice(apiPriceData.low());
        this.setClosePrice(apiPriceData.close());
        this.setAdjustedClose(apiPriceData.adjustedClose());
        this.setVolume(apiPriceData.volume());
        this.setDividendAmount(apiPriceData.dividendAmount());
    }

    public static HistoricalPrice fromApiData(Investment investment, String dateString, MonthlyAdjustedDataPoint apiPriceData) {
        HistoricalPrice price = new HistoricalPrice();
        price.setInvestment(investment);
        price.updateFrom(dateString, apiPriceData);
        return price;
    }
}
