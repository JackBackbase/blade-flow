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
import com.backbase.flow.sme.onboarding.event.ApplicationSubmittedEvent;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ApplicationSubmittedMilestoneEventProviderTest {

    private CaseEngineEventPublisher publisher = mock(CaseEngineEventPublisher.class);
    private ApplicationSubmittedMilestoneEventProvider testApplicationSubmittedMilestoneEventProvider =
        new ApplicationSubmittedMilestoneEventProvider(publisher);

    private JourneyEventMessage<ApplicationSubmittedEvent> eventMessage(ApplicationSubmittedEvent event, UUID caseKey) {
        return JourneyEventMessage.<ApplicationSubmittedEvent>builder()
            .eventName("ApplicationCompleteEvent")
            .eventId(randomUUID())
            .eventTime(Instant.now())
            .caseKey(caseKey)
            .eventDescription(event.getDescription())
            .metadata(MetaDataKeys.ORIGINATED_FROM, "originatedFrom")
            .payload(event)
            .build();
    }

    private ApplicationSubmittedEvent applicationSubmittedEvent(UUID caseKey) {
        return new ApplicationSubmittedEvent(caseKey);
    }

    @Test
    void consume_interceptingProcessEngineEvents_eventIsPublished() {

        var caseKey = randomUUID();

        testApplicationSubmittedMilestoneEventProvider
            .consume(eventMessage(applicationSubmittedEvent(caseKey), caseKey));

        verify(publisher, times(1)).publish(any(), any());
    }

    @Test
    void getEventInstance_interceptingProcessEngineEvents_eventIsPublished() {

        var caseEvent = testApplicationSubmittedMilestoneEventProvider
            .getEventInstance(randomUUID(), Collections.emptyMap());

        assertEquals("Application Submitted has been checked", caseEvent.getDescription());
    }
}
