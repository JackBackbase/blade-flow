package com.backbase.flow.onboarding.us.mapper.applicant;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.mapper.JourneyReader;
import com.backbase.flow.onboarding.us.util.OnboardingCaseDataUtils;
import org.apache.commons.lang3.ObjectUtils;

public class CoApplicantAwareApplicantReader implements JourneyReader<Applicant> {

    @Override
    public Applicant read(String subject, String context, Case aCase) {
        final Onboarding onboarding = aCase.getCaseData(Onboarding.class);

        return OnboardingCaseDataUtils.isMainApplicantFlow(onboarding)
            ? ObjectUtils.defaultIfNull(onboarding.getMainApplicant(), new Applicant())
            : onboarding.getCoApplicant();
    }

    @Override
    public boolean canApply(String context, Case aCase) {
        return true;
    }
}
