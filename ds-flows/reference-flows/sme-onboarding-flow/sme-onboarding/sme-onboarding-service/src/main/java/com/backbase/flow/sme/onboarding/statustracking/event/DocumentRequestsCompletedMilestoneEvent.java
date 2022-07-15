package com.backbase.flow.sme.onboarding.statustracking.event;

import com.backbase.flow.casedata.status.CaseEvent;
import java.util.Map;
import java.util.UUID;

public class DocumentRequestsCompletedMilestoneEvent extends CaseEvent {

    private static final String BASE_DESCRIPTION = "Application completed has been checked";

    public DocumentRequestsCompletedMilestoneEvent(UUID caseKey, Map<String, String> metaData) {
        super(caseKey, BASE_DESCRIPTION, metaData);
    }
}
