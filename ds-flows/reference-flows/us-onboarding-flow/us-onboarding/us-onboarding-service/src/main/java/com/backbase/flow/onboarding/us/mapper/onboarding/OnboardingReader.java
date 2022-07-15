package com.backbase.flow.onboarding.us.mapper.onboarding;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.mapper.JourneyReader;

public class OnboardingReader implements JourneyReader<Onboarding> {

    @Override
    public Onboarding read(String subject, String context, Case aCase) {
        final Onboarding onboarding = aCase.getCaseData(Onboarding.class);
        if (onboarding.getMainApplicant() == null) {
            onboarding.setMainApplicant(new Applicant());
        }

        return onboarding;
    }

    @Override
    public boolean canApply(String context, Case aCase) {
        return true;
    }
}
