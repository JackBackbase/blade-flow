package com.backbase.flow.onboarding.us.validation.validator;

import static java.util.stream.Collectors.toSet;

import com.backbase.flow.onboarding.us.validation.annotation.ValidEnum;
import java.util.Set;
import java.util.stream.Stream;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidEnumValidator implements ConstraintValidator<ValidEnum, String> {

    private Set<String> validEnumValues;

    @Override
    public void initialize(ValidEnum validEnum) {
        final Class<? extends Enum> selectedEnum = validEnum.enumClass();
        final Enum<?>[] enums = selectedEnum.getEnumConstants();

        validEnumValues = Stream.of(enums)
            .map(Enum::name)
            .collect(toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || validEnumValues.contains(value);
    }

}