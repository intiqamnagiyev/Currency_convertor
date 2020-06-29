package com.example.currency_exchange.service.impl;

import com.example.currency_exchange.model.Currency;
import com.example.currency_exchange.service.CurrencyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Random;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    @Value("${currency.apiUrl}")
    private String baseUrlApi;

    @Override
    public BigDecimal convert(String from, String to, BigDecimal amount, String fromDate, String toDate) {
        final RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            final ResponseEntity<String> response = restTemplate.exchange("https://mvnrepository.com/", HttpMethod.GET, entity, String.class);
            System.out.println(response.getBody());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        final ResponseEntity<Currency> forEntity = restTemplate.getForEntity(baseUrlApi + "latest", Currency.class);
                return BigDecimal.valueOf(new Random().nextInt(100));
    }
}
