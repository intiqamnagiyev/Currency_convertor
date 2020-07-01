package com.example.currency_exchange.service.impl;

import com.example.currency_exchange.api.external.CurrencyApi;
import com.example.currency_exchange.api.send_to_frontend.RateApi;
import com.example.currency_exchange.api.stored_db.Rate;
import com.example.currency_exchange.repository.CurrencyRepository;
import com.example.currency_exchange.service.CurrencyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;
    @Value("${currency.apiUrl}")
    private String baseUrlApi;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }


    public BigDecimal convert1(String from, String to, BigDecimal amount) {
        final CurrencyApi body = parseWithHistory();
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
        final RateApi rates = parse(generateUrlForSingleRate(from, to));
        if (rates == null) return BigDecimal.ZERO;
        return rates.getRates()
                .getOrDefault(to, BigDecimal.ONE)
                .multiply(amount)
                .setScale(3, RoundingMode.CEILING).
                        stripTrailingZeros();
    }

    private boolean checkDateExpired(LocalDate apiDate) {
        return currencyRepository
                .findAll()
                .stream()
                .map(Rate::getDate).max((o1, o2) -> o2.compareTo(o1))
                .orElse(apiDate.minusDays(1))
                .isBefore(apiDate);
    }

    private CurrencyApi parseWithHistory() {
        return new RestTemplate().getForEntity(baseUrlApi + "latest", CurrencyApi.class).getBody();
    }

    private RateApi parse(String url) {
        return new RestTemplate().getForEntity(url, RateApi.class).getBody();
    }

    private String generateUrlForSingleRate(String from, String to) {
        return String.format(baseUrlApi + "latest?symbols=%s&base=%s", to, from);
    }


}
