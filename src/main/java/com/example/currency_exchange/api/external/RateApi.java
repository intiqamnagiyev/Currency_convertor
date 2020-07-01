package com.example.currency_exchange.api.external;


import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
public class RateApi {
    private Map<String, String> rates;
    private LocalDate date;
}
