package com.jamescr1.backend.repository;

import com.jamescr1.backend.entity.HistoricalPrice;
import com.jamescr1.backend.entity.Investment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class HistoricalPriceRepositoryIntegrationTest {

    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    private HistoricalPriceRepository historicalPriceRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void shouldSaveAndRetrieveHistoricalPrice() {
        // Arrange: create and save an investment first
        Investment investment = new Investment();

        investment.setTicker("TESTHP");
        investment.setName("Historical Price Test ETF");
        investment.setFundProvider("Test Provider");
        investment.setExchange("TEST");
        investment.setAlphaVantageSymbol("TESTHP.TEST");
        investment.setTradingCurrency("GBP");
        investment.setRegion("Global");

        Investment savedInvestment =
                investmentRepository.saveAndFlush(investment);

        // Arrange: create a historical price associated with that investment
        HistoricalPrice historicalPrice = new HistoricalPrice();

        historicalPrice.setInvestment(savedInvestment);
        historicalPrice.setPriceDate(LocalDate.of(2025, 12, 31));
        historicalPrice.setOpenPrice(new BigDecimal("100.000000"));
        historicalPrice.setHighPrice(new BigDecimal("105.000000"));
        historicalPrice.setLowPrice(new BigDecimal("98.000000"));
        historicalPrice.setClosePrice(new BigDecimal("103.000000"));
        historicalPrice.setAdjustedClose(new BigDecimal("102.500000"));
        historicalPrice.setVolume(1_000_000L);
        historicalPrice.setDividendAmount(new BigDecimal("0.250000"));

        // Act: save the historical price
        HistoricalPrice savedPrice =
                historicalPriceRepository.saveAndFlush(historicalPrice);

        assertNotNull(savedPrice.getId());

        Long priceId = savedPrice.getId();
        Long investmentId = savedInvestment.getId();

        // Force the next lookup to read from the database
        entityManager.clear();

        // Retrieve the saved price
        HistoricalPrice retrieved =
                historicalPriceRepository.findById(priceId)
                        .orElseThrow();

        // Assert: verify the saved data and relationship
        assertAll(
                () -> assertEquals(priceId, retrieved.getId()),
                () -> assertEquals(
                        LocalDate.of(2025, 12, 31),
                        retrieved.getPriceDate()
                ),
                () -> assertEquals(
                        0,
                        new BigDecimal("100.000000")
                                .compareTo(retrieved.getOpenPrice())
                ),
                () -> assertEquals(
                        0,
                        new BigDecimal("102.500000")
                                .compareTo(retrieved.getAdjustedClose())
                ),
                () -> assertEquals(1_000_000L, retrieved.getVolume()),
                () -> assertEquals(
                        0,
                        new BigDecimal("0.250000")
                                .compareTo(retrieved.getDividendAmount())
                ),
                () -> assertNotNull(retrieved.getInvestment()),
                () -> assertEquals(
                        investmentId,
                        retrieved.getInvestment().getId()
                ),
                () -> assertEquals(
                        "TESTHP",
                        retrieved.getInvestment().getTicker()
                )
        );
    }
}