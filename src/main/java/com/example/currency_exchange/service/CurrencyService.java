package com.example.currency_exchange.service;

import com.example.currency_exchange.api.send_to_frontend.RateSaver;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public interface CurrencyService {
    BigDecimal convert(String from, String to, BigDecimal amount);

    List<RateSaver> convertWithHistory(String fromDate, String toDate, String baseFrom, String baseTo);


}
