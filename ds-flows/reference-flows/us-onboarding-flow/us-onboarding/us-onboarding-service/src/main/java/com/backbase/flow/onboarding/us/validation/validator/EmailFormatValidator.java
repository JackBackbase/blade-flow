package com.backbase.flow.onboarding.us.validation.validator;

import com.backbase.flow.onboarding.us.validation.ValidationConstants;
import com.backbase.flow.onboarding.us.validation.annotation.EmailFormatIsValid;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.validator.routines.DomainValidator;

public class EmailFormatValidator implements ConstraintValidator<EmailFormatIsValid, String> {

    /**
     * <p>Uses the {@link ValidationConstants} to evaluate the validity of the annotated field.</p>
     * @param value the value to be validated.
     * @param context the context of the field which will be validated.
     * @return a {@code boolean} that determines if the field is valid.
     */
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        String[] tokensInEmail = value.split("\\.");
        return Pattern.compile(ValidationConstants.EMAIL_REGEX_PATTERN).matcher(value).matches()
            && DomainValidator.getInstance().isValidTld(tokensInEmail[tokensInEmail.length - 1]);
    }
}