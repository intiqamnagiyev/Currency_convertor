package com.example.currency_exchange.handler;

import com.example.currency_exchange.ex.BusinessEx;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * only for handling exceptions
 * by application-wide
 */
@Log4j2
@ControllerAdvice
public class WholeAppCurrencyExHandler {

  //@ExceptionHandler({ BusinessEx.class })
  public ModelAndView handleErr(HttpServletRequest rq, Exception ex) {
    log.info("caught: BusinessEx");
    ModelAndView mav = new ModelAndView();
    mav.addObject("url", rq.getRequestURL());
    mav.addObject("ex", ex);
    mav.addObject("name:", ex.getClass().getSimpleName());
    mav.setViewName("handler_book_not_found");
    return mav;
  }

}
