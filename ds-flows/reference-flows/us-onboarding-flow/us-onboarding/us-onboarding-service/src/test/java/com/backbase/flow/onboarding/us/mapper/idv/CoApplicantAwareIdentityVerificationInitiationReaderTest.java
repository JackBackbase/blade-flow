package com.backbase.flow.onboarding.us.mapper.idv;

import static org.assertj.core.api.Assertions.assertThat;

import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.service.dto.IdentityVerificationContext;
import java.util.Map;
import org.junit.jupiter.api.Test;

class CoApplicantAwareIdentityVerificationInitiationReaderTest {

    private static final String TEST = "test";
    private static final String MAIN_APPLICANT = "mainApplicant";
    private static final String CONTEXT = IdentityVerificationContext.INITIATION;

    private final CoApplicantAwareIdentityVerificationInitiationReader reader = new CoApplicantAwareIdentityVerificationInitiationReader();

    @Test
    void read() {
        Case aCase = new Case();
        aCase.setCaseData(
            Map.of(MAIN_APPLICANT,
                Map.of(CONTEXT,
                    Map.of(TEST, TEST)),
                "isMainApplicantFlow", true));
        Map<String, Object> readResult = reader.read(null, CONTEXT, aCase);
        assertThat(readResult)
            .isNotNull()
            .containsEntry(TEST, TEST);
    }

    @Test
    void read_empty() {
        Case aCase = new Case();
        aCase.setCaseData(
            Map.of(MAIN_APPLICANT,
                Map.of(),
                "isMainApplicantFlow", true));
        Map<String, Object> readResult = reader.read(null, CONTEXT, aCase);
        assertThat(readResult)
            .isNotNull()
            .isEmpty();
    }
}