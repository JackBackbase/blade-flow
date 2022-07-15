package com.backbase.flow.sme.onboarding.process.service;

import static com.backbase.flow.process.ProcessConstants.PROCESS_VARIABLE_CASE_KEY;
import static com.backbase.flow.sme.onboarding.constants.CaseDefinitionConstants.GROUP_ID;
import static com.backbase.flow.sme.onboarding.constants.ProcessConstants.DMN_BUSINESS_LICENCE_REQUIRED;
import static com.backbase.flow.sme.onboarding.constants.ProcessConstants.DMN_BUSINESS_STRUCTURE_REQUIRED;
import static com.backbase.flow.sme.onboarding.constants.ProcessConstants.DMN_DBA_REQUIRED;
import static com.backbase.flow.sme.onboarding.constants.ProcessConstants.DMN_OUTPUT_BUSINESS_LICENCE_REQUIRED;
import static com.backbase.flow.sme.onboarding.constants.ProcessConstants.DMN_OUTPUT_BUSINESS_STRUCTURE_REQUIRED;
import static com.backbase.flow.sme.onboarding.constants.ProcessConstants.DMN_OUTPUT_DBA_REQUIRED;
import static com.backbase.flow.sme.onboarding.constants.ProcessConstants.DOCUMENT_REFERENCE_ID;
import static com.backbase.flow.sme.onboarding.constants.ProcessConstants.DOCUMENT_TYPE_BUSINESS_LICENSE;
import static com.backbase.flow.sme.onboarding.constants.ProcessConstants.DOCUMENT_TYPE_BUSINESS_STRUCTURE;
import static com.backbase.flow.sme.onboarding.constants.ProcessConstants.DOCUMENT_TYPE_DBA;
import static com.backbase.flow.sme.onboarding.constants.ProcessConstants.INITIAL_NOTE;

import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.process.annotation.ProcessBean;
import com.backbase.flow.process.service.FlowDecisionService;
import com.backbase.flow.service.bpmnprocess.CaseKey;
import com.backbase.flow.sme.onboarding.casedata.DocumentRequest;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import java.util.HashMap;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

@ProcessBean
@RequiredArgsConstructor
@Component("generateDocumentRequests")
public class DocumentCreatorService {

    private final CaseDataService caseDataService;
    private final FlowDecisionService flowDecisionService;

    public void generateDocRequests(DelegateExecution execution) {

        var caseKey = UUID.fromString(execution.getVariable(PROCESS_VARIABLE_CASE_KEY).toString());

        execution.setVariable(GROUP_ID, UUID.randomUUID().toString());

        var isDbaRequired = flowDecisionService.checkDecisionTable(caseKey,
            DMN_DBA_REQUIRED, null, new HashMap<>());

        var isBusinessLicenseRequired = flowDecisionService.checkDecisionTable(caseKey,
            DMN_BUSINESS_LICENCE_REQUIRED, null, new HashMap<>());

        var isBusinessStructureRequired = flowDecisionService.checkDecisionTable(caseKey,
            DMN_BUSINESS_STRUCTURE_REQUIRED, null, new HashMap<>());

        boolean dbaRequired = (boolean) isDbaRequired.get(0).get(DMN_OUTPUT_DBA_REQUIRED);

        boolean businessLicenseRequired = (boolean) isBusinessLicenseRequired.get(0)
            .get(DMN_OUTPUT_BUSINESS_LICENCE_REQUIRED);

        boolean businessStructureRequired = (boolean) isBusinessStructureRequired.get(0)
            .get(DMN_OUTPUT_BUSINESS_STRUCTURE_REQUIRED);

        var existingCase = caseDataService.getCaseByKey(CaseKey.from(execution).getKey());

        var smeCase = existingCase.getCaseData(SmeCaseDefinition.class);

        if (dbaRequired) {
            smeCase.getCompanyLookupInfo().getBusinessDetailsInfo().getDocumentRequests()
                .add(buildDocRequest(DOCUMENT_TYPE_DBA, execution.getVariable(GROUP_ID)));
        }
        if (businessLicenseRequired) {
            smeCase.getCompanyLookupInfo().getBusinessDetailsInfo().getDocumentRequests()
                .add(buildDocRequest(DOCUMENT_TYPE_BUSINESS_LICENSE, execution.getVariable(GROUP_ID)));
        }
        if (businessStructureRequired) {
            smeCase.getCompanyLookupInfo().getBusinessDetailsInfo().getDocumentRequests()
                .add(buildDocRequest(DOCUMENT_TYPE_BUSINESS_STRUCTURE, execution.getVariable(GROUP_ID)));
        }

        var caseInstance = caseDataService.getCaseByKey(CaseKey.from(execution).getKey());
        caseInstance.setCaseData(smeCase);
        caseDataService.updateCase(caseInstance);
    }

    private DocumentRequest buildDocRequest(String docType, Object groupId) {
        var id = UUID.randomUUID().toString();
        return new DocumentRequest()
            .withGroupId(groupId.toString())
            .withDeadline(java.time.OffsetDateTime.now().plusDays(7))
            .withDocumentType(docType)
            .withInternalId(id)
            .withExternalId(id)
            .withInitialNote(INITIAL_NOTE)
            .withReferenceId(DOCUMENT_REFERENCE_ID);
    }
}
