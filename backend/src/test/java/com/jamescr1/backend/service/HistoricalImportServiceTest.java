package com.jamescr1.backend.service;

import com.jamescr1.backend.client.AlphaVantageClient;
import com.jamescr1.backend.client.dto.AlphaVantageMonthlyResponse;
import com.jamescr1.backend.client.dto.MonthlyAdjustedDataPoint;
import com.jamescr1.backend.entity.HistoricalPrice;
import com.jamescr1.backend.entity.Investment;
import com.jamescr1.backend.repository.HistoricalPriceRepository;
import com.jamescr1.backend.repository.InvestmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoricalPriceImportServiceTest {

    @Mock
    private HistoricalPriceRepository historicalPriceRepository;

    @Mock
    private InvestmentRepository investmentRepository;

    @Mock
    private AlphaVantageClient alphaVantageClient;

    @InjectMocks
    private HistoricalPriceImportService historicalPriceImportService;

    @Captor
    private ArgumentCaptor<Iterable<HistoricalPrice>> savedPricesCaptor;

    @Test
    void shouldUpdateExistingMonthAndInsertNewMonth() {
        // Arrange: an investment that already exists
        Investment investment = new Investment();
        investment.setAlphaVantageSymbol("VUSA.LON");

        when(investmentRepository.findById(1L))
                .thenReturn(Optional.of(investment));

        // Arrange: September already exists in the database
        HistoricalPrice existingSeptember = new HistoricalPrice();
        existingSeptember.setInvestment(investment);
        existingSeptember.setPriceDate(LocalDate.of(2026, 9, 30));
        existingSeptember.setAdjustedClose(new BigDecimal("100.0000"));

        when(historicalPriceRepository.findAllByInvestment(investment))
                .thenReturn(List.of(existingSeptember));

        // Arrange: Alpha Vantage returns updated September data and new October data
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

        AlphaVantageMonthlyResponse response =
                new AlphaVantageMonthlyResponse(null, apiMonthlyData);

        when(alphaVantageClient.getMonthlyData("VUSA.LON"))
                .thenReturn(response);

        // Act: run the import
        historicalPriceImportService.importHistoricalPriceData(1L);

        // Assert: verify saveAll was called and capture its records
        verify(historicalPriceRepository).saveAll(savedPricesCaptor.capture());

        List<HistoricalPrice> savedPrices =
                StreamSupport.stream(
                        savedPricesCaptor.getValue().spliterator(),
                        false
                ).toList();

        assertEquals(2, savedPrices.size());

        // September should be the existing entity, updated with the API values
        HistoricalPrice september = savedPrices.stream()
                .filter(price -> YearMonth.from(price.getPriceDate())
                        .equals(YearMonth.of(2026, 9)))
                .findFirst()
                .orElseThrow();

        assertSame(existingSeptember, september);
        assertEquals(
                0,
                new BigDecimal("107.5000")
                        .compareTo(september.getAdjustedClose())
        );

        // October should be a newly created entity
        HistoricalPrice october = savedPrices.stream()
                .filter(price -> YearMonth.from(price.getPriceDate())
                        .equals(YearMonth.of(2026, 10)))
                .findFirst()
                .orElseThrow();

        assertNotSame(existingSeptember, october);
        assertEquals(
                LocalDate.of(2026, 10, 14),
                october.getPriceDate()
        );
        assertEquals(
                0,
                new BigDecimal("110.5000")
                        .compareTo(october.getAdjustedClose())
        );
        assertSame(investment, october.getInvestment());

        verify(alphaVantageClient).getMonthlyData("VUSA.LON");
    }
}