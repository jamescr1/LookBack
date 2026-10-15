package com.jamescr1.backend.client.dto;

import java.util.Map;

public record AlphaVantageMonthlyResponse(
        AlphaVantageMonthlyMetaData metaData,
        Map<String, MonthlyAdjustedDataPoint> monthlyData
) {
}