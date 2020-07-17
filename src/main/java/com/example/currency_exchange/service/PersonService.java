package com.example.currency_exchange.service;

import com.example.currency_exchange.entity.Person;
import org.springframework.stereotype.Service;

@Service
public interface PersonService {
    Person save(Person person);

    Person getPersonByEmail(String email);

    boolean checkEmail(String email);
}
