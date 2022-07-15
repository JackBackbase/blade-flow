package com.backbase.flow.sme.onboarding.event;

import com.backbase.flow.casedata.status.CaseEvent;
import java.util.Map;
import java.util.UUID;

public class ApplicantActionNotRequiredEvent extends CaseEvent {

    public static final String APPLICANT_ONBOARDING_NOT_REQUIRED = "Applicant doesn't need to complete onboarding";

    public ApplicantActionNotRequiredEvent(UUID caseKey, Map<String, String> metaData) {
        super(caseKey, APPLICANT_ONBOARDING_NOT_REQUIRED, metaData);
    }
}
