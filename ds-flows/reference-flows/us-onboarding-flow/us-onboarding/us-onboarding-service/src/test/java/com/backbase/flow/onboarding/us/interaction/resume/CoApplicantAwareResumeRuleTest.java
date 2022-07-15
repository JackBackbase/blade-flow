package com.backbase.flow.onboarding.us.interaction.resume;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CoApplicantAwareResumeRuleTest {

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    private final CoApplicantAwareResumeRule resumeRule = new CoApplicantAwareResumeRule(objectMapper);

    @ParameterizedTest
    @CsvSource({"false, true, false",
        "true, false, false",
        "false, false, false",
        "true, true, true"})
    void validate(boolean currentIsMainApplicant, boolean previousIsMainApplicant, boolean expectedResult) {
        final Map<String, Object> currentCaseData = Map.of("isMainApplicantFlow", currentIsMainApplicant);
        final Map<String, Object> previousCaseData = Map.of("isMainApplicantFlow", previousIsMainApplicant);

        boolean result = resumeRule.validate(currentCaseData, previousCaseData);

        assertThat(result).isEqualTo(expectedResult);
    }
}
