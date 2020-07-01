package com.example.currency_exchange.service;

import com.example.currency_exchange.api.stored_db.Rate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public interface CurrencyService {
    BigDecimal convert(String from, String to, BigDecimal amount);

    List<Rate> getHistory(LocalDate fromDate, LocalDate toDate);
}
