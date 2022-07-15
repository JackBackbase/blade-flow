package com.backbase.flow.sme.onboarding.event;

import java.util.UUID;

public class ApplicationSubmittedEvent extends BaseSmeNameOnlyEvent {

    public static final String APPLICATION_SUBMITTED_EVENT_NAME = "ApplicationSubmitted";

    private static final String BASE_DESCRIPTION = "Application has been submitted";

    public ApplicationSubmittedEvent(UUID caseKey) {
        super(BASE_DESCRIPTION, caseKey, APPLICATION_SUBMITTED_EVENT_NAME);
    }
}
