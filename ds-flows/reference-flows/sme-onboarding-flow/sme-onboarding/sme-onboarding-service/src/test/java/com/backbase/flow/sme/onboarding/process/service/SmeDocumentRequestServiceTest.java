package com.backbase.flow.sme.onboarding.process.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.iam.util.WithFlowAnonymousUser;
import com.backbase.flow.sme.onboarding.builder.SmeCaseDefBuilder;
import com.backbase.flow.sme.onboarding.casedata.DocumentRequest;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class SmeDocumentRequestServiceTest {

    private CaseDataService caseDataService = mock(CaseDataService.class);
    private SmeDocumentRequestService smeDocumentRequestService = new SmeDocumentRequestService(caseDataService);

    @Test
    @WithFlowAnonymousUser
    void allDocsUploaded_docOpen_returnFalse() {
        var openDocumentRequest = new DocumentRequest();
        openDocumentRequest.setStatus(DocumentRequest.Status.OPEN);
        openDocumentRequest.setDocumentType("DBA document");
        openDocumentRequest.setReferenceId("Documents");
        openDocumentRequest.setInitialNote("Note");
        openDocumentRequest.setExternalId(UUID.randomUUID().toString());
        openDocumentRequest.setDeadline(OffsetDateTime.now().plusDays(6));
        var notOpenDocumentRequest = new DocumentRequest();
        notOpenDocumentRequest.setStatus(DocumentRequest.Status.PENDING_REVIEW);
        notOpenDocumentRequest.setDocumentType("DBA document");
        notOpenDocumentRequest.setReferenceId("Documents");
        notOpenDocumentRequest.setInitialNote("Note");
        notOpenDocumentRequest.setExternalId(UUID.randomUUID().toString());
        notOpenDocumentRequest.setDeadline(OffsetDateTime.now().plusDays(6));
        var documentRequests = new ArrayList<DocumentRequest>();
        documentRequests.add(openDocumentRequest);
        documentRequests.add(notOpenDocumentRequest);
        var exCase = startCase(documentRequests);
        when(caseDataService.getCaseByKey(exCase.getKey())).thenReturn(exCase);
        assertFalse(smeDocumentRequestService.allDocsUploaded(exCase.getKey()));
    }

    @Test
    @WithFlowAnonymousUser
    void allDocsUploaded_allDocsOpen_returnFalse() {
        var openDocumentRequestDba = new DocumentRequest();
        openDocumentRequestDba.setStatus(DocumentRequest.Status.OPEN);
        openDocumentRequestDba.setDocumentType("DBA document");
        openDocumentRequestDba.setReferenceId("Documents");
        openDocumentRequestDba.setInitialNote("Note");
        openDocumentRequestDba.setExternalId(UUID.randomUUID().toString());
        openDocumentRequestDba.setDeadline(OffsetDateTime.now().plusDays(6));
        var openDocumentRequest = new DocumentRequest();
        openDocumentRequest.setStatus(DocumentRequest.Status.OPEN);
        openDocumentRequest.setDocumentType("DBA document");
        openDocumentRequest.setReferenceId("Documents");
        openDocumentRequest.setInitialNote("Note");
        openDocumentRequest.setExternalId(UUID.randomUUID().toString());
        openDocumentRequest.setDeadline(OffsetDateTime.now().plusDays(6));
        var documentRequests = new ArrayList<DocumentRequest>();
        documentRequests.add(openDocumentRequest);
        documentRequests.add(openDocumentRequestDba);
        var exCase = startCase(documentRequests);
        when(caseDataService.getCaseByKey(exCase.getKey())).thenReturn(exCase);
        assertFalse(smeDocumentRequestService.allDocsUploaded(exCase.getKey()));
    }

    @Test
    @WithFlowAnonymousUser
    void allDocsUploaded_noDocsOpen_returnTrue() {
        var notOpenDocumentRequestDba = new DocumentRequest();
        notOpenDocumentRequestDba.setStatus(DocumentRequest.Status.PENDING_REVIEW);
        notOpenDocumentRequestDba.setDocumentType("DBA document");
        notOpenDocumentRequestDba.setReferenceId("Documents");
        notOpenDocumentRequestDba.setInitialNote("Note");
        notOpenDocumentRequestDba.setExternalId(UUID.randomUUID().toString());
        notOpenDocumentRequestDba.setDeadline(OffsetDateTime.now().plusDays(6));
        var notOpenDocumentRequest = new DocumentRequest();
        notOpenDocumentRequest.setStatus(DocumentRequest.Status.PENDING_REVIEW);
        notOpenDocumentRequest.setDocumentType("DBA document");
        notOpenDocumentRequest.setReferenceId("Documents");
        notOpenDocumentRequest.setInitialNote("Note");
        notOpenDocumentRequest.setExternalId(UUID.randomUUID().toString());
        notOpenDocumentRequest.setDeadline(OffsetDateTime.now().plusDays(6));
        var documentRequests = new ArrayList<DocumentRequest>();
        documentRequests.add(notOpenDocumentRequest);
        documentRequests.add(notOpenDocumentRequestDba);
        var exCase = startCase(documentRequests);
        when(caseDataService.getCaseByKey(exCase.getKey())).thenReturn(exCase);
        assertTrue(smeDocumentRequestService.allDocsUploaded(exCase.getKey()));
    }

    private Case startCase(List<DocumentRequest> documentRequests) {
        Case aCase = new Case();
        var smeCaseData = SmeCaseDefBuilder.getInstance()
            .firstName("ab")
            .lastName("bc")
            .email("abc@gmail.com")
            .dateOfBirth(LocalDate.parse("1970-01-01"))
            .soleProp()
            .documentRequests(documentRequests)
            .build();
        aCase.setCaseData(smeCaseData);
        return aCase;
    }
}
