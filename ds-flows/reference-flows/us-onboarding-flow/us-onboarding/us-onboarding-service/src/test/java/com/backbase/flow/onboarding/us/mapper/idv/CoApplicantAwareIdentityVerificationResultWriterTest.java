package com.backbase.flow.onboarding.us.mapper.idv;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.service.dto.IdentityVerificationContext;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CoApplicantAwareIdentityVerificationResultWriterTest {

    private static final String TEST = "test";
    private static final String CONTEXT = IdentityVerificationContext.RESULT;

    @Mock
    private CaseDataService caseDataService;
    @InjectMocks
    private CoApplicantAwareIdentityVerificationResultWriter writer;

    @Test
    void write() {
        Case aCase = new Case();
        aCase.setCaseData(new Onboarding()
            .withMainApplicant(new Applicant()));

        when(caseDataService.updateCase(any(Case.class))).thenAnswer(e -> e.getArguments()[0]);
        Case updatedCase = writer.write(Map.of(TEST, TEST), null, CONTEXT, aCase);
        Map<String, Object> mainApplicantCaseData = (Map<String, Object>) updatedCase.getCaseData(Map.class)
            .get("mainApplicant");
        Map<String, Object> mainApplicantIdvCaseData = (Map<String, Object>) mainApplicantCaseData.get(CONTEXT);

        assertThat(mainApplicantIdvCaseData).containsEntry(TEST, TEST);
    }

    @Test
    void write_coApplicant() {
        Case aCase = new Case();
        Onboarding onboarding = new Onboarding()
            .withCoApplicant(new Applicant())
            .withCoApplicantId(UUID.randomUUID().toString());
        aCase.setCaseData(onboarding);
        when(caseDataService.updateCase(any(Case.class))).thenAnswer(e -> e.getArguments()[0]);

        Case updatedCase = writer.write(Map.of(TEST, TEST), null, CONTEXT, aCase);
        Map<String, Object> coApplicantCaseData = (Map<String, Object>) updatedCase.getCaseData(Map.class)
            .get("coApplicant");
        Map<String, Object> coApplicantIdvCaseData = (Map<String, Object>) coApplicantCaseData.get(CONTEXT);

        assertThat(coApplicantIdvCaseData).containsEntry(TEST, TEST);
    }
}