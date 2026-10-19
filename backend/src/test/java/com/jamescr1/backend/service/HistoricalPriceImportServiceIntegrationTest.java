package com.jamescr1.backend.service;

import com.jamescr1.backend.client.AlphaVantageClient;
import com.jamescr1.backend.client.dto.AlphaVantageMonthlyResponse;
import com.jamescr1.backend.client.dto.MonthlyAdjustedDataPoint;
import com.jamescr1.backend.entity.HistoricalPrice;
import com.jamescr1.backend.entity.Investment;
import com.jamescr1.backend.repository.HistoricalPriceRepository;
import com.jamescr1.backend.repository.InvestmentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@Import(HistoricalPriceImportService.class)
class HistoricalPriceImportServiceIntegrationTest {

    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    private HistoricalPriceRepository historicalPriceRepository;

    @Autowired
    private HistoricalPriceImportService historicalPriceImportService;

    @MockitoBean
    private AlphaVantageClient alphaVantageClient;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void shouldUpdateExistingPricesAndInsertNewMonth() {
        // Arrange: create an investment in the real database.
        Investment investment = new Investment();
        investment.setTicker("TSTIMP");
        investment.setName("Import Integration Test ETF");
        investment.setFundProvider("Test Provider");
        investment.setExchange("TEST");
        investment.setAlphaVantageSymbol("TSTIMP.TEST");
        investment.setTradingCurrency("GBP");
        investment.setRegion("Global");

        Investment savedInvestment =
                investmentRepository.saveAndFlush(investment);

        // Arrange: September already exists in the database.
        HistoricalPrice september = new HistoricalPrice();
        september.setInvestment(savedInvestment);
        september.setPriceDate(LocalDate.of(2026, 9, 30));
        september.setOpenPrice(new BigDecimal("95.0000"));
        september.setHighPrice(new BigDecimal("105.0000"));
        september.setLowPrice(new BigDecimal("94.0000"));
        september.setClosePrice(new BigDecimal("100.0000"));
        september.setAdjustedClose(new BigDecimal("100.0000"));
        september.setVolume(1_000_000L);
        september.setDividendAmount(new BigDecimal("0.0000"));

        HistoricalPrice savedSeptember =
                historicalPriceRepository.saveAndFlush(september);

        Long septemberId = savedSeptember.getId();
        Long investmentId = savedInvestment.getId();

        // Arrange: mock Alpha Vantage returning updated September
        // data and a new October observation.
        MonthlyAdjustedDataPoint updatedSeptember =
                new MonthlyAdjustedDataPoint(
                        new BigDecimal("101.0000"),
                        new BigDecimal("110.0000"),
                        new BigDecimal("99.0000"),
                        new BigDecimal("108.0000"),
                        new BigDecimal("107.5000"),
                        1_200_000L,
                        new BigDecimal("0.5000")
                );

        MonthlyAdjustedDataPoint newOctober =
                new MonthlyAdjustedDataPoint(
                        new BigDecimal("108.0000"),
                        new BigDecimal("112.0000"),
                        new BigDecimal("106.0000"),
                        new BigDecimal("111.0000"),
                        new BigDecimal("110.5000"),
                        1_500_000L,
                        new BigDecimal("0.0000")
                );

        Map<String, MonthlyAdjustedDataPoint> apiMonthlyData = Map.of(
                "2026-09-30", updatedSeptember,
                "2026-10-14", newOctober
        );

        AlphaVantageMonthlyResponse apiResponse =
                new AlphaVantageMonthlyResponse(null, apiMonthlyData);

        when(alphaVantageClient.getMonthlyData("TSTIMP.TEST"))
                .thenReturn(apiResponse);

        // Act: run the actual import service.
        historicalPriceImportService.importHistoricalPriceData(investmentId);

        // Force pending database changes to be written, then clear
        // Hibernate's cache so subsequent reads come from PostgreSQL.
        historicalPriceRepository.flush();
        entityManager.clear();

        // Assert: reload the investment and its historical prices.
        Investment reloadedInvestment =
                investmentRepository.findById(investmentId).orElseThrow();

        List<HistoricalPrice> prices =
                historicalPriceRepository.findAllByInvestment(
                        reloadedInvestment
                );

        // We expect September and October, with no duplicate September.
        assertEquals(2, prices.size());

        HistoricalPrice reloadedSeptember = prices.stream()
                .filter(price -> YearMonth.from(price.getPriceDate())
                        .equals(YearMonth.of(2026, 9)))
                .findFirst()
                .orElseThrow();

        // Existing record was updated, not replaced with a duplicate.
        assertEquals(septemberId, reloadedSeptember.getId());
        assertEquals(
                0,
                new BigDecimal("107.5000")
                        .compareTo(reloadedSeptember.getAdjustedClose())
        );
        assertEquals(1_200_000L, reloadedSeptember.getVolume());
        assertEquals(
                0,
                new BigDecimal("0.5000")
                        .compareTo(reloadedSeptember.getDividendAmount())
        );

        HistoricalPrice reloadedOctober = prices.stream()
                .filter(price -> YearMonth.from(price.getPriceDate())
                        .equals(YearMonth.of(2026, 10)))
                .findFirst()
                .orElseThrow();

        // A new record was inserted for October.
        assertNotNull(reloadedOctober.getId());
        assertEquals(
                LocalDate.of(2026, 10, 14),
                reloadedOctober.getPriceDate()
        );
        assertEquals(
                0,
                new BigDecimal("110.5000")
                        .compareTo(reloadedOctober.getAdjustedClose())
        );
        assertEquals(1_500_000L, reloadedOctober.getVolume());
        assertEquals(
                investmentId,
                reloadedOctober.getInvestment().getId()
        );
    }
}
