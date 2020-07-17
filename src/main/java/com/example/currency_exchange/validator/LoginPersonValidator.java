package com.example.currency_exchange.validator;
import com.example.currency_exchange.model.LoginPerson;
import com.example.currency_exchange.service.PersonService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


@Component
public class LoginPersonValidator implements Validator {
    private final PersonService personService;

    public LoginPersonValidator(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass == LoginPerson.class;
    }

    @Override
    public void validate(Object o, Errors errors) {
        LoginPerson loginPerson = (LoginPerson) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "registrationForm.email.invalid");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password","registrationForm.password.enter");
        final String password = personService.getPersonByEmail(loginPerson.getEmail()).getPassword();
        if (!BCrypt.checkpw(loginPerson.getPassword(), password)){
            errors.rejectValue("password", "registrationForm.password.invalid");
        }
    }


}
