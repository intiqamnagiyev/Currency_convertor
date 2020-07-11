package com.example.currency_exchange.util;

import com.example.currency_exchange.entity.User;
import com.example.currency_exchange.model.UserForm;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ConvertUtil {
    @Autowired
    private static PasswordEncoder enc;

    public static User convert(UserForm userForm){
       return User
                .builder()
               .fullName(userForm.getFullName())
               .email(userForm.getEmail())
               .password(BCrypt.hashpw(userForm.getPassword(),BCrypt.gensalt()))
               .build();

    }

    public static User convert2(UserForm userForm){
        return User.builder()
                .fullName(userForm.getFullName())
                .email(userForm.getEmail())
                .password(enc.encode(userForm.getPassword())).build();

    }
}
