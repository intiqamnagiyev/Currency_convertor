package com.example.currency_exchange.controller;

import com.example.currency_exchange.model.Currency;
import com.example.currency_exchange.service.CurrencyService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

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
    public BigDecimal convert(@RequestParam(value = "from",required = false,defaultValue = "USD") String from,
                                @RequestParam(value = "to" ,required = false, defaultValue = "EUR") String to,
                                @RequestParam(value = "amount", required = false) BigDecimal amount,
                                @RequestParam(value = "fromDate", required = false, defaultValue = "") String fromDate,
                                @RequestParam(value = "toDate", required = false, defaultValue = "") String toDate) {
        return currencyService.convert(from, to, amount, fromDate, toDate);
    }
}
