package com.project.taskipro.model.user.email;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    @Override
    public void initialize(ValidEmail constraintAnnotation) {}

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isEmpty()) { return false;}
        String regex = "^[a-zA-Z0-9._%+-]+@(" + EmailDomains.getRegexForDomains() + ")$";
        return email.matches(regex);
    }
}
