package com.backbase.flow.onboarding.us.mapper.applicant;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.mapper.JourneyWriter;
import com.backbase.flow.onboarding.us.util.OnboardingCaseDataUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CoApplicantAwareApplicantWriter implements JourneyWriter<Applicant> {

    private final CaseDataService caseDataService;

    @Override
    public Case write(Applicant journeyData, String subject, String context, Case aCase) {
        final Onboarding caseData = aCase.getCaseData(Onboarding.class);

        if (OnboardingCaseDataUtils.isMainApplicantFlow(caseData)) {
            caseData.setMainApplicant(journeyData);
        } else {
            caseData.setCoApplicant(journeyData);
        }
        aCase.setCaseData(caseData);

        return caseDataService.updateCase(aCase);
    }

    @Override
    public boolean canApply(String context, Case aCase) {
        return true;
    }
}
