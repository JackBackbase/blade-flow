package com.backbase.flow.onboarding.us.interaction.resume;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class OtpVerificationResumeRuleTest {

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    private final OtpVerificationResumeRule resumeRule = new OtpVerificationResumeRule(objectMapper);

    @ParameterizedTest
    @CsvSource({"false, false, false",
        "true, false, true",
        "false, true, true",
        "true, true, true"})
    void testMainApplicant(boolean phoneNumberVerified, boolean emailVerified, boolean expectedResult) {
        final Map<String, Object> caseData = Map.of("mainApplicant",
            Map.of("phoneNumberVerified", phoneNumberVerified, "emailVerified", emailVerified),
            "isMainApplicantFlow", true);

        boolean result = resumeRule.validate(caseData, null);

        assertThat(result).isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @CsvSource({"false, false, false",
        "true, false, true",
        "false, true, true",
        "true, true, true"})
    void testCoApplicant(boolean phoneNumberVerified, boolean emailVerified, boolean expectedResult) {
        final Map<String, Object> caseData = Map.of("coApplicantId", "not empty id", "coApplicant",
            Map.of("phoneNumberVerified", phoneNumberVerified, "emailVerified", emailVerified),
            "isMainApplicantFlow", false);

        boolean result = resumeRule.validate(caseData, null);

        assertThat(result).isEqualTo(expectedResult);
    }
}
