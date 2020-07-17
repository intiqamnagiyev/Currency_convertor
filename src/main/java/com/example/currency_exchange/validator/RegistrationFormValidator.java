package com.example.currency_exchange.validator;

import com.example.currency_exchange.model.PersonForm;
import com.example.currency_exchange.service.PersonService;
import org.apache.commons.validator.GenericValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class RegistrationFormValidator implements Validator {
    private final PersonService personService;

    public RegistrationFormValidator(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass == PersonForm.class;
    }

    @Override
    public void validate(Object o, Errors errors) {
        PersonForm personForm = (PersonForm) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fullName", "registrationForm.name.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "registrationForm.email.required");
        if (!errors.hasFieldErrors("email")) {
            if (personService.checkEmail(personForm.getEmail())) {
                errors.rejectValue("email", "registrationForm.email.duplicate");
            } else if (!GenericValidator.isEmail(personForm.getEmail())) {
                errors.rejectValue("email", "registrationForm.email.invalid");
            }
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "registrationForm.password.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "registrationForm.passwordConfirmation.required");
        if (!personForm.getPassword().equals(personForm.getPasswordConfirm())) {
            errors.rejectValue("passwordConfirm", "registrationForm.password.mismatch");
        }

    }
}
