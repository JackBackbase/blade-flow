package com.backbase.flow.sme.onboarding.event;

import java.util.UUID;

public class ApplicationCompleteEvent extends BaseSmeNameOnlyEvent {

    public static final String APPLICATION_COMPLETE_EVENT_NAME = "Complete";

    private static final String BASE_DESCRIPTION = "Application completed ";

    public ApplicationCompleteEvent(UUID caseKey) {
        super(BASE_DESCRIPTION, caseKey, APPLICATION_COMPLETE_EVENT_NAME);
    }
}
