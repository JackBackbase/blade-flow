package com.backbase.flow.onboarding.us.mapper.onboarding;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.onboarding.us.mapper.MapperTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OnboardingWriterTest {

    private final CaseDataService caseDataService = mock(CaseDataService.class);
    private final OnboardingWriter writer = new OnboardingWriter(caseDataService);

    @BeforeEach
    void setup() {
        when(caseDataService.updateCase(any())).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void write_noMainApplicantCompletionId_writesToRoot() {
        final Case sampleCase = MapperTestData.sampleCaseNoMainApplicantCompletionId();
        final Onboarding onboarding = sampleCase.getCaseData(Onboarding.class);
        onboarding.getMainApplicant().setLastName("Main applicant last name");

        final Case updatedCase = writer.write(onboarding, null, null, sampleCase);
        final Onboarding updatedCaseData = updatedCase.getCaseData(Onboarding.class);

        assertThat(updatedCaseData.getMainApplicant().getFirstName()).isEqualTo("main-applicant");
        assertThat(updatedCaseData.getMainApplicant().getLastName()).isEqualTo("Main applicant last name");
        assertThat(updatedCaseData.getCoApplicant()).isNull();
    }

    @Test
    void write_hasMainApplicantCompletionId_writesToCoApplicant() {
        final Case sampleCase = MapperTestData.sampleCaseWithMainApplicantCompletionId();
        final Onboarding onboarding = sampleCase.getCaseData(Onboarding.class);
        onboarding.getCoApplicant().setLastName("Co applicant last name");

        final Case updatedCase = writer.write(onboarding, null, null, sampleCase);
        final Onboarding updatedCaseData = updatedCase.getCaseData(Onboarding.class);

        assertThat(updatedCaseData.getCoApplicant().getFirstName()).isEqualTo("co-applicant");
        assertThat(updatedCaseData.getCoApplicant().getLastName()).isEqualTo("Co applicant last name");
    }
}
