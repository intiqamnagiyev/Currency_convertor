package com.example.currency_exchange.service.impl;

import com.example.currency_exchange.api.external.Currency;
import com.example.currency_exchange.api.external.RateApi;
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
    @Value("${base.currency}")
    private String baseCur;


    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }


    public BigDecimal convert1(String from, String to, BigDecimal amount) {
        final Currency body = parseWithHistory();
        if (body != null) {
            boolean isExpired = checkDateExpired(LocalDate.parse(body.getDate()));
            if (isExpired) {
                final List<Rate> rates = body.getRates().entrySet()
                        .stream()
                        .map(entry -> new Rate(entry.getKey(), entry.getValue(), LocalDate.parse(body.getDate())))
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
        final RateApi rates = parse(url(from, to));

        return BigDecimal.valueOf(Double.parseDouble(rates.getRates().get(to)))
                .multiply(amount)
                .setScale(3,RoundingMode.CEILING)
                .stripTrailingZeros();
    }

    private boolean checkDateExpired(LocalDate apiDate) {
        return currencyRepository
                .findAll()
                .stream()
                .map(Rate::getDate).max((o1, o2) -> o2.compareTo(o1))
                .orElse(apiDate.minusDays(1))
                .isBefore(apiDate);
    }

    private Currency parseWithHistory() {
        return new RestTemplate().getForEntity(baseUrlApi + "latest", Currency.class).getBody();
    }

    private RateApi parse(String url) {
        return new RestTemplate().getForEntity(url, RateApi.class).getBody();
    }

    private String url(String from, String to) {
        return String.format(baseUrlApi + "latest?symbols=%s&base=%s", to, from);
    }

    @Override
    public List<Rate> getHistory(LocalDate fromDate, LocalDate toDate) {
        return currencyRepository.findAllByDateBetween(fromDate, toDate);

    }
}
