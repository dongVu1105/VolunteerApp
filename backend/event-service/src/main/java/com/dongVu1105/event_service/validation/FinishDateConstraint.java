package com.dongVu1105.event_service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {FinishDateValidator.class})
public @interface FinishDateConstraint {
    String message() default "Finish date must be after start date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}