package com.example.currency_exchange.config;

import com.example.currency_exchange.entity.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class CurrencySecurityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final User user = (User) request.getSession().getAttribute("user");
        boolean flag = false;

        if (request.getRequestURI().endsWith("/main-page-auth")) {
            if (user == null) {
                response.sendRedirect("/web/main-page-guest");
            } else flag = true;
        } else flag = true;
        return flag;
    }
}
