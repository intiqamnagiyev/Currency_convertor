package com.example.currency_exchange.service;

import com.example.currency_exchange.model.Currency;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface CurrencyService {
    Currency convert(String from, String to, BigDecimal amount, String fromDate, String toDate);
}
