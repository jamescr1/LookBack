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
                .orElseThrow(() -> new ResourceNotFoundException("Investment not found"));
        List<HistoricalPrice> dbMonthlyHistory = historicalPriceRepository.findAllByInvestment(investment);

        String alphaVantageSymbol = investment.getAlphaVantageSymbol();
        Map<String, MonthlyAdjustedDataPoint> apiMonthlyHistory = alphaVantageClient.getMonthlyData(alphaVantageSymbol).monthlyData();

        Map<YearMonth, HistoricalPrice> dbMonthlyHistoryMap = convertToYrMonthMap(dbMonthlyHistory);
        Map<YearMonth, HistoricalPrice> monthlyDataToInsert = updateDB(investment, apiMonthlyHistory, dbMonthlyHistoryMap);
        historicalPriceRepository.saveAll(monthlyDataToInsert.values());
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

    private Map<YearMonth, HistoricalPrice> updateDB (Investment investment, Map<String, MonthlyAdjustedDataPoint> apiMonthlyData, Map<YearMonth, HistoricalPrice> dbMonthlyHistory) {
        for(Map.Entry<String, MonthlyAdjustedDataPoint> entry : apiMonthlyData.entrySet()) {
            String dateString = entry.getKey();
            MonthlyAdjustedDataPoint apiPriceData = entry.getValue();
            //converting monthly date string to YearMonth
            YearMonth apiRecordYearMonth = strToYearMonth(dateString);
            //finding whether db contains a record for this api data
            HistoricalPrice dbRecordToUpdate = dbMonthlyHistory.get(apiRecordYearMonth);
            //if record exists in db, then update record to ensure db is up to date with API,
            if(dbRecordToUpdate != null) {
                dbRecordToUpdate.updateFrom(dateString, apiPriceData);
            } else {  //else create new historicprice instance for this api data and add to map
                HistoricalPrice recordToInsert = HistoricalPrice.fromApiData(investment, dateString, apiPriceData);
                dbMonthlyHistory.put(apiRecordYearMonth, recordToInsert);
            }
        }
        return dbMonthlyHistory;
    }

    private YearMonth strToYearMonth(String strToConvert) {
        LocalDate localDate = LocalDate.parse(strToConvert);
        YearMonth yrMonth = YearMonth.from(localDate);
        return yrMonth;
    }






}
