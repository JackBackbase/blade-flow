package com.backbase.flow.sme.onboarding.statustracking.provider;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import com.backbase.flow.casedata.status.CaseEngineEventPublisher;
import com.backbase.flow.iam.FlowSecurityContext;
import com.backbase.flow.sme.onboarding.process.service.SmeDocumentRequestService;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class DocumentsUploadCompletedMilestoneEventProviderTest {

    private DocumentsUploadCompletedMilestoneEventProvider testDocumentsUploadCompletedMilestoneEventProvider =
        new DocumentsUploadCompletedMilestoneEventProvider(
            mock(SmeDocumentRequestService.class),
            mock(CaseEngineEventPublisher.class),
            mock(FlowSecurityContext.class)
        );

    @Test
    void getEventInstance_interceptingProcessEngineEvents_eventIsPublished() {

        var caseEvent = testDocumentsUploadCompletedMilestoneEventProvider
            .getEventInstance(randomUUID(), Collections.emptyMap());

        assertEquals("Documents Uploaded has been checked", caseEvent.getDescription());
    }
}
