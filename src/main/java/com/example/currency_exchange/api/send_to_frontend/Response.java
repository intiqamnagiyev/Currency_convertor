package com.example.currency_exchange.api.send_to_frontend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    LocalDate start_at;
    LocalDate end_at;
    String base;
    Map<String, Map<String,Double>> rates;

}
