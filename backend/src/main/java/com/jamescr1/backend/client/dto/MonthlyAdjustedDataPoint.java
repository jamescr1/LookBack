package com.jamescr1.backend.client.dto;

import java.math.BigDecimal;

public record MonthlyAdjustedDataPoint(
        BigDecimal open,
        BigDecimal high,
        BigDecimal low,
        BigDecimal close,
        BigDecimal adjustedClose,
        Long volume,
        BigDecimal dividendAmount
) {
}
