package com.backbase.flow.onboarding.us.validation.validator;

import com.backbase.flow.onboarding.us.validation.annotation.CheckDateFormat;
import java.time.LocalDate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckDateFormatValidator implements ConstraintValidator<CheckDateFormat, String> {

    /**
     * <p>Uses the {@link LocalDate#parse(CharSequence)} method to evaluate the validity of the annotated field.</p>
     * @param value the value to be validated.
     * @param context the context of the field which will be validated.
     * @return a {@code boolean} that determines if the field is valid.
     */
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            LocalDate.parse(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}