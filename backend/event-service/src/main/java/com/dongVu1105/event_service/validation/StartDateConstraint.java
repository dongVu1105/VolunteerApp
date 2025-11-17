package com.dongVu1105.event_service.validation;



import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {StartDateValidator.class})
public @interface StartDateConstraint {
    String message() default "Invalid start date";

    int min();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
