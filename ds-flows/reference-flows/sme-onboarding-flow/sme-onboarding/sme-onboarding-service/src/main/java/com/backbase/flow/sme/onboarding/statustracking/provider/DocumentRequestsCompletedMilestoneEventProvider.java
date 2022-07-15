package com.backbase.flow.sme.onboarding.statustracking.provider;

import com.backbase.flow.casedata.status.CaseEngineEventPublisher;
import com.backbase.flow.casedata.status.CaseEvent;
import com.backbase.flow.events.JourneyEventConsumer;
import com.backbase.flow.events.JourneyEventMessage;
import com.backbase.flow.sme.onboarding.event.ApplicationCompleteEvent;
import com.backbase.flow.sme.onboarding.statustracking.event.DocumentRequestsCompletedMilestoneEvent;
import java.util.Map;
import java.util.UUID;

public class DocumentRequestsCompletedMilestoneEventProvider extends BaseDocumentRequestsMilestoneProvider
    implements JourneyEventConsumer<ApplicationCompleteEvent> {

    public DocumentRequestsCompletedMilestoneEventProvider(CaseEngineEventPublisher publisher) {
        super(publisher);
    }

    @Override
    public void consume(JourneyEventMessage<ApplicationCompleteEvent> eventMessage) {
        super.publishMilestoneEvent(eventMessage);
    }

    @Override
    protected CaseEvent getEventInstance(UUID caseKey, Map<String, String> metaData) {
        return new DocumentRequestsCompletedMilestoneEvent(caseKey, metaData);
    }
}
