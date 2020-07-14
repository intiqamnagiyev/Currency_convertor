package com.example.currency_exchange.api.send_to_frontend;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class RateSaver {
    LocalDate date;
    double value;
    double inverseValue;
}
