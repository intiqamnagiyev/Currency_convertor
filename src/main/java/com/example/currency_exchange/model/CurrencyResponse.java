package com.example.currency_exchange.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrencyResponse {
    private final BigDecimal UsdToEur;
    private final BigDecimal EurToUsd;

}
