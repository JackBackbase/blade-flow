package com.backbase.flow.onboarding.us.process.status.event;

import com.backbase.flow.casedata.status.CaseEvent;

import java.util.Map;
import java.util.UUID;

public class CaseClosedEvent extends CaseEvent {

    public CaseClosedEvent(UUID caseKey, String description, Map<String, String> metaData) {
        super(caseKey, description, metaData);
    }
}
