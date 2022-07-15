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
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.iam.util.WithFlowAnonymousUser;
import com.backbase.flow.process.service.FlowDecisionService;
import com.backbase.flow.sme.onboarding.casedata.BusinessDetails;
import com.backbase.flow.sme.onboarding.casedata.BusinessStructureInfo;
import com.backbase.flow.sme.onboarding.casedata.CompanyLookupInfo;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentCreatorServiceTest {

    private final UUID casekeyUUID = UUID.randomUUID();
    private final OffsetDateTime sixDaysFromNow = OffsetDateTime.now().plusDays(6);
    private final DelegateExecution delegateExecution = mock(DelegateExecution.class);
    private final Case exCase = new Case().setKey(casekeyUUID);
    private Map<String, Object> documentsMapDba;
    private Map<String, Object> documentsMapBizLicence;
    private Map<String, Object> documentsMapBizStructure;
    private List<Map<String, Object>> isDbaRequired;
    private List<Map<String, Object>> isBusinessLicenceRequired;
    private List<Map<String, Object>> isBusinessStructureRequired;

    private CaseDataService caseDataService = mock(CaseDataService.class);
    private FlowDecisionService flowDecisionService = mock(FlowDecisionService.class);
    private DocumentCreatorService documentCreatorService =
        new DocumentCreatorService(caseDataService, flowDecisionService);

    @BeforeEach
    public void setUp() {
        var smeCaseDefinition = new SmeCaseDefinition();
        smeCaseDefinition.setCompanyLookupInfo(new CompanyLookupInfo());
        smeCaseDefinition.getCompanyLookupInfo().setBusinessStructureInfo(new BusinessStructureInfo());
        smeCaseDefinition.getCompanyLookupInfo().setBusinessDetailsInfo(new BusinessDetails());

        documentsMapDba = new HashMap<>();
        documentsMapDba.put(DMN_OUTPUT_DBA_REQUIRED, true);
        documentsMapBizLicence = new HashMap<>();
        documentsMapBizLicence.put(DMN_OUTPUT_BUSINESS_LICENCE_REQUIRED, true);
        documentsMapBizStructure = new HashMap<>();
        documentsMapBizStructure.put(DMN_OUTPUT_BUSINESS_STRUCTURE_REQUIRED, true);
        isDbaRequired = new ArrayList<>();
        isDbaRequired.add(documentsMapDba);
        isBusinessLicenceRequired = new ArrayList<>();
        isBusinessLicenceRequired.add(documentsMapBizLicence);
        isBusinessStructureRequired = new ArrayList<>();
        isBusinessStructureRequired.add(documentsMapBizStructure);
        when(flowDecisionService.checkDecisionTable(casekeyUUID, DMN_DBA_REQUIRED, null, new HashMap<>()))
            .thenReturn(isDbaRequired);
        when(flowDecisionService.checkDecisionTable(casekeyUUID, DMN_BUSINESS_LICENCE_REQUIRED, null, new HashMap<>()))
            .thenReturn(isBusinessLicenceRequired);
        when(
            flowDecisionService.checkDecisionTable(casekeyUUID, DMN_BUSINESS_STRUCTURE_REQUIRED, null, new HashMap<>()))
            .thenReturn(isBusinessStructureRequired);
        when(delegateExecution.getVariable(PROCESS_VARIABLE_CASE_KEY)).thenReturn(casekeyUUID.toString());
        when(delegateExecution.getVariable(GROUP_ID)).thenReturn(UUID.randomUUID());
        exCase.setCaseData(smeCaseDefinition);
        when(caseDataService.getCaseByKey(UUID.fromString(casekeyUUID.toString()))).thenReturn(exCase);
    }

    @Test
    @WithFlowAnonymousUser
    void generateDocRequests_dbaAndBizStructureRequired_setDbaAndBizStructureDocumentRequestInCase() {
        documentsMapDba.put(DMN_OUTPUT_DBA_REQUIRED, true);
        documentsMapBizLicence.put(DMN_OUTPUT_BUSINESS_LICENCE_REQUIRED, false);
        documentsMapBizStructure.put(DMN_OUTPUT_BUSINESS_STRUCTURE_REQUIRED, true);
        isDbaRequired = new ArrayList<>();
        isDbaRequired.add(documentsMapDba);
        isBusinessLicenceRequired = new ArrayList<>();
        isBusinessLicenceRequired.add(documentsMapBizLicence);
        isBusinessStructureRequired = new ArrayList<>();
        isBusinessStructureRequired.add(documentsMapBizStructure);

        documentCreatorService.generateDocRequests(delegateExecution);

        var savedCase = caseDataService.getCaseByKey(casekeyUUID);
        var smeCase = savedCase.getCaseData(SmeCaseDefinition.class);
        var documentRequests = smeCase.getCompanyLookupInfo().getBusinessDetailsInfo()
            .getDocumentRequests();
        var sevenDaysFromNow = OffsetDateTime.now().plusDays(7);
        assertThat(documentRequests.size()).isEqualTo(2);
        assertThat(documentRequests.get(0).getDocumentType()).isEqualTo(DOCUMENT_TYPE_DBA);
        assertThat(documentRequests.get(0).getInternalId()).isNotEmpty();
        assertThat(documentRequests.get(0).getGroupId()).isNotEmpty();
        assertThat(documentRequests.get(0).getExternalId()).isEqualTo(documentRequests.get(0).getInternalId());
        assertThat(documentRequests.get(0).getDeadline()).isBetween(sixDaysFromNow, sevenDaysFromNow);
        assertThat(documentRequests.get(0).getDeadline()).isAfter(sixDaysFromNow);
        assertThat(documentRequests.get(0).getInitialNote()).isEqualTo(INITIAL_NOTE);
        assertThat(documentRequests.get(0).getReferenceId()).isEqualTo(DOCUMENT_REFERENCE_ID);
        assertThat(documentRequests.get(1).getDocumentType()).isEqualTo(DOCUMENT_TYPE_BUSINESS_STRUCTURE);
        assertThat(documentRequests.get(1).getInternalId()).isNotEmpty();
        assertThat(documentRequests.get(1).getDeadline()).isBetween(sixDaysFromNow, sevenDaysFromNow);
        assertThat(documentRequests.get(1).getDeadline()).isAfter(sixDaysFromNow);
        assertThat(documentRequests.get(1).getInitialNote()).isEqualTo(INITIAL_NOTE);
        assertThat(documentRequests.get(1).getReferenceId()).isEqualTo(DOCUMENT_REFERENCE_ID);
    }

    @Test
    @WithFlowAnonymousUser
    void generateDocRequests_businessLicenseRequired_setBusinessLicenseDocumentRequestInCase() {
        documentsMapDba.put(DMN_OUTPUT_DBA_REQUIRED, false);
        documentsMapBizLicence.put(DMN_OUTPUT_BUSINESS_LICENCE_REQUIRED, true);
        documentsMapBizStructure.put(DMN_OUTPUT_BUSINESS_STRUCTURE_REQUIRED, false);
        isDbaRequired = new ArrayList<>();
        isDbaRequired.add(documentsMapDba);
        isBusinessLicenceRequired = new ArrayList<>();
        isBusinessLicenceRequired.add(documentsMapBizLicence);
        isBusinessStructureRequired = new ArrayList<>();
        isBusinessStructureRequired.add(documentsMapBizStructure);

        documentCreatorService.generateDocRequests(delegateExecution);

        var savedCase = caseDataService.getCaseByKey(casekeyUUID);
        var smeCase = savedCase.getCaseData(SmeCaseDefinition.class);
        var documentRequests = smeCase.getCompanyLookupInfo().getBusinessDetailsInfo()
            .getDocumentRequests();
        var sevenDaysFromNow = OffsetDateTime.now().plusDays(7);
        assertThat(documentRequests.size()).isEqualTo(1);
        assertThat(documentRequests.get(0).getDocumentType()).isEqualTo(DOCUMENT_TYPE_BUSINESS_LICENSE);
        assertThat(documentRequests.get(0).getInternalId()).isNotEmpty();
        assertThat(documentRequests.get(0).getDeadline()).isBetween(sixDaysFromNow, sevenDaysFromNow);
        assertThat(documentRequests.get(0).getDeadline()).isAfter(sixDaysFromNow);
        assertThat(documentRequests.get(0).getInitialNote()).isEqualTo(INITIAL_NOTE);
        assertThat(documentRequests.get(0).getReferenceId()).isEqualTo(DOCUMENT_REFERENCE_ID);
    }

    @Test
    @WithFlowAnonymousUser
    void generateDocRequests_businessLicenseAndBizStructureAndDbaRequired_setBusinessLicenseAndBuzStructureAndDbaDocumentRequestInCase() {
        documentCreatorService.generateDocRequests(delegateExecution);

        var sevenDaysFromNow = OffsetDateTime.now().plusDays(7);
        var savedCase = caseDataService.getCaseByKey(casekeyUUID);
        var smeCase = savedCase.getCaseData(SmeCaseDefinition.class);
        var documentRequests = smeCase.getCompanyLookupInfo().getBusinessDetailsInfo()
            .getDocumentRequests();
        assertThat(documentRequests.size()).isEqualTo(3);
        assertThat(documentRequests.get(0).getDocumentType()).isEqualTo(DOCUMENT_TYPE_DBA);
        assertThat(documentRequests.get(0).getInternalId()).isNotEmpty();
        assertThat(documentRequests.get(0).getInitialNote()).isEqualTo(INITIAL_NOTE);
        assertThat(documentRequests.get(0).getReferenceId()).isEqualTo(DOCUMENT_REFERENCE_ID);
        assertThat(documentRequests.get(0).getDeadline()).isBetween(sixDaysFromNow, sevenDaysFromNow);
        assertThat(documentRequests.get(0).getDeadline()).isAfter(sixDaysFromNow);
        assertThat(documentRequests.get(1).getDocumentType()).isEqualTo(DOCUMENT_TYPE_BUSINESS_LICENSE);
        assertThat(documentRequests.get(1).getInternalId()).isNotEmpty();
        assertThat(documentRequests.get(1).getDeadline()).isBetween(sixDaysFromNow, sevenDaysFromNow);
        assertThat(documentRequests.get(1).getDeadline()).isAfter(sixDaysFromNow);
        assertThat(documentRequests.get(1).getInitialNote()).isEqualTo(INITIAL_NOTE);
        assertThat(documentRequests.get(1).getReferenceId()).isEqualTo(DOCUMENT_REFERENCE_ID);
        assertThat(documentRequests.get(2).getDocumentType()).isEqualTo(DOCUMENT_TYPE_BUSINESS_STRUCTURE);
        assertThat(documentRequests.get(2).getInternalId()).isNotEmpty();
        assertThat(documentRequests.get(2).getDeadline()).isBetween(sixDaysFromNow, sevenDaysFromNow);
        assertThat(documentRequests.get(2).getDeadline()).isAfter(sixDaysFromNow);
        assertThat(documentRequests.get(2).getInitialNote()).isEqualTo(INITIAL_NOTE);
        assertThat(documentRequests.get(2).getReferenceId()).isEqualTo(DOCUMENT_REFERENCE_ID);
    }

}
