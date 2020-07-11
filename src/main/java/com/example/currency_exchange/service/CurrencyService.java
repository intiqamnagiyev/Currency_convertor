package com.example.currency_exchange.service;

import com.example.currency_exchange.api.external.CurrencyApi;
import com.example.currency_exchange.api.external.RateHistory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public interface CurrencyService {
    BigDecimal convert(String from, String to, BigDecimal amount);
    RateHistory showHistory(String from, String to, String startAt, String endAt);

}
