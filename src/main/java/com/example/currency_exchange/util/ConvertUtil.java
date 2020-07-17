package com.example.currency_exchange.util;

import com.example.currency_exchange.entity.Person;
import com.example.currency_exchange.model.PersonForm;
import org.mindrot.jbcrypt.BCrypt;

public class ConvertUtil {
    public static Person convert(PersonForm personForm){
       return Person
                .builder()
               .fullName(personForm.getFullName())
               .email(personForm.getEmail())
               .password(BCrypt.hashpw(personForm.getPassword(),BCrypt.gensalt()))
               .build();

    }
}
