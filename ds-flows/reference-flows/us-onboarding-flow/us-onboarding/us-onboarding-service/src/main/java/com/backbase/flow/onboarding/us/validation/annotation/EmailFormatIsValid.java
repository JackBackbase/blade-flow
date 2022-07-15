package com.backbase.flow.onboarding.us.validation.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.backbase.flow.onboarding.us.validation.validator.EmailFormatValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = EmailFormatValidator.class)
@Documented
public @interface EmailFormatIsValid {

    /**
     * Default constraint message.
     */
    String message() default "Check your input.";

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