package com.jamescr1.backend.service;

import com.jamescr1.backend.client.AlphaVantageClient;
import com.jamescr1.backend.client.dto.AlphaVantageMonthlyResponse;
import com.jamescr1.backend.entity.Investment;
import com.jamescr1.backend.exception.ResourceNotFoundException;
import com.jamescr1.backend.repository.HistoricalPriceRepository;
import com.jamescr1.backend.repository.InvestmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoricalPriceImportService {
    private final HistoricalPriceRepository historicalPriceRepository;
    private final InvestmentRepository investmentRepository;
    private final AlphaVantageClient alphaVantageClient;

    public void importHistoricalPriceData(Long investmentId) {
        Investment investmentRecord = investmentRepository.findById(investmentId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String alphaVantageSymbol = investmentRecord.getAlphaVantageSymbol();

        AlphaVantageMonthlyResponse monthlyResponse = alphaVantageClient.getMonthlyData(alphaVantageSymbol);

        boolean alreadyInDB
    }


}
