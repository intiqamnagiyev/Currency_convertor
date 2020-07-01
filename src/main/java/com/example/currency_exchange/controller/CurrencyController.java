package com.example.currency_exchange.controller;

import com.example.currency_exchange.service.CurrencyService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

@Log4j2
@Controller
@RequestMapping("/currency")
public class CurrencyController {
    private final CurrencyService currencyService;


    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping()
    @ResponseBody
    public BigDecimal convert(@RequestParam(value = "from", required = false) String from,
                              @RequestParam(value = "to", required = false) String to,
                              @RequestParam(value = "amount", required = false, defaultValue = "0") BigDecimal amount) {
         return currencyService.convert(from, to, amount);
    }

//todo RateNotFoundex handler

}
