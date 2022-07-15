package com.backbase.flow.sme.onboarding.event;

import com.backbase.flow.casedata.status.CaseEvent;
import java.util.Map;
import java.util.UUID;

public class ApplicantOnboardingFinishedEvent extends CaseEvent {

    public static final String APPLICANT_ONBOARDING_FINISHED = "Applicant onboarding finished.";

    public ApplicantOnboardingFinishedEvent(UUID caseKey, Map<String, String> metaData) {
        super(caseKey, APPLICANT_ONBOARDING_FINISHED, metaData);
    }
}
