package com.backbase.flow.sme.onboarding.event.factory;

import static com.backbase.flow.sme.onboarding.event.AllDocsVerifiedEvent.ALL_DOCS_VERIFIED_EVENT_NAME;
import static com.backbase.flow.sme.onboarding.event.ApplicationCompleteEvent.APPLICATION_COMPLETE_EVENT_NAME;
import static com.backbase.flow.sme.onboarding.event.NoDocsRequiredEvent.NO_DOCS_REQUIRED_EVENT_NAME;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class SmeDocumentRequestNameOnlyEventFactoryTest {

    private final UUID caseKey = randomUUID();
    private final UUID processInstanceId = randomUUID();
    private final SmeDocumentRequestNameOnlyEventFactory smeDocumentRequestNameOnlyEventFactory =
        new SmeDocumentRequestNameOnlyEventFactory();
    private String eventName;

    @Test
    void create_allDocsVerifiedEvent_returnEvent() {

        eventName = ALL_DOCS_VERIFIED_EVENT_NAME;

        var allDocsVerifiedEvent = smeDocumentRequestNameOnlyEventFactory
            .create(caseKey, processInstanceId.toString(), eventName);

        assertEquals(eventName, allDocsVerifiedEvent.getEventName());
    }

    @Test
    void create_applicationCompleteEvent_returnEvent() {

        eventName = APPLICATION_COMPLETE_EVENT_NAME;

        var applicationCompleteEvent = smeDocumentRequestNameOnlyEventFactory
            .create(caseKey, processInstanceId.toString(), eventName);

        assertEquals(eventName, applicationCompleteEvent.getEventName());
    }

    @Test
    void create_noDocsRequiredEvent_returnEvent() {

        eventName = NO_DOCS_REQUIRED_EVENT_NAME;

        var noDocsRequiredEvent = smeDocumentRequestNameOnlyEventFactory
            .create(caseKey, processInstanceId.toString(), eventName);

        assertEquals(eventName, noDocsRequiredEvent.getEventName());
    }

    @Test
    void create_defaultNameOnlyEvent_returnEvent() {

        eventName = "NotSpecified";

        var defaultNameOnlyEvent = smeDocumentRequestNameOnlyEventFactory
            .create(caseKey, processInstanceId.toString(), eventName);

        assertEquals(eventName, defaultNameOnlyEvent.getEventName());
    }
}
