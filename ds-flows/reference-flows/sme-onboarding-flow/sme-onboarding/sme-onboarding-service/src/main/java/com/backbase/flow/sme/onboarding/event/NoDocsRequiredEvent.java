package com.backbase.flow.sme.onboarding.event;

import java.util.UUID;

public class NoDocsRequiredEvent extends BaseSmeNameOnlyEvent {

    public static final String NO_DOCS_REQUIRED_EVENT_NAME = "NoDocsRequired";

    private static final String BASE_DESCRIPTION = "No documents required";

    public NoDocsRequiredEvent(UUID caseKey) {
        super(BASE_DESCRIPTION, caseKey, NO_DOCS_REQUIRED_EVENT_NAME);
    }
}
