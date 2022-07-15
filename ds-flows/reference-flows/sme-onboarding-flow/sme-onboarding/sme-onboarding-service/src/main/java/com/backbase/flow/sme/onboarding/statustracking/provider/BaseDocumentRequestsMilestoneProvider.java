package com.backbase.flow.sme.onboarding.statustracking.provider;

import com.backbase.flow.casedata.status.CaseEngineEventPublisher;
import com.backbase.flow.casedata.status.CaseEvent;
import com.backbase.flow.events.JourneyEventMessage;
import com.backbase.flow.process.events.ProcessEngineEvent;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseDocumentRequestsMilestoneProvider {

    protected final CaseEngineEventPublisher publisher;

    protected abstract CaseEvent getEventInstance(UUID caseKey, Map<String, String> metaData);

    public void publishMilestoneEvent(JourneyEventMessage<? extends ProcessEngineEvent> eventMessage) {
        var payload = eventMessage.getPayload();
        var caseKey = payload.getCaseKey();
        var eventInstance = getEventInstance(caseKey, Collections.emptyMap());
        publisher.publish(caseKey, eventInstance);
    }
}
