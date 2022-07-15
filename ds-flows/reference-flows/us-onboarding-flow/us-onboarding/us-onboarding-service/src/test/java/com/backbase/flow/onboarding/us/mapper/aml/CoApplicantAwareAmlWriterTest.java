package com.backbase.flow.onboarding.us.mapper.aml;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.onboarding.us.mapper.MapperTestData;
import com.backbase.flow.service.aml.casedata.AntiMoneyLaunderingInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoApplicantAwareAmlWriterTest {

    private final CaseDataService caseDataService = mock(CaseDataService.class);
    private final CoApplicantAwareAmlWriter writer = new CoApplicantAwareAmlWriter(caseDataService);

    @BeforeEach
    void setup() {
        when(caseDataService.updateCase(any())).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void write_noMainApplicantCompletionId_writesToMainApplicant() {
        final AntiMoneyLaunderingInfo journeyData = new AntiMoneyLaunderingInfo()
            .withReviewDeclinedComment("Some comment");
        final Case sampleCase = MapperTestData.sampleCaseNoMainApplicantCompletionId();

        final Case updatedCase = writer.write(journeyData, null, null, sampleCase);
        final Onboarding updatedCaseData = updatedCase.getCaseData(Onboarding.class);

        assertThat(updatedCaseData.getCoApplicant()).isNull();
        assertThat(updatedCaseData.getMainApplicant().getAntiMoneyLaunderingInfo().getReviewDeclinedComment()).isEqualTo("Some comment");
    }

    @Test
    void write_hasMainApplicantCompletionId_writesToCoApplicant() {
        final AntiMoneyLaunderingInfo journeyData = new AntiMoneyLaunderingInfo()
            .withReviewDeclinedComment("Some comment");
        final Case sampleCase = MapperTestData.sampleCaseWithMainApplicantCompletionId();

        final Case updatedCase = writer.write(journeyData, null, null, sampleCase);
        final Onboarding updatedCaseData = updatedCase.getCaseData(Onboarding.class);

        assertThat(updatedCaseData.getMainApplicant().getAntiMoneyLaunderingInfo()).isNull();
        assertThat(updatedCaseData.getCoApplicant().getAntiMoneyLaunderingInfo().getReviewDeclinedComment())
            .isEqualTo("Some comment");
    }
}
