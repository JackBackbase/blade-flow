package com.backbase.flow.sme.onboarding.process.service;

import static com.backbase.flow.sme.onboarding.constants.ProcessConstants.DMN_DOCUMENT_REQUIRED;
import static com.backbase.flow.sme.onboarding.constants.ProcessConstants.DMN_OUTPUT_DOCUMENT_REQUIRED;

import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.sme.onboarding.casedata.DocumentRequest;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SmeDocumentRequestService {

    private final CaseDataService caseDataService;

    public boolean allDocsUploaded(UUID caseKey) {
        var caseInstance = caseDataService.getCaseByKey(caseKey);
        var caseData = caseInstance.getCaseData(SmeCaseDefinition.class);
        return caseData.getCompanyLookupInfo().getBusinessDetailsInfo().getDocumentRequests().stream()
            .filter(Objects::nonNull)
            .map(DocumentRequest::getStatus)
            .noneMatch(DocumentRequest.Status.OPEN::equals);
    }

    public boolean isDocumentRequired(InteractionContext context) {
        var documentRequired = context.checkDecisionTable(context.getCaseKey(),
            DMN_DOCUMENT_REQUIRED, null, new HashMap<>());
        return (boolean) documentRequired.get(0).get(DMN_OUTPUT_DOCUMENT_REQUIRED);
    }
}
