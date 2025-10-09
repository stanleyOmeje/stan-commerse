package com.stan.stancommerce.validation;

import com.stan.stancommerce.annotation.LowercaseAnnotation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LowercaseValidator implements ConstraintValidator<LowercaseAnnotation, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(value == null || value.isEmpty()){
            return true;
        }
        return value.equals(value.toLowerCase());
    }
}
