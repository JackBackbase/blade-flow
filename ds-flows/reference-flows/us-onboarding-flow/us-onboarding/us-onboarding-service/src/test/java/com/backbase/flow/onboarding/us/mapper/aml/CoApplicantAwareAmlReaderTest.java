package com.backbase.flow.onboarding.us.mapper.aml;

import static org.assertj.core.api.Assertions.assertThat;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.application.uso.casedata.OnboardingAntiMoneyLaunderingInfo;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.onboarding.us.mapper.MapperTestData;
import com.backbase.flow.service.aml.casedata.AntiMoneyLaunderingInfo;
import org.junit.jupiter.api.Test;

class CoApplicantAwareAmlReaderTest {

    private final CoApplicantAwareAmlReader reader = new CoApplicantAwareAmlReader();

    @Test
    void read_noMainApplicantCompletionId_returnsMainApplicant() {
        final Case sampleCase = MapperTestData.sampleCaseNoMainApplicantCompletionId();
        final OnboardingAntiMoneyLaunderingInfo amlInfo = new OnboardingAntiMoneyLaunderingInfo()
            .withReviewDeclinedComment("A comment");
        final Onboarding caseData = sampleCase.getCaseData(Onboarding.class);
        final Applicant applicant = new Applicant();
        applicant.setAntiMoneyLaunderingInfo(amlInfo);
        caseData.setMainApplicant(applicant);
        sampleCase.setCaseData(caseData);

        final AntiMoneyLaunderingInfo result = reader.read(null, null, sampleCase);

        assertThat(result.getReviewDeclinedComment()).isEqualTo("A comment");
    }

    @Test
    void read_hasMainApplicantCompletionId_returnsCoApplicant() {
        final Case sampleCase = MapperTestData.sampleCaseWithMainApplicantCompletionId();
        final OnboardingAntiMoneyLaunderingInfo amlInfo = new OnboardingAntiMoneyLaunderingInfo()
            .withReviewDeclinedComment("A comment");
        final Onboarding caseData = sampleCase.getCaseData(Onboarding.class);
        final Applicant coApplicant = new Applicant();
        coApplicant.setAntiMoneyLaunderingInfo(amlInfo);
        caseData.setCoApplicant(coApplicant);
        sampleCase.setCaseData(caseData);

        final AntiMoneyLaunderingInfo result = reader.read(null, null, sampleCase);

        assertThat(result.getReviewDeclinedComment()).isEqualTo("A comment");
    }
}
