package com.example.currency_exchange.api.send_to_frontend;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class RateSaver {
    String date;
    double value;
    double inverseValue;
}
