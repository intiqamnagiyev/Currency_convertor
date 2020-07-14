package com.example.currency_exchange.api.send_to_frontend;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
public class RateApiHistory {
    private Map<LocalDate, RateItselfForHistory> rates;
    private LocalDate startAt;
    private LocalDate endAt;
}
