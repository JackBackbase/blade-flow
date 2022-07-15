package com.backbase.flow.sme.onboarding.statustracking.provider;

import com.backbase.flow.casedata.status.CaseEngineEventPublisher;
import com.backbase.flow.casedata.status.CaseEvent;
import com.backbase.flow.events.JourneyEventConsumer;
import com.backbase.flow.events.JourneyEventMessage;
import com.backbase.flow.sme.onboarding.event.ApplicationSubmittedEvent;
import com.backbase.flow.sme.onboarding.statustracking.event.ApplicationSubmittedMilestoneEvent;
import java.util.Map;
import java.util.UUID;

public class ApplicationSubmittedMilestoneEventProvider extends BaseDocumentRequestsMilestoneProvider
    implements JourneyEventConsumer<ApplicationSubmittedEvent> {

    public ApplicationSubmittedMilestoneEventProvider(CaseEngineEventPublisher publisher) {
        super(publisher);
    }

    @Override
    public void consume(JourneyEventMessage<ApplicationSubmittedEvent> eventMessage) {
        super.publishMilestoneEvent(eventMessage);
    }

    @Override
    protected CaseEvent getEventInstance(UUID caseKey, Map<String, String> metaData) {
        return new ApplicationSubmittedMilestoneEvent(caseKey, metaData);
    }
}
