package com.example.currency_exchange.controller;

import com.example.currency_exchange.model.Currency;
import com.example.currency_exchange.service.CurrencyService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    public ModelAndView convert(@RequestParam("from") String from,
                                @RequestParam("to") String to,
                                @RequestParam("amount") BigDecimal amount,
                                @RequestParam(value = "fromDate", required = false, defaultValue = "") String fromDate,
                                @RequestParam(value = "toDate", required = false, defaultValue = "") String toDate) {
        final ModelAndView mav = new ModelAndView("main-page-authorized");
        Currency currency= currencyService.convert(from, to, amount, fromDate, toDate);
        mav.addObject("currency", currency);
        return mav;
    }
}
