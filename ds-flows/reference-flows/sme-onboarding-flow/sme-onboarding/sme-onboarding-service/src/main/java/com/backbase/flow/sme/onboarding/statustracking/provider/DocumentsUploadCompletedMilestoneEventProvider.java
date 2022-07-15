package com.backbase.flow.sme.onboarding.statustracking.provider;

import com.backbase.flow.casedata.status.CaseEngineEventPublisher;
import com.backbase.flow.casedata.status.CaseEvent;
import com.backbase.flow.events.JourneyEventConsumer;
import com.backbase.flow.events.JourneyEventMessage;
import com.backbase.flow.iam.FlowSecurityContext;
import com.backbase.flow.integration.service.event.UploadDocumentsTaskCompletedEvent;
import com.backbase.flow.process.events.ProcessEngineEvent;
import com.backbase.flow.sme.onboarding.process.service.SmeDocumentRequestService;
import com.backbase.flow.sme.onboarding.statustracking.event.DocumentsUploadCompletedMilestoneEvent;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class DocumentsUploadCompletedMilestoneEventProvider extends BaseDocumentRequestsMilestoneProvider
    implements JourneyEventConsumer<UploadDocumentsTaskCompletedEvent> {

    private final FlowSecurityContext flowSecurityContext;
    private final SmeDocumentRequestService documentRequestService;

    public DocumentsUploadCompletedMilestoneEventProvider(
        SmeDocumentRequestService documentRequestService,
        CaseEngineEventPublisher publisher,
        FlowSecurityContext flowSecurityContext
    ) {
        super(publisher);
        this.flowSecurityContext = flowSecurityContext;
        this.documentRequestService = documentRequestService;
    }

    @Override
    public void consume(JourneyEventMessage<UploadDocumentsTaskCompletedEvent> eventMessage) {
        publishEvent(eventMessage);
    }

    private void publishEvent(JourneyEventMessage<? extends ProcessEngineEvent> eventMessage) {
        var payload = eventMessage.getPayload();
        var caseKey = payload.getCaseKey();
        var allDocsUploaded = flowSecurityContext.runWithoutAuthorization(
            () -> documentRequestService.allDocsUploaded(caseKey));
        if (Boolean.TRUE.equals(allDocsUploaded)) {
            var eventInstance = getEventInstance(caseKey, Collections.emptyMap());
            publisher.publish(caseKey, eventInstance);
        }
    }

    @Override
    protected CaseEvent getEventInstance(UUID caseKey, Map<String, String> metaData) {
        return new DocumentsUploadCompletedMilestoneEvent(caseKey, metaData);
    }
}
