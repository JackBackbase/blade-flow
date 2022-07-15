package com.backbase.flow.sme.onboarding.event;

import java.util.UUID;
import lombok.Getter;

@Getter
public class AllDocsVerifiedEvent extends BaseSmeNameOnlyEvent {

    public static final String ALL_DOCS_VERIFIED_EVENT_NAME = "AllDocsVerified";

    private static final String BASE_DESCRIPTION = "Document requests have been verified";

    public AllDocsVerifiedEvent(UUID caseKey) {
        super(BASE_DESCRIPTION, caseKey, ALL_DOCS_VERIFIED_EVENT_NAME);
    }
}
