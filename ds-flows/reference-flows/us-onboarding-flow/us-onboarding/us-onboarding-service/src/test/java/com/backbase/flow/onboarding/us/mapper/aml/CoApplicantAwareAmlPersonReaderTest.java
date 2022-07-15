package com.backbase.flow.onboarding.us.mapper.aml;

import static org.assertj.core.api.Assertions.assertThat;

import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.onboarding.us.mapper.MapperTestData;
import com.backbase.flow.service.aml.casedata.AmlPersonApplicant;
import org.junit.jupiter.api.Test;

class CoApplicantAwareAmlPersonReaderTest {

    private final CoApplicantAwareAmlPersonReader reader = new CoApplicantAwareAmlPersonReader();

    @Test
    void read_noMainApplicantCompletionId_returnsMainApplicant() {
        final Case sampleCase = MapperTestData.sampleCaseNoMainApplicantCompletionId();

        final AmlPersonApplicant result = reader.read(null, null, sampleCase);

        assertThat(result.getFullName()).isEqualTo("main-applicant main-applicant-last-name");
    }

    @Test
    void read_hasMainApplicantCompletionId_returnsCoApplicant() {
        final Case sampleCase = MapperTestData.sampleCaseWithMainApplicantCompletionId();

        final AmlPersonApplicant result = reader.read(null, null, sampleCase);

        assertThat(result.getFullName()).isEqualTo("co-applicant co-applicant-last-name");
    }
}
