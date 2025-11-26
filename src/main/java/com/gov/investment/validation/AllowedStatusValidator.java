package com.gov.investment.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class AllowedStatusValidator implements ConstraintValidator<AllowedStatus, String> {
    private List<String> allowedStatuses;

    @Override
    public void initialize(AllowedStatus constraintAnnotation) {
        allowedStatuses = Arrays.asList(constraintAnnotation.allowedValues());
    }

    @Override
    public boolean isValid(String status, ConstraintValidatorContext context) {
        if (status == null) {
            return false;
        }
        return allowedStatuses.contains(status);
    }
}
