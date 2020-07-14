package com.example.currency_exchange.service.impl;

import com.example.currency_exchange.api.send_to_frontend.RateApi;
import com.example.currency_exchange.api.send_to_frontend.RateSaver;
import com.example.currency_exchange.api.send_to_frontend.Response;
import com.example.currency_exchange.api.stored_db.Rate;
import com.example.currency_exchange.repository.CurrencyRepository;
import com.example.currency_exchange.service.CurrencyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;
    @Value("${currency.apiUrl}")
    private String baseUrlApi;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }


//    public BigDecimal convert1(String from, String to, BigDecimal amount) {
//        final CurrencyApi body = parseWithHistory();
//        if (body != null) {
//            boolean isExpired = checkDateExpired(LocalDate.parse(body.getDate()));
//            if (isExpired) {
//                final List<Rate> rates = body.getRates().entrySet()
//                        .stream()
//                        .map(entry -> new Rate(entry.getKey(), BigDecimal.valueOf(Double.parseDouble(entry.getValue())), LocalDate.parse(body.getDate())))
//                        .collect(Collectors.toList());
//                final List<Rate> savedRates = currencyRepository.saveAll(rates);
//            }
//
//        }
//        final String s = Objects.requireNonNull(body).getRates().get(from);
//        final BigDecimal bigDecimal = BigDecimal.valueOf(Double.parseDouble(s));
//        return amount.divide(bigDecimal, 3, RoundingMode.CEILING);
//    }

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

    @Override
    public List<RateSaver> convertWithHistory(String fromDate, String toDate, String baseFrom, String baseTo) {
        fromDate = parseStringToDATE(fromDate);
        toDate = parseStringToDATE(toDate);
        String url = generateUrlWithHistory(fromDate, toDate, baseFrom, baseTo);
        Response rates = parseForHistory(url);
        Set<String> dates = rates.getRates().keySet();
        rates.getRates().get("2020-01-01");
        List<RateSaver> collected = dates.stream().map(d -> {
            Map<String, Double> one = rates.getRates().get(d);
            return new RateSaver(d, one.get(baseTo), (1 / one.get(baseTo)));
        }).collect(Collectors.toList());

        return collected;

    }

    private boolean checkDateExpired(LocalDate apiDate) {
        return currencyRepository
                .findAll()
                .stream()
                .map(Rate::getDate).max((o1, o2) -> o2.compareTo(o1))
                .orElse(apiDate.minusDays(1))
                .isBefore(apiDate);
    }

//    private CurrencyApi parseWithHistory() {
//        return new RestTemplate().getForEntity(baseUrlApi + "latest", CurrencyApi.class).getBody();
//    }

    private RateApi parse(String url) {
        return new RestTemplate().getForEntity(url, RateApi.class).getBody();
    }

    private Response parseForHistory(String url) {
        return new RestTemplate().getForEntity(url, Response.class).getBody();
    }

    private String generateUrlForSingleRate(String from, String to) {
        return String.format(baseUrlApi + "latest?symbols=%s&base=%s", to, from);
    }

    private String generateUrlWithHistory(String fromDate, String toDate, String baseFrom, String baseTo) {
        return String.format(baseUrlApi + "history?start_at=%S&end_at=%s&base=%s&symbols=%s", fromDate, toDate, baseFrom, baseTo);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////


    // into format ->  2018-01-01
    public String parseStringToDATE(String date) {
        Map<String, String> calendar = new HashMap() {{
            put("January", "01");
            put("February", "02");
            put("March", "03");
            put("April", "04");
            put("May", "05");
            put("June", "06");
            put("July", "07");
            put("August", "08");
            put("September", "09");
            put("October", "10");
            put("November", "11");
            put("December", "12");
        }};
        // we get from FrontEnd via @RequestParam  -> "07 July"
        String[] splitted = date.split(" ");
        String year = "2020"; // we cant get the year. So let it be the worst year ever
        String month = calendar.get(splitted[1]);
        String day = splitted[0];
        return String.format("%s-%s-%s", year, month, day);
    }

}
