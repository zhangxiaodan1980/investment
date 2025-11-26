package com.gov.investment.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AllowedStageValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedStage {
    String message() default "Invalid stage value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String[] allowedValues() default {"Prospect", "Qualified", "Proposal", "Negotiation", "Closed Won", "Closed Lost"};
}
