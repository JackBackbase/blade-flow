package com.backbase.flow.sme.onboarding.validation;

import com.backbase.flow.process.service.FlowDecisionService;
import com.backbase.flow.validation.definition.ValidationResult;
import com.backbase.flow.validation.definition.ValidatorDefinition;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("age")
class AgeValidator extends BaseNotNullValidator {

    private final FlowDecisionService flowDecisionService;

    @Override
    List<ValidationResult> validateNotNull(ValidatorDefinition validator,
                                           Map<String, Object> params,
                                           String fieldName,
                                           Object fieldValue) {
        if (fieldValue instanceof LocalDate) {
            var dateOfBirth = (LocalDate) fieldValue;
            if (ageVerified(dateOfBirth)) {
                return noError();
            }
        }
        return error(validator.getError(), fieldName);
    }

    private boolean ageVerified(LocalDate dateOfBirth) {
        var age = Period.between(dateOfBirth, LocalDate.now()).getYears();
        var decisions = flowDecisionService.checkDecisionTable(null, "age-verification", null, Map.of("age", age));
        return (boolean) decisions.get(0).get("age-verified");
    }
}
