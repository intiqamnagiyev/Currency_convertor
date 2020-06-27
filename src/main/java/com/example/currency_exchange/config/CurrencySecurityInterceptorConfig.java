package com.example.currency_exchange.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CurrencySecurityInterceptorConfig implements WebMvcConfigurer {
  private final   CurrencySecurityInterceptor currencySecurityInterceptor;

    public CurrencySecurityInterceptorConfig(CurrencySecurityInterceptor currencySecurityInterceptor) {
        this.currencySecurityInterceptor = currencySecurityInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(currencySecurityInterceptor)
                .addPathPatterns("/web/**");
    }
}
