package com.backbase.flow.sme.onboarding.event;

import static com.backbase.flow.integration.service.event.EventConstants.EVENT_CASE_KEY;

import com.backbase.flow.process.events.NameOnlyEvent;
import java.util.Map;
import java.util.UUID;

public class BaseSmeNameOnlyEvent extends NameOnlyEvent {

    public BaseSmeNameOnlyEvent(String description, UUID caseKey, String eventName) {
        super(description, caseKey, null, eventName,
            getMetaData(caseKey));
    }

    private static Map<String, String> getMetaData(UUID caseKey) {
        return Map.of(EVENT_CASE_KEY, caseKey.toString());
    }
}
