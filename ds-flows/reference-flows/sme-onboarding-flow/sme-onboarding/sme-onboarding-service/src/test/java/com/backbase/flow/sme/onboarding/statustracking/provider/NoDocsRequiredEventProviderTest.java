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
import com.backbase.flow.sme.onboarding.event.NoDocsRequiredEvent;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class NoDocsRequiredEventProviderTest {

    private CaseEngineEventPublisher publisher = mock(CaseEngineEventPublisher.class);
    private NoDocsRequiredEventProvider testNoDocsRequiredEventProvider = new NoDocsRequiredEventProvider(publisher);

    private JourneyEventMessage<NoDocsRequiredEvent> eventMessage(NoDocsRequiredEvent event, UUID caseKey) {
        return JourneyEventMessage.<NoDocsRequiredEvent>builder()
            .eventName("ApplicationCompleteEvent")
            .eventId(randomUUID())
            .eventTime(Instant.now())
            .caseKey(caseKey)
            .eventDescription(event.getDescription())
            .metadata(MetaDataKeys.ORIGINATED_FROM, "originatedFrom")
            .payload(event)
            .build();
    }

    private NoDocsRequiredEvent noDocsRequiredEvent(UUID caseKey) {
        return new NoDocsRequiredEvent(caseKey);
    }

    @Test
    void consume_interceptingProcessEngineEvents_eventIsPublished() {

        var caseKey = randomUUID();

        testNoDocsRequiredEventProvider.consume(eventMessage(noDocsRequiredEvent(caseKey), caseKey));

        verify(publisher, times(1)).publish(any(), any());
    }

    @Test
    void getEventInstance_interceptingProcessEngineEvents_eventIsPublished() {

        var caseEvent = testNoDocsRequiredEventProvider.getEventInstance(randomUUID(), Collections.emptyMap());

        assertEquals("Documents reviewed has been checked", caseEvent.getDescription());
    }
}
