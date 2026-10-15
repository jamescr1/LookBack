package com.jamescr1.backend.client;

import com.jamescr1.backend.client.dto.AlphaVantageMonthlyResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class AlphaVantageClient {
    private final RestClient restClient;
    private final String apiKey;

    public AlphaVantageClient(RestClient.Builder restClientBuilder, @Value("${alphavantage.api-key}") String apiKey) {
        this.restClient = restClientBuilder
                .baseUrl("https://www.alphavantage.co")
                .build();
        this.apiKey = apiKey;
    }

    public AlphaVantageMonthlyResponse getMonthlyData(String symbol) {
        return restClient.get()
                .uri("query?function=TIME_SERIES_MONTHLY_ADJUSTED&symbol={symbol}&apikey={apiKey}", symbol, apiKey)
                .accept(APPLICATION_JSON)
                .retrieve()
                .body(AlphaVantageMonthlyResponse.class);
    }

}
