package com.jamescr1.backend.repository;

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
class InvestmentRepositoryIntegrationTest {

    @Autowired
    private InvestmentRepository investmentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void shouldSaveAndRetrieveInvestment() {
        // creating a test investment
        Investment investment = new Investment();

        investment.setTicker("TESTLB");
        investment.setName("LookBack Integration Test ETF");
        investment.setFundProvider("Test Provider");
        investment.setExchange("TEST");
        investment.setAlphaVantageSymbol("TESTLB.TEST");
        investment.setTradingCurrency("GBP");
        investment.setInceptionDate(LocalDate.of(2020, 1, 1));
        investment.setOngoingChargePercent(new BigDecimal("0.0700"));
        investment.setRegion("Global");

        // saving the investment to the database
        Investment saved = investmentRepository.saveAndFlush(investment);

        // Verifying the database generated an ID
        assertNotNull(saved.getId());

        Long investmentId = saved.getId();

        // Clearing the persistence context to force a database read
        entityManager.clear();

        // Retrieving the investment by its generated ID
        Investment retrieved = investmentRepository.findById(investmentId)
                .orElseThrow();

        // Assert: verify the stored values
        assertAll(
                () -> assertEquals(investmentId, retrieved.getId()),
                () -> assertEquals("TESTLB", retrieved.getTicker()),
                () -> assertEquals(
                        "LookBack Integration Test ETF",
                        retrieved.getName()
                ),
                () -> assertEquals(
                        "Test Provider",
                        retrieved.getFundProvider()
                ),
                () -> assertEquals(
                        "TESTLB.TEST",
                        retrieved.getAlphaVantageSymbol()
                ),
                () -> assertEquals("GBP", retrieved.getTradingCurrency()),
                () -> assertEquals(
                        LocalDate.of(2020, 1, 1),
                        retrieved.getInceptionDate()
                ),
                () -> assertEquals(
                        0,
                        new BigDecimal("0.0700").compareTo(
                                retrieved.getOngoingChargePercent()
                        )
                ),
                () -> assertEquals("Global", retrieved.getRegion())
        );
    }
}
