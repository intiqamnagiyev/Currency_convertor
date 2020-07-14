package com.example.currency_exchange.api.send_to_frontend;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class RateItselfForHistory {
    Map<String, BigDecimal> rate;

}
