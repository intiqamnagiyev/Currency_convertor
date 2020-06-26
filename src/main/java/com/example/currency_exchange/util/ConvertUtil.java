package com.example.currency_exchange.util;

import com.example.currency_exchange.entity.User;
import com.example.currency_exchange.model.UserForm;
import org.mindrot.jbcrypt.BCrypt;

public class ConvertUtil {
    public static User convert(UserForm userForm){
       return User
                .builder()
               .fullName(userForm.getFullName())
               .email(userForm.getEmail())
               .password(BCrypt.hashpw(userForm.getPassword(),BCrypt.gensalt()))
               .build();

    }
}
