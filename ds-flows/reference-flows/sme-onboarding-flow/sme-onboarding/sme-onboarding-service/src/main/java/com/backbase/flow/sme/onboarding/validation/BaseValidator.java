package com.backbase.flow.sme.onboarding.validation;

import com.backbase.flow.validation.definition.ValidationResult;
import com.backbase.flow.validation.definition.ValidationResultCollection;
import com.backbase.flow.validation.definition.ValidatorDefinition.ValidationError;
import com.backbase.flow.validation.validators.Validator;
import java.util.List;

interface BaseValidator extends Validator {

    default List<ValidationResult> noError() {
        return ValidationResultCollection.POSITIVE_RESULT_LIST;
    }

    default List<ValidationResult> error(ValidationError error, String fieldName) {
        return List.of(new ValidationResult(false, fieldName, error));
    }
}
