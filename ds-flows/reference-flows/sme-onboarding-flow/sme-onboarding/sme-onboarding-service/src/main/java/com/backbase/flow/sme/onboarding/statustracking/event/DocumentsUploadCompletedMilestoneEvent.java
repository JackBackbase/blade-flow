package com.backbase.flow.sme.onboarding.statustracking.event;

import com.backbase.flow.casedata.status.CaseEvent;
import java.util.Map;
import java.util.UUID;

public class DocumentsUploadCompletedMilestoneEvent extends CaseEvent {

    private static final String BASE_DESCRIPTION = "Documents Uploaded has been checked";

    public DocumentsUploadCompletedMilestoneEvent(UUID caseKey, Map<String, String> metaData) {
        super(caseKey, BASE_DESCRIPTION, metaData);
    }
}
