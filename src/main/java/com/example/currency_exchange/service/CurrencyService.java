package com.example.currency_exchange.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public interface CurrencyService {
    BigDecimal convert(String from, String to, BigDecimal amount);


}
