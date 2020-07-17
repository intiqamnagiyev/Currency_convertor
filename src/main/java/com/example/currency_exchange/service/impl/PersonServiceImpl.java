package com.example.currency_exchange.service.impl;

import com.example.currency_exchange.entity.Person;
import com.example.currency_exchange.ex.PersonNotFoundEx;
import com.example.currency_exchange.repository.PersonRepository;
import com.example.currency_exchange.service.PersonService;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;


    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person save(Person person) {
        return personRepository.save(person);
    }

    @Override
    public Person getPersonByEmail(String email) {
        return personRepository.findByEmail(email)
                .orElseThrow(PersonNotFoundEx::new);
    }

    @Override
    public boolean checkEmail(String email) {
        return personRepository.findByEmail(email).isPresent();
    }

}
