package com.backbase.flow.sme.onboarding.statustracking.provider;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.backbase.flow.casedata.status.CaseEngineEventPublisher;
import com.backbase.flow.events.JourneyEventMessage;
import com.backbase.flow.events.MetaDataKeys;
import com.backbase.flow.sme.onboarding.event.ApplicationCompleteEvent;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class DocumentRequestsCompletedMilestoneEventProviderTest {

    private CaseEngineEventPublisher publisher = mock(CaseEngineEventPublisher.class);
    private DocumentRequestsCompletedMilestoneEventProvider testDocumentRequestsCompletedMilestoneEventProvider =
        new DocumentRequestsCompletedMilestoneEventProvider(publisher);

    private JourneyEventMessage<ApplicationCompleteEvent> eventMessage(ApplicationCompleteEvent event, UUID caseKey) {
        return JourneyEventMessage.<ApplicationCompleteEvent>builder()
            .eventName("ApplicationCompleteEvent")
            .eventId(randomUUID())
            .eventTime(Instant.now())
            .caseKey(caseKey)
            .eventDescription(event.getDescription())
            .metadata(MetaDataKeys.ORIGINATED_FROM, "originatedFrom")
            .payload(event)
            .build();
    }

    private ApplicationCompleteEvent applicationCompleteEvent(UUID caseKey) {
        return new ApplicationCompleteEvent(caseKey);
    }

    @Test
    void consume_interceptingProcessEngineEvents_eventIsPublished() {

        var caseKey = randomUUID();

        testDocumentRequestsCompletedMilestoneEventProvider
            .consume(eventMessage(applicationCompleteEvent(caseKey), caseKey));

        verify(publisher, times(1)).publish(any(), any());
    }

    @Test
    void getEventInstance_interceptingProcessEngineEvents_eventIsPublished() {

        var caseEvent = testDocumentRequestsCompletedMilestoneEventProvider
            .getEventInstance(randomUUID(), Collections.emptyMap());

        assertEquals("Application completed has been checked", caseEvent.getDescription());
    }
}
