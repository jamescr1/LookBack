package com.jamescr1.backend.client;

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

    public String getMonthlyData(String ticker) {
        return restClient.get()
                .uri("query?function=TIME_SERIES_MONTHLY_ADJUSTED&symbol={ticker}&apikey={apiKey}", ticker, apiKey)
                .accept(APPLICATION_JSON)
                .retrieve()
                .body(String.class);
    }

}
