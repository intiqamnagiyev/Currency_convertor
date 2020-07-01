package com.example.currency_exchange.controller;

import com.example.currency_exchange.api.stored_db.Rate;
import com.example.currency_exchange.service.CurrencyService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
    public BigDecimal convert(@RequestParam(value = "from", required = false, defaultValue = "USD") String from,
                              @RequestParam(value = "to", required = false, defaultValue = "EUR") String to,
                              @RequestParam(value = "amount", required = false, defaultValue = "0") BigDecimal amount) {
        return currencyService.convert(from, to, amount);
    }

    /**
     *http://localhost:8080/currency/history
     */
    @GetMapping("/history")
    @ResponseBody
    public List<Rate> history(@RequestParam(value = "fromDate", defaultValue = "2020-05-30") String fromDate,
                              @RequestParam(value = "toDate", defaultValue = "2020-07-30") String toDate) {
        return currencyService.getHistory(LocalDate.parse(fromDate), LocalDate.parse(toDate));
    }

}
