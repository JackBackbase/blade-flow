package com.backbase.flow.sme.onboarding.statustracking.provider;

import com.backbase.flow.casedata.status.CaseEngineEventPublisher;
import com.backbase.flow.casedata.status.CaseEvent;
import com.backbase.flow.events.JourneyEventConsumer;
import com.backbase.flow.events.JourneyEventMessage;
import com.backbase.flow.sme.onboarding.event.NoDocsRequiredEvent;
import com.backbase.flow.sme.onboarding.statustracking.event.AllDocsVerifiedMilestoneEvent;
import java.util.Map;
import java.util.UUID;

public class NoDocsRequiredEventProvider extends BaseDocumentRequestsMilestoneProvider
    implements JourneyEventConsumer<NoDocsRequiredEvent> {

    public NoDocsRequiredEventProvider(CaseEngineEventPublisher publisher) {
        super(publisher);
    }

    @Override
    public void consume(JourneyEventMessage<NoDocsRequiredEvent> eventMessage) {
        super.publishMilestoneEvent(eventMessage);
    }

    @Override
    protected CaseEvent getEventInstance(UUID caseKey, Map<String, String> metaData) {
        return new AllDocsVerifiedMilestoneEvent(caseKey, metaData);
    }
}
