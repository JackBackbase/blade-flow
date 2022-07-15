package com.backbase.flow.sme.onboarding.validation;

import com.backbase.flow.sme.onboarding.BaseIntegrationIT;
import com.backbase.flow.sme.onboarding.interaction.model.AnchorRequestDto;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AgeValidatorIT extends BaseIntegrationIT {

    private static final LocalDate TODAY = LocalDate.now();
    private static final String FIELD_NAME = "dateOfBirth";
    private static final String ERROR_KEY = "FLOW-AGE-INVALID";
    private static final String ERROR_MESSAGE = "Customer's age should be between 18 and 100 years old.";

    @Test
    void validate_validAge_noError() {
        var payload = payloadWithDateOfBirth(TODAY.minusYears(40));
        checkNoValidationError(payload, FIELD_NAME);
    }

    @ParameterizedTest
    @ValueSource(longs = {0, 120})
    void validate_invalidAge_error(long age) {
        var payload = payloadWithDateOfBirth(TODAY.minusYears(age));
        checkValidationError(payload, FIELD_NAME, ERROR_KEY, ERROR_MESSAGE);
    }

    private AnchorRequestDto payloadWithDateOfBirth(LocalDate dateOfBirth) {
        return AnchorRequestDto.builder()
            .dateOfBirth(dateOfBirth)
            .build();
    }
}
