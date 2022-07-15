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
import com.backbase.flow.sme.onboarding.event.AllDocsVerifiedEvent;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AllDocsVerifiedMilestoneEventProviderTest {

    private CaseEngineEventPublisher publisher = mock(CaseEngineEventPublisher.class);
    private AllDocsVerifiedMilestoneEventProvider testAllDocsVerifiedMilestoneEventProvider =
        new AllDocsVerifiedMilestoneEventProvider(publisher);

    private JourneyEventMessage<AllDocsVerifiedEvent> eventMessage(AllDocsVerifiedEvent event, UUID caseKey) {
        return JourneyEventMessage.<AllDocsVerifiedEvent>builder()
            .eventName("AllDocsVerifiedEvent")
            .eventId(randomUUID())
            .eventTime(Instant.now())
            .caseKey(caseKey)
            .eventDescription(event.getDescription())
            .metadata(MetaDataKeys.ORIGINATED_FROM, "originatedFrom")
            .payload(event)
            .build();
    }

    private AllDocsVerifiedEvent allDocsVerifiedEvent(UUID caseKey) {
        return new AllDocsVerifiedEvent(caseKey);
    }

    @Test
    void consume_interceptingProcessEngineEvents_eventIsPublished() {

        var caseKey = randomUUID();

        testAllDocsVerifiedMilestoneEventProvider.consume(eventMessage(allDocsVerifiedEvent(caseKey), caseKey));

        verify(publisher, times(1)).publish(any(), any());
    }

    @Test
    void getEventInstance_interceptingProcessEngineEvents_eventIsPublished() {

        var caseEvent = testAllDocsVerifiedMilestoneEventProvider
            .getEventInstance(randomUUID(), Collections.emptyMap());

        assertEquals("Documents reviewed has been checked", caseEvent.getDescription());
    }
}
