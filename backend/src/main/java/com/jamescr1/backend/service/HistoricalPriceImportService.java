package com.jamescr1.backend.service;

import com.jamescr1.backend.client.AlphaVantageClient;
import com.jamescr1.backend.client.dto.AlphaVantageMonthlyResponse;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        boolean priceHistoryNeedsUpdate = checkIfNeedsUpdatedPrice(investmentRecord, monthlyResponse);
        if(priceHistoryNeedsUpdate) {
            updateDb();
        } else {
            return;
        }
    }

    private void updateDb() {
        
    }

    private boolean checkIfNeedsUpdatedPrice(Investment investment, AlphaVantageMonthlyResponse monthlyResponse) {
        List<HistoricalPrice> historicalPricesInDB = historicalPriceRepository.findAllByInvestment(investment);
        //if no historic prices in DB for this investment then return true
        if(historicalPricesInDB.isEmpty()) {
            return true;
        }

        //checking if the latest month present in the DB is chronologically before the latest month in the API's data
        YearMonth latestDBDate = getLatestDBDate(investment, historicalPricesInDB);
        YearMonth latestApiDate = getLatestApiDate(monthlyResponse);
        boolean dbNeedsUpdating;
        //if most recent date in DB is older than most recent date in API response DB needs updating
        dbNeedsUpdating = latestDBDate.isBefore(latestApiDate);

        return dbNeedsUpdating;
    }

    private YearMonth getLatestDBDate(Investment investment, List<HistoricalPrice> historicalPricesInDB) {
        List<YearMonth> yearMonthsAlreadyInDB = getYearMonthsForInvestment(historicalPricesInDB);
        Optional<YearMonth> mostRecentMonth = yearMonthsAlreadyInDB.stream().max(YearMonth::compareTo);
        return mostRecentMonth.orElseThrow(() -> new ResourceNotFoundException("No latest month found"));
    }

    private YearMonth getLatestApiDate(AlphaVantageMonthlyResponse monthlyResponse) {
        String latestDateAsString = monthlyResponse.metadata().lastRefreshed();
        //converting string to local date, so it can then be converted to type YearMonth
        LocalDate latestLocalDate = LocalDate.parse(latestDateAsString);
        YearMonth latestYrMonthDate = YearMonth.from(latestLocalDate);
        return latestYrMonthDate;
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




}
