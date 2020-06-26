package com.example.currency_exchange.validator;

import com.example.currency_exchange.entity.User;
import com.example.currency_exchange.model.LoginUser;
import com.example.currency_exchange.service.UserService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class LoginUserValidator implements Validator {
    private final UserService userService;

    public LoginUserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass == LoginUser.class;
    }

    @Override
    public void validate(Object o, Errors errors) {
        LoginUser loginUser = (LoginUser) o;
        if (!getUser(loginUser).isPresent()) {
            errors.rejectValue("email", "registrationForm.email.invalid");
        }
        if (!BCrypt.checkpw(loginUser.getPassword(), getUser(loginUser).get().getPassword())) {
            errors.rejectValue("password", "registrationForm.password.invalid");
        }
    }

    private Optional<User> getUser(LoginUser loginUser) {
        return userService.getUserByEmail(loginUser);
    }
}
