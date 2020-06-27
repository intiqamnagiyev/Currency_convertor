package com.example.currency_exchange.service.impl;

import com.example.currency_exchange.model.Currency;
import com.example.currency_exchange.service.CurrencyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    @Value("${currency.apiUrl}")
    private String baseUrlApi;

    @Override
    public Currency convert(String from, String to, BigDecimal amount, String fromDate, String toDate) {
        final RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<Currency> forEntity = restTemplate.getForEntity(baseUrlApi + "latest", Currency.class);
        return forEntity.getBody();
    }
}
