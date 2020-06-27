package com.example.currency_exchange.validator;
import com.example.currency_exchange.model.LoginUser;
import com.example.currency_exchange.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


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

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "registrationForm.email.invalid");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password","registrationForm.password.enter");

    }


}
