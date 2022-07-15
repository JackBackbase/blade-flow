package com.backbase.flow.sme.onboarding.event;

import com.backbase.flow.casedata.status.CaseEvent;
import java.util.Map;
import java.util.UUID;

public class ApplicantOnboardingPendingEvent extends CaseEvent {

    public static final String APPLICANT_ONBOARDING_PENDING = "Applicant onboarding pending.";

    public ApplicantOnboardingPendingEvent(UUID caseKey, Map<String, String> metaData) {
        super(caseKey, APPLICANT_ONBOARDING_PENDING, metaData);
    }
}
