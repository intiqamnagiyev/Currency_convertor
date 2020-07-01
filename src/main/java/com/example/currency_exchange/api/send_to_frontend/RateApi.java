package com.example.currency_exchange.api.send_to_frontend;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
public class RateApi {
    private Map<String, BigDecimal> rates;
    private LocalDate date;
}
