package com.example.currency_exchange.api.send_to_frontend;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RateSaver {
    LocalDate date;
    BigDecimal value;
}
