package com.stan.stancommerce.annotation;

import com.stan.stancommerce.validation.LowercaseValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = LowercaseValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LowercaseAnnotation {
    public String message() default "must be a lower case letter";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
