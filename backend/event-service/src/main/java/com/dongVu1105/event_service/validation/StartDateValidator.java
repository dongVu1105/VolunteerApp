package com.dongVu1105.event_service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class StartDateValidator implements ConstraintValidator<StartDateConstraint, Instant> {
    private int min;

    @Override
    public void initialize(StartDateConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(Instant startDate, ConstraintValidatorContext constraintValidatorContext) {
        if(Objects.isNull(startDate)){
            return true;
        }
        return Instant.now().isBefore(startDate);
    }
}
