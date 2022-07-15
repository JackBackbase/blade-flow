package com.backbase.flow.onboarding.us.interaction.handler.event;

import com.backbase.flow.casedata.status.CaseEvent;

import java.util.Map;
import java.util.UUID;

public class CustomerNotEligibleEvent extends CaseEvent {

    public CustomerNotEligibleEvent(UUID caseKey, String description, Map<String, String> metaData) {
        super(caseKey, description, metaData);
    }
}