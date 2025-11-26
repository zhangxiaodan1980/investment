package com.gov.investment.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AllowedStatusValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedStatus {
    String message() default "Invalid status value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String[] allowedValues() default {"Active", "Inactive"};
}
