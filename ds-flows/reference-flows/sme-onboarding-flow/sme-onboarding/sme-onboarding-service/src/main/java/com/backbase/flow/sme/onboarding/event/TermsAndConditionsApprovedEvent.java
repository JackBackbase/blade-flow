package com.backbase.flow.sme.onboarding.event;

import com.backbase.flow.casedata.status.CaseEvent;
import java.util.Collections;
import java.util.UUID;

public class TermsAndConditionsApprovedEvent extends CaseEvent {

    public TermsAndConditionsApprovedEvent(UUID caseKey) {
        super(caseKey, "Term and conditions are accepted", Collections.emptyMap());
    }
}
