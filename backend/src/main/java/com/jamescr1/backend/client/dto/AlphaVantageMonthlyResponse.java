package com.jamescr1.backend.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record AlphaVantageMonthlyResponse(
        @JsonProperty("Meta Data")
        AlphaVantageMonthlyMetadata metadata,
        @JsonProperty("Monthly Adjusted Time Series")
        Map<String, MonthlyAdjustedDataPoint> monthlyData
) {
}