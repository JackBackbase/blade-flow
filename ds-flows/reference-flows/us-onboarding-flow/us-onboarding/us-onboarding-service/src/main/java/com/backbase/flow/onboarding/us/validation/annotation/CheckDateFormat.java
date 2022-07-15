package com.backbase.flow.onboarding.us.validation.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.backbase.flow.onboarding.us.validation.validator.CheckDateFormatValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = CheckDateFormatValidator.class)
@Documented
public @interface CheckDateFormat {

    /**
     * Default constraint message.
     */
    String message() default "Invalid date (format). Date should be in format yyyy-MM-dd";

    /**
     * Allows the specification of validation groups, to which this constraint belongs.
     */
    Class<?>[] groups() default {};

    /**
     * The attribute can be used by clients of the Bean Validation API to assign custom payload objects to a
     * constraint.
     */
    Class<? extends Payload>[] payload() default {};

}