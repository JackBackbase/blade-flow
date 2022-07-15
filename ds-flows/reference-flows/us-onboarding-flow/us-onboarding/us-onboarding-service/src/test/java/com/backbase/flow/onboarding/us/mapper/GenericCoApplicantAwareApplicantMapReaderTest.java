package com.backbase.flow.onboarding.us.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.cases.Case;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;

class GenericCoApplicantAwareApplicantMapReaderTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules();

    @Test
    void read_noMainApplicantCompletionId_returnsMainApplicant() {
        final Case sampleCase = MapperTestData.sampleCaseNoMainApplicantCompletionId();

        final Map<String, Object> applicant = GenericCoApplicantAwareApplicantMapReader.read(sampleCase);
        final Applicant resultApplicant = OBJECT_MAPPER.convertValue(applicant, Applicant.class);

        assertThat(resultApplicant).isEqualTo(sampleCase.getCaseData(Onboarding.class).getMainApplicant());
    }

    @Test
    void read_hasMainApplicantCompletionId_returnsCoApplicant() {
        final Case sampleCase = MapperTestData.sampleCaseWithMainApplicantCompletionId();

        final Map<String, Object> applicant = GenericCoApplicantAwareApplicantMapReader.read(sampleCase);
        final Applicant resultApplicant = OBJECT_MAPPER.convertValue(applicant, Applicant.class);

        assertThat(resultApplicant).isEqualTo(sampleCase.getCaseData(Onboarding.class).getCoApplicant());
    }
}
