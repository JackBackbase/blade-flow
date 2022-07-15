package com.backbase.flow.onboarding.us.mapper.idv;

import static com.backbase.flow.onboarding.us.mapper.idv.CoApplicantAwareIdentityVerificationResultReader.FIRST_NAME;
import static com.backbase.flow.onboarding.us.mapper.idv.CoApplicantAwareIdentityVerificationResultReader.LAST_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.service.dto.IdentityVerificationContext;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class CoApplicantAwareIdentityVerificationResultReaderTest {

    private static final String TEST = "test";
    private static final String MAIN_APPLICANT = "mainApplicant";
    private static final String CONTEXT = IdentityVerificationContext.RESULT;
    private static Map<String, Object> FILESET_NAME_SUFFIX_MAP = Map.of("filesetNameSuffix", "John Doe");

    private final CoApplicantAwareIdentityVerificationResultReader reader = new CoApplicantAwareIdentityVerificationResultReader();

    @Test
    void read() {
        Map<String, Object> testData  = Map.of(TEST, TEST);
        Case aCase = createCase(testData);
        Map<String, Object> readResult = reader.read(null, CONTEXT, aCase);
        assertThat(readResult)
            .hasSize(2)
            .containsAllEntriesOf(testData)
            .containsAllEntriesOf(FILESET_NAME_SUFFIX_MAP);
    }

    @Test
    void read_empty() {
        Case aCase = createCase(Map.of());
        Map<String, Object> readResult = reader.read(null, CONTEXT, aCase);
        assertThat(readResult)
            .containsExactlyEntriesOf(FILESET_NAME_SUFFIX_MAP);
    }

    private Case createCase(Map<String, Object> additionalData) {
        Map<String, Object> applicantData = new HashMap<>();
        applicantData.put(FIRST_NAME, "John");
        applicantData.put(LAST_NAME, "Doe");
        applicantData.put(CONTEXT, additionalData);
        return new Case().setCaseData(Map.of(MAIN_APPLICANT, applicantData, "isMainApplicantFlow", true));
    }
}