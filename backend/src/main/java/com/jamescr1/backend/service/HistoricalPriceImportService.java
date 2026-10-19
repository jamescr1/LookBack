package com.jamescr1.backend.service;

import com.jamescr1.backend.client.AlphaVantageClient;
import com.jamescr1.backend.client.dto.AlphaVantageMonthlyResponse;
import com.jamescr1.backend.client.dto.MonthlyAdjustedDataPoint;
import com.jamescr1.backend.entity.HistoricalPrice;
import com.jamescr1.backend.entity.Investment;
import com.jamescr1.backend.exception.ResourceNotFoundException;
import com.jamescr1.backend.repository.HistoricalPriceRepository;
import com.jamescr1.backend.repository.InvestmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.*;

@Service
@RequiredArgsConstructor
public class HistoricalPriceImportService {
    private final HistoricalPriceRepository historicalPriceRepository;
    private final InvestmentRepository investmentRepository;
    private final AlphaVantageClient alphaVantageClient;

    public void importHistoricalPriceData(Long investmentId) {
        Investment investment = investmentRepository.findById(investmentId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<HistoricalPrice> dbMonthlyHistory = historicalPriceRepository.findAllByInvestment(investment);

        String alphaVantageSymbol = investment.getAlphaVantageSymbol();
        Map<String, MonthlyAdjustedDataPoint> apiMonthlyHistory = alphaVantageClient.getMonthlyData(alphaVantageSymbol).monthlyData();
    }

    //this method converts the LocalDate 'priceDate' field of all the provided HistoricalPrice objects to
    //a YearMonth format and returns the resulting list of YearMonths
    private List<YearMonth> getYearMonthsForInvestment(List<HistoricalPrice> historicalPrices) {
        List<YearMonth> listOfMonthsInDB = new ArrayList<>();
        for(HistoricalPrice priceObj : historicalPrices) {
            YearMonth convertedDateFormat = YearMonth.from(priceObj.getPriceDate());
            listOfMonthsInDB.add(convertedDateFormat);
        }
        return listOfMonthsInDB;
    }

    private Map<YearMonth, HistoricalPrice> convertToYrMonthMap(List<HistoricalPrice> listToConvert) {
        Map<YearMonth, HistoricalPrice> mapToReturn = new HashMap<>();

        for(HistoricalPrice currEl : listToConvert) {
            LocalDate localDate = currEl.getPriceDate();
            YearMonth yrMonth = YearMonth.from(localDate);
            mapToReturn.put(yrMonth, currEl);
        }
        return mapToReturn;
    }

    private void processAPIData (Map<String, MonthlyAdjustedDataPoint> apiMonthlyData) {
        for(Map.Entry<String, MonthlyAdjustedDataPoint> entry : apiMonthlyData.entrySet()) {
            String key = entry.getKey();
            MonthlyAdjustedDataPoint value = entry.getValue();

        }
    }




}
