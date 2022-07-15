package com.backbase.flow.sme.onboarding.statustracking.provider;

import static java.util.UUID.randomUUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.backbase.flow.casedata.status.CaseEngineEventPublisher;
import com.backbase.flow.events.JourneyEventMessage;
import com.backbase.flow.events.MetaDataKeys;
import com.backbase.flow.process.events.ProcessEngineEvent;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class BaseDocumentRequestsMilestoneProviderTest {

    private CaseEngineEventPublisher publisher = mock(CaseEngineEventPublisher.class);
    private TestBaseDocumentRequestsMilestoneProvider milestoneProvider =
        new TestBaseDocumentRequestsMilestoneProvider(publisher);

    @Test
    void publishMilestoneEvent_interceptingProcessEngineEvents_eventIsPublished() {
        var caseKey = randomUUID();

        milestoneProvider.publishMilestoneEvent(eventMessage(event(caseKey), caseKey));

        verify(publisher, times(1)).publish(any(), any());
    }

    private JourneyEventMessage<ProcessEngineEvent> eventMessage(ProcessEngineEvent event, UUID caseKey) {
        return JourneyEventMessage.<ProcessEngineEvent>builder()
            .eventName("ProcessEngineEvent")
            .eventId(randomUUID())
            .eventTime(Instant.now())
            .caseKey(caseKey)
            .eventDescription(event.getDescription())
            .metadata(MetaDataKeys.ORIGINATED_FROM, "originatedFrom")
            .payload(event)
            .build();
    }

    private ProcessEngineEvent event(UUID caseKey) {
        return new ProcessEngineEvent(
            "Process engine test event", caseKey, randomUUID().toString(), Collections.emptyMap()
        );
    }
}
