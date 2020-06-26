package com.example.currency_exchange.validator;

import com.example.currency_exchange.model.UserForm;
import org.apache.commons.validator.GenericValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class RegistrationFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass == UserForm.class;
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserForm userForm = (UserForm) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fullName", "registrationForm.name.required");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "registrationForm.email.required");
        if (!errors.hasFieldErrors("email")) {
            if (!GenericValidator.isEmail(userForm.getEmail())) {
                errors.rejectValue("email", "registrationForm.email.invalid");
            }
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "registrationForm.password.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "registrationForm.passwordConfirmation.required");
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())) {
            errors.rejectValue("passwordConfirm", "registrationForm.password.mismatch");
        }

    }
}
