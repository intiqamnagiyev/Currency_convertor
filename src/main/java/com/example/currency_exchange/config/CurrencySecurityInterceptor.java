package com.example.currency_exchange.config;

import com.example.currency_exchange.entity.Person;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class CurrencySecurityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final Person person = (Person) request.getSession().getAttribute("person");
        boolean flag = false;

        if (request.getRequestURI().endsWith("/main-page-auth")) {
            if (person == null) {
                response.sendRedirect("/web/main-page-guest");
            } else flag = true;
        } else flag = true;
        return flag;
    }
}
