package com.jamescr1.backend.client;

import com.jamescr1.backend.client.dto.AlphaVantageMonthlyResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;

public class AlphaVantageClientTest {

    @Test
    void shouldFetchMonthlyDataFromAlphaVantage() {
        RestClient.Builder builder = RestClient.builder();
        String apiKey = System.getenv("ALPHA_VANTAGE_API_KEY");

        assertNotNull(apiKey, "API key environment variable must be set");
        assertFalse(apiKey.isBlank(), "API key must not be blank");

        AlphaVantageClient alphaVantageClient = new AlphaVantageClient(builder, apiKey);

        AlphaVantageMonthlyResponse response = alphaVantageClient.getMonthlyData("VUSA.LON");

        assertNotNull(response);
        assertNotNull(response.metadata());
        assertEquals("VUSA.LON", response.metadata().symbol());

        assertNotNull(response.monthlyData());
        assertFalse(response.monthlyData().isEmpty());

        assertNotNull(response);
        assertNotNull(response.metadata());
        assertEquals("VUSA.LON", response.metadata().symbol());
        assertNotNull(response.metadata().lastRefreshed());
        assertFalse(response.metadata().lastRefreshed().isBlank());

        assertNotNull(response.monthlyData());
        assertFalse(response.monthlyData().isEmpty());
        assertTrue(response.monthlyData().size() > 1);
    }
}
