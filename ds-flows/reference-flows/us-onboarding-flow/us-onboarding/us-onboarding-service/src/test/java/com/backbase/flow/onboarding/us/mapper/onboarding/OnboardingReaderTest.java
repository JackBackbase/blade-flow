package com.backbase.flow.onboarding.us.mapper.onboarding;

import static org.assertj.core.api.Assertions.assertThat;

import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.onboarding.us.mapper.MapperTestData;
import org.junit.jupiter.api.Test;

class OnboardingReaderTest {

    private final OnboardingReader reader = new OnboardingReader();

    @Test
    void read_noMainApplicantCompletionId_returnsOnboardingWithMainApplicant() {
        final Case sampleCase = MapperTestData.sampleCaseNoMainApplicantCompletionId();

        final Onboarding result = reader.read(null, null, sampleCase);

        assertThat(result.getMainApplicant()).isEqualTo(sampleCase.getCaseData(Onboarding.class).getMainApplicant());
    }

    @Test
    void read_hasMainApplicantCompletionId_returnsOnboardingWithCoApplicant() {
        final Case sampleCase = MapperTestData.sampleCaseWithMainApplicantCompletionId();

        final Onboarding result = reader.read(null, null, sampleCase);

        assertThat(result.getCoApplicant()).isEqualTo(sampleCase.getCaseData(Onboarding.class).getCoApplicant());
    }
}
