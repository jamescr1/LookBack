package com.jamescr1.backend.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record MonthlyAdjustedDataPoint(
        @JsonProperty("1. open")
        BigDecimal open,

        @JsonProperty("2. high")
        BigDecimal high,

        @JsonProperty("3. low")
        BigDecimal low,

        @JsonProperty("4. close")
        BigDecimal close,

        @JsonProperty("5. adjusted close")
        BigDecimal adjustedClose,

        @JsonProperty("6. volume")
        Long volume,

        @JsonProperty("7. dividend amount")
        BigDecimal dividendAmount
) {
}
