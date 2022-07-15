package com.backbase.flow.onboarding.us.mapper.otp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.onboarding.us.mapper.MapperTestData;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoApplicantAwareOtpWriterTest {

    private final CaseDataService caseDataService = mock(CaseDataService.class);
    private final CoApplicantAwareOtpWriter writer = new CoApplicantAwareOtpWriter(caseDataService);

    @BeforeEach
    void setup() {
        when(caseDataService.updateCase(any())).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void write_noMainApplicantCompletionId_writesToRoot() {
        final Case sampleCase = MapperTestData.sampleCaseNoMainApplicantCompletionId();
        final Map<String, Object> journeyData = Map.of("phoneNumberVerified", true, "phoneNumber", "123");

        final Case updatedCase = writer.write(journeyData, null, null, sampleCase);
        final Onboarding updatedCaseData = updatedCase.getCaseData(Onboarding.class);

        assertThat(updatedCaseData.getMainApplicant().getFirstName()).isEqualTo("main-applicant");
        assertThat(updatedCaseData.getMainApplicant().getPhoneNumberVerified()).isTrue();
        assertThat(updatedCaseData.getMainApplicant().getPhoneNumber()).isEqualTo("123");
        assertThat(updatedCaseData.getCoApplicant()).isNull();
    }

    @Test
    void write_hasMainApplicantCompletionId_writesToCoApplicant() {
        final Case sampleCase = MapperTestData.sampleCaseWithMainApplicantCompletionId();
        final Map<String, Object> journeyData = Map.of("phoneNumberVerified", true, "phoneNumber", "123");

        final Case updatedCase = writer.write(journeyData, null, null, sampleCase);
        final Onboarding updatedCaseData = updatedCase.getCaseData(Onboarding.class);

        assertThat(updatedCaseData.getCoApplicant().getFirstName()).isEqualTo("co-applicant");
        assertThat(updatedCaseData.getCoApplicant().getPhoneNumberVerified()).isTrue();
        assertThat(updatedCaseData.getCoApplicant().getPhoneNumber()).isEqualTo("123");
        assertThat(updatedCaseData.getMainApplicant().getPhoneNumber()).isNull();
    }
}
