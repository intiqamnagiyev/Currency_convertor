package com.example.currency_exchange.service.impl;

import com.example.currency_exchange.api.external.CurrencyApi;
import com.example.currency_exchange.api.external.RateHistory;
import com.example.currency_exchange.api.send_to_frontend.RateApi;
import com.example.currency_exchange.api.stored_db.Rate;
import com.example.currency_exchange.model.HistoryResponse;
import com.example.currency_exchange.repository.CurrencyRepository;
import com.example.currency_exchange.service.CurrencyService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2
@Service
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;

    @Value("${currency.apiUrl}")
    private String baseUrlApi;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }


    public BigDecimal convert1(String from, String to, BigDecimal amount) {
        final CurrencyApi body = null;
        if (body != null) {
            boolean isExpired = checkDateExpired(LocalDate.parse(body.getDate()));
            if (isExpired) {
                final List<Rate> rates = body.getRates().entrySet()
                        .stream()
                        .map(entry -> new Rate(entry.getKey(), BigDecimal.valueOf(Double.parseDouble(entry.getValue())), LocalDate.parse(body.getDate())))
                        .collect(Collectors.toList());
                final List<Rate> savedRates = currencyRepository.saveAll(rates);
            }

        }
        final String s = Objects.requireNonNull(body).getRates().get(from);
        final BigDecimal bigDecimal = BigDecimal.valueOf(Double.parseDouble(s));
        return amount.divide(bigDecimal, 3, RoundingMode.CEILING);
    }

    @Override
    public BigDecimal convert(String from, String to, BigDecimal amount) {
        try {
            final RateApi rates = parseSingleRate(generateUrlForSingleRate(from, to)).getBody();
            if (rates == null) return BigDecimal.ZERO;
            return rates.getRates()
                    .getOrDefault(to, BigDecimal.ONE)
                    .multiply(amount)
                    .setScale(3, RoundingMode.CEILING).
                            stripTrailingZeros();
        } catch (HttpClientErrorException httpClientErrorException) {
            return BigDecimal.ZERO;
        }
    }

    private boolean checkDateExpired(LocalDate apiDate) {
        return currencyRepository
                .findAll()
                .stream()
                .map(Rate::getDate).max((o1, o2) -> o2.compareTo(o1))
                .orElse(apiDate.minusDays(1))
                .isBefore(apiDate);
    }

    @Override
    public RateHistory showHistory(String from, String to, String startAt, String endAt) {
        final RateHistory rateHistory = parseRateWithHistory(from, to, startAt, endAt);
        final RateHistory rateHistory1 = parseRateWithHistory(to, from, startAt, endAt);
        if (rateHistory==null) return new RateHistory();


        //todo wip
        System.out.println(rateHistory);
        System.out.println("=======");
        System.out.println(rateHistory1);
        return rateHistory;
    }

    private ResponseEntity<RateApi> parseSingleRate(String url) {
        return new RestTemplate().getForEntity(url, RateApi.class);
    }
    private RateHistory parseRateWithHistory(String from, String to, String startAt, String endAt){
        return new RestTemplate().getForEntity(generateUrlForHistory(
                from, to, LocalDate.parse(startAt), LocalDate.parse(endAt)), RateHistory.class).getBody();
    }

    private String generateUrlForSingleRate(String from, String to) {
        return String.format(baseUrlApi + "latest?symbols=%s&base=%s", to, from);
    }

    private String generateUrlForHistory(String symbol, String base, LocalDate startAt, LocalDate endAt) {
        return String.format(baseUrlApi + "history?start_at=%s&end_at=%s&symbols=%s&base=%s", startAt, endAt, symbol, base);
    }


}
