package com.jamescr1.backend.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AlphaVantageMonthlyMetadata(
        @JsonProperty("2. Symbol")
        String symbol,
        @JsonProperty("3. Last Refreshed")
        String lastRefreshed
) {
}
