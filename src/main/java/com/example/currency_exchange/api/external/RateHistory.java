package com.example.currency_exchange.api.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateHistory {
    private  Map<LocalDate, Map<String, BigDecimal>> rates;

}
