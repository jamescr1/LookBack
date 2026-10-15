package com.jamescr1.backend.client.dto;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import java.io.InputStream;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AlphaVantageMonthlyResponseTest {

    private final JsonMapper objectMapper = JsonMapper.builder().build();

    @Test
    void shouldDeserializeMonthlyAdjustedResponse() throws Exception {
        try (InputStream input = getClass().getResourceAsStream(
                "/alphavantage-monthly-response.json")) {

            assertNotNull(input, "JSON test fixture should exist");

            AlphaVantageMonthlyResponse response = objectMapper.readValue(
                    input,
                    AlphaVantageMonthlyResponse.class
            );

            // Check the metadata mapping
            assertEquals("VUSA.LON", response.metadata().symbol());
            assertEquals("2026-10-14", response.metadata().lastRefreshed());

            // Check that the monthly data was mapped correctly
            assertEquals(2, response.monthlyData().size());

            MonthlyAdjustedDataPoint latest =
                    response.monthlyData().get("2026-10-14");

            assertNotNull(latest);
            assertEquals(new BigDecimal("466.1000"), latest.open());
            assertEquals(new BigDecimal("526.4000"), latest.adjustedClose());
            assertEquals(161039558L, latest.volume());
            assertEquals(new BigDecimal("0.0000"), latest.dividendAmount());

            // Check the second monthly observation too
            MonthlyAdjustedDataPoint previous =
                    response.monthlyData().get("2026-09-30");

            assertNotNull(previous);
            assertEquals(new BigDecimal("468.8000"), previous.close());
        }
    }
}