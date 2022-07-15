package com.backbase.flow.sme.onboarding.validation;

import com.backbase.flow.iam.annotation.RunWithoutAuthorization;
import com.backbase.flow.validation.definition.ValidationResult;
import com.backbase.flow.validation.definition.ValidatorDefinition;
import java.util.List;
import java.util.Map;
import java.util.Optional;

abstract class BaseNotNullValidator implements BaseValidator {

    @Override
    @RunWithoutAuthorization
    public List<ValidationResult> validate(ValidatorDefinition validator,
                                           Map<String, Object> params, String fieldName, Object fieldValue) {
        return Optional.ofNullable(fieldValue)
            .map(value -> validateNotNull(validator, params, fieldName, value))
            .orElseGet(this::noError);
    }

    abstract List<ValidationResult> validateNotNull(ValidatorDefinition validator, Map<String, Object> params,
                                                    String fieldName,
                                                    Object fieldValue);
}
