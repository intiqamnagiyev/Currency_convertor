package com.example.currency_exchange.service.impl;

import com.example.currency_exchange.model.Currency;
import com.example.currency_exchange.service.CurrencyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    @Value("${currency.apiUrl}")
    private String baseUrlApi;
    @Value("${base.currency}")
    private String baseCur;

    @Override
    public BigDecimal convert(String from, String to, BigDecimal amount, String fromDate, String toDate) {
        final RestTemplate restTemplate = new RestTemplate();

        final ResponseEntity<Currency> forEntity = restTemplate.getForEntity(baseUrlApi + "latest", Currency.class);
        final String s = Objects.requireNonNull(forEntity.getBody()).getRates().get(from);
        final BigDecimal bigDecimal = BigDecimal.valueOf(Double.parseDouble(s));
        return amount.divide(bigDecimal,3, RoundingMode.CEILING );
    }
}
