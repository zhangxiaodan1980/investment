package com.gov.investment.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class AllowedStageValidator implements ConstraintValidator<AllowedStage, String> {
    private List<String> allowedStages;

    @Override
    public void initialize(AllowedStage constraintAnnotation) {
        allowedStages = Arrays.asList(constraintAnnotation.allowedValues());
    }

    @Override
    public boolean isValid(String stage, ConstraintValidatorContext context) {
        if (stage == null) {
            return false;
        }
        return allowedStages.contains(stage);
    }
}
