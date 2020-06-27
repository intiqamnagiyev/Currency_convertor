package com.example.currency_exchange.model;

import lombok.Data;

import java.util.Map;

@Data
public class Currency {
private String base;
private String date;
private Map<String, String> rates;
}
