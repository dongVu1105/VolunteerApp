package com.dongVu1105.event_service.validation;

import com.dongVu1105.event_service.dto.request.EventCreationRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FinishDateValidator implements ConstraintValidator<FinishDateConstraint, EventCreationRequest> {

    @Override
    public boolean isValid(EventCreationRequest request, ConstraintValidatorContext context) {
        if (request.getStartDate() == null || request.getFinishDate() == null) {
            return true; // Let @NotNull handle null validation
        }

        return request.getFinishDate().isAfter(request.getStartDate());
    }
}