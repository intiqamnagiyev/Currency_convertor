package com.example.currency_exchange.controller;

import com.example.currency_exchange.api.external.RateHistory;
import com.example.currency_exchange.ex.RateNotFoundExc;
import com.example.currency_exchange.model.HistoryResponse;
import com.example.currency_exchange.service.CurrencyService;
import com.sun.webkit.Timer;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.Arrays;
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
    public BigDecimal convert(@RequestParam(value = "from", required = false) String from,
                              @RequestParam(value = "to", required = false) String to,
                              @RequestParam(value = "amount", required = false, defaultValue = "0") BigDecimal amount) {
        return currencyService.convert(from, to, amount);
    }

    /**
     * http://localhost:8080/currency/history
     */
    @GetMapping("history")

    public ModelAndView history(@RequestParam(value = "from", required = false, defaultValue = "EUR") String from,
                                         @RequestParam(value = "to", required = false, defaultValue = "USD") String to,
                                         @RequestParam(value = "startAt", required = false, defaultValue = "2020-05-01") String startAt,
                                         @RequestParam(value = "endAt", required = false, defaultValue = "2020-06-01") String endAt) {
        final ModelAndView mav = new ModelAndView("rates");
        final List<HistoryResponse> historyResponses = Arrays.asList(new HistoryResponse("1usd/2eur", "02 july 2020", "1eur/3usd"),
                new HistoryResponse("1usd/2eur", "02 july 2020", "1eur/3usd"));
        mav.addObject("rates", historyResponses);
        return mav;
    }

    @ExceptionHandler({RateNotFoundExc.class})
    public ModelAndView handleError(Exception x) {
        log.info("caught: BadRequest");
        log.info(x.getClass().getSimpleName());
        return new ModelAndView("redirect:/web/main-page-guest");
    }

//todo RateNotFoundex handler

}
