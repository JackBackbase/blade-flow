package com.backbase.flow.onboarding.us.mapper.onboarding;

import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.mapper.JourneyWriter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OnboardingWriter implements JourneyWriter<Onboarding> {

    private final CaseDataService caseDataService;

    @Override
    public Case write(Onboarding journeyData, String subject, String context, Case aCase) {

        aCase.setCaseData(journeyData);

        return caseDataService.updateCase(aCase);
    }

    @Override
    public boolean canApply(String context, Case aCase) {
        return true;
    }
}
