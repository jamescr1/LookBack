package com.jamescr1.backend.controller;

import com.jamescr1.backend.service.HistoricalPriceImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/investments/{investmentId}/historical-prices")
@RequiredArgsConstructor
public class HistoricalPriceImportController {

    private final HistoricalPriceImportService priceImportService;

    @PostMapping("/import")
    public ResponseEntity<Void> importHistoricPriceData(@PathVariable Long investmentId) {
        priceImportService.importHistoricalPriceData(investmentId);
        return ResponseEntity.noContent().build();
    }
}
