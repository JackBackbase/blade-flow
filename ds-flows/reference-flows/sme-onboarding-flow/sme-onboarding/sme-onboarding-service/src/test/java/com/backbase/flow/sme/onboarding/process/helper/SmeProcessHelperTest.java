package com.backbase.flow.sme.onboarding.process.helper;

import static com.backbase.flow.sme.onboarding.TestConstants.TRANSPORTATION_AND_WAREHOUSING_GENERAL;
import static com.backbase.flow.sme.onboarding.process.helper.BusinessRelationsPredicates.hasCompleteStatus;
import static com.backbase.flow.sme.onboarding.process.helper.BusinessRelationsPredicates.hasInReviewStatus;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.events.PersistedEvent;
import com.backbase.flow.casedata.events.PersistedEventRepository;
import com.backbase.flow.casedata.status.CaseEngineEventPublisher;
import com.backbase.flow.sme.onboarding.builder.SmeCaseDefBuilder;
import com.backbase.flow.sme.onboarding.casedata.Address;
import com.backbase.flow.sme.onboarding.casedata.AmlInfo;
import com.backbase.flow.sme.onboarding.casedata.AmlInfo.Status;
import com.backbase.flow.sme.onboarding.casedata.Applicant;
import com.backbase.flow.sme.onboarding.casedata.BusinessDetails;
import com.backbase.flow.sme.onboarding.casedata.BusinessIdentityInfo;
import com.backbase.flow.sme.onboarding.casedata.BusinessRelationsCaseData;
import com.backbase.flow.sme.onboarding.casedata.BusinessStructureInfo.Subtype;
import com.backbase.flow.sme.onboarding.casedata.BusinessStructureInfo.Type;
import com.backbase.flow.sme.onboarding.casedata.CompanyLookupInfo;
import com.backbase.flow.sme.onboarding.casedata.DocumentRequest;
import com.backbase.flow.sme.onboarding.casedata.Industry;
import com.backbase.flow.sme.onboarding.casedata.RiskAssessmentCaseData;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import com.backbase.flow.sme.onboarding.casedata.TermsAndConditions;
import com.backbase.flow.sme.onboarding.constants.CaseDefinitionConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

class SmeProcessHelperTest {

    private CaseDataService caseDataService = mock(CaseDataService.class);
    private ObjectMapper objectMapper = mock(ObjectMapper.class);
    private PersistedEventRepository persistedEventRepository = mock(PersistedEventRepository.class);
    private CaseEngineEventPublisher publisher = mock(CaseEngineEventPublisher.class);
    private SmeProcessHelper smeProcessHelper = new SmeProcessHelper(
        objectMapper, caseDataService, persistedEventRepository, publisher
    );

    private DelegateExecution delegateExecution = mock(DelegateExecution.class);
    private Case mockCase = mock(Case.class);
    private SmeCaseDefinition caseDefinition;

    @BeforeEach
    private void init() {
        var caseKey = UUID.randomUUID();
        caseDefinition = SmeCaseDefBuilder.getInstance().firstName("b").lastName("b")
            .dateOfBirth(LocalDate.parse("1970-01-01")).email("test@bb.com").phoneNumber("5555555").isRegistrar(true)
            .build();

        var businessPerson1 = new Applicant();
        businessPerson1.setId(UUID.randomUUID().toString());
        businessPerson1.setFirstName("abc");
        businessPerson1.setLastName("dba");
        businessPerson1.setEmail("my-emaail@test.invalid");
        businessPerson1.setRelationType(Applicant.RelationType.OWNER);
        businessPerson1.setRole("CEO");
        businessPerson1.setIsRegistrar(true);
        businessPerson1.setOwnershipPercentage(30.0);
        var businessPerson2 = new Applicant();
        businessPerson2.setId(UUID.randomUUID().toString());
        businessPerson2.setFirstName("uty");
        businessPerson2.setLastName("vcx");
        businessPerson2.setEmail("second-email@test.invalid");
        businessPerson2.setRelationType(Applicant.RelationType.CONTROL_PERSON);
        businessPerson2.setRole("CTO");
        businessPerson2.setIsRegistrar(false);
        businessPerson2.setOwnershipPercentage(70.0);
        var applicantList = new ArrayList<Applicant>();
        applicantList.add(businessPerson1);
        applicantList.add(businessPerson2);
        caseDefinition.setApplicants(applicantList);
        caseDefinition.setRiskAssessment(new RiskAssessmentCaseData());

        when(delegateExecution.getVariable("caseKey")).thenReturn(caseKey);
        when(caseDataService.getCaseByKey(caseKey)).thenReturn(mockCase);
        when(mockCase.getCaseData(SmeCaseDefinition.class)).thenReturn(caseDefinition);
    }

    @Test
    void isDocumentsApproved_withValidData_returnTrue() {
        var companyLookupInfo = new CompanyLookupInfo();
        var businessDetails = new BusinessDetails();
        var documentRequests = new ArrayList<DocumentRequest>();
        var documentRequest = new DocumentRequest();
        documentRequest.setStatus(DocumentRequest.Status.APPROVED);
        documentRequests.add(documentRequest);
        businessDetails.setDocumentRequests(documentRequests);
        companyLookupInfo.setBusinessDetailsInfo(businessDetails);
        caseDefinition.setCompanyLookupInfo(companyLookupInfo);

        boolean isDocumentApproved = smeProcessHelper.isDocumentsApproved(delegateExecution);

        assertTrue(isDocumentApproved);
    }

    @Test
    void isDocumentsApproved_withInvalidData_returnFalse() {
        var companyLookupInfo = new CompanyLookupInfo();
        var businessDetails = new BusinessDetails();
        var documentRequests = new ArrayList<DocumentRequest>();
        var documentRequest = new DocumentRequest();
        documentRequest.setStatus(DocumentRequest.Status.PENDING_REVIEW);
        documentRequests.add(documentRequest);
        businessDetails.setDocumentRequests(documentRequests);
        companyLookupInfo.setBusinessDetailsInfo(businessDetails);
        caseDefinition.setCompanyLookupInfo(companyLookupInfo);

        boolean isDocumentApproved = smeProcessHelper.isDocumentsApproved(delegateExecution);

        assertFalse(isDocumentApproved);
    }

    @Test
    void isApplicationSubmitted_withValidData_returnTrue() {
        var persistedEvents = new ArrayList<PersistedEvent>();
        PersistedEvent persistedEvent = new PersistedEvent();
        persistedEvent.setEventName(CaseDefinitionConstants.APPLICATION_SUBMITTED_EVENT);
        persistedEvents.add(persistedEvent);

        when(persistedEventRepository.filterEvents(any(), any())).thenReturn(persistedEvents);

        boolean isApplicationSubmitted = smeProcessHelper.isApplicationSubmitted(delegateExecution);

        assertTrue(isApplicationSubmitted);
    }

    @Test
    void isApplicationSubmitted_withInvalidData_returnFalse() {
        var persistedEvents = new ArrayList<PersistedEvent>();
        var persistedEvent = new PersistedEvent();
        persistedEvent.setEventName("EventName");
        persistedEvents.add(persistedEvent);

        when(persistedEventRepository.filterEvents(any(), any())).thenReturn(persistedEvents);

        boolean isApplicationSubmitted = smeProcessHelper.isApplicationSubmitted(delegateExecution);

        assertFalse(isApplicationSubmitted);
    }

    @Test
    void amlKybSucceeded_withValidData_returnTrue() {
        var amlInfo = new AmlInfo();
        amlInfo.setStatus(Status.SUCCESS);
        caseDefinition.getCompanyLookupInfo().setBusinessDetailsInfo(new BusinessDetails());
        caseDefinition.getCompanyLookupInfo().getBusinessDetailsInfo().setAntiMoneyLaunderingInfo(amlInfo);

        var amlKybSucceeded = smeProcessHelper.amlKybSucceeded(delegateExecution);

        assertTrue(amlKybSucceeded);
    }

    @Test
    void amlKybSucceeded_withInvalidData_returnFalse() {
        var amlInfo = new AmlInfo();
        amlInfo.setStatus(Status.PENDING);
        caseDefinition.getCompanyLookupInfo().setBusinessDetailsInfo(new BusinessDetails());
        caseDefinition.getCompanyLookupInfo().getBusinessDetailsInfo().setAntiMoneyLaunderingInfo(amlInfo);

        var amlKybSucceeded = smeProcessHelper.amlKybSucceeded(delegateExecution);

        assertFalse(amlKybSucceeded);
    }

    @Test
    void amlKybFailed_withValidData_returnTrue() {
        var amlInfo = new AmlInfo();
        amlInfo.setStatus(Status.FAILED);
        caseDefinition.getCompanyLookupInfo().setBusinessDetailsInfo(new BusinessDetails());
        caseDefinition.getCompanyLookupInfo().getBusinessDetailsInfo().setAntiMoneyLaunderingInfo(amlInfo);

        var amlKybFailed = smeProcessHelper.amlKybFailed(delegateExecution);

        assertTrue(amlKybFailed);
    }

    @Test
    void amlKybFailed_withInvalidData_returnFalse() {
        var amlInfo = new AmlInfo();
        amlInfo.setStatus(Status.PENDING);
        caseDefinition.getCompanyLookupInfo().setBusinessDetailsInfo(new BusinessDetails());
        caseDefinition.getCompanyLookupInfo().getBusinessDetailsInfo().setAntiMoneyLaunderingInfo(amlInfo);

        var amlKybFailed = smeProcessHelper.amlKybFailed(delegateExecution);

        assertFalse(amlKybFailed);
    }

    @Test
    void businessRelationKybSucceeded_whenCompleteStatus_returnTrue() {
        caseDefinition.setBusinessRelations(getBusinessRelationInStatus(BusinessRelationsCaseData.Status.COMPLETE));

        var businessRelationKybComplete = smeProcessHelper.businessRelationKybSucceeded(delegateExecution);

        assertTrue(businessRelationKybComplete);
    }

    @ParameterizedTest
    @EnumSource(value = BusinessRelationsCaseData.Status.class, names = {"PENDING", "IN_REVIEW"})
    void businessRelationKybSucceeded_whenInPendingStatusOrWhenDataInReview_returnFalse(
        BusinessRelationsCaseData.Status status) {
        caseDefinition.setBusinessRelations(getBusinessRelationInStatus(status));

        var businessRelationKybComplete = smeProcessHelper.businessRelationKybSucceeded(delegateExecution);

        assertFalse(businessRelationKybComplete);
    }

    @Test
    void businessRelationKybFailed_whenIncompleteStatus_returnTrue() {
        caseDefinition.setBusinessRelations(getBusinessRelationInStatus(BusinessRelationsCaseData.Status.INCOMPLETE));

        var businessRelationKybIncomplete = smeProcessHelper.businessRelationKybFailed(delegateExecution);

        assertTrue(businessRelationKybIncomplete);
    }

    @ParameterizedTest
    @EnumSource(value = BusinessRelationsCaseData.Status.class, names = {"PENDING", "IN_REVIEW"})
    void businessRelationKybFailed_whenInPendingStatusOrWhenDataInReview_returnFalse(
        BusinessRelationsCaseData.Status status) {
        caseDefinition.setBusinessRelations(getBusinessRelationInStatus(status));

        var businessRelationKybIncomplete = smeProcessHelper.businessRelationKybFailed(delegateExecution);

        assertFalse(businessRelationKybIncomplete);
    }

    @Test
    void termsAccepted_withValidData_returnTrue() {
        var termsAndConditions = new TermsAndConditions();
        termsAndConditions.setAccepted(true);
        caseDefinition.setTermsAndConditions(termsAndConditions);

        var termsAccepted = smeProcessHelper.termsAccepted(delegateExecution);

        assertTrue(termsAccepted);
    }

    @Test
    void termsAccepted_withInvalidData_returnFalse() {
        var termsAndConditions = new TermsAndConditions();
        termsAndConditions.setAccepted(false);
        caseDefinition.setTermsAndConditions(termsAndConditions);

        var termsAccepted = smeProcessHelper.termsAccepted(delegateExecution);

        assertFalse(termsAccepted);
    }

    @Test
    void isAdditionalApplicant_withValidData_returnTrue() {
        caseDefinition.setBusinessRelationRequired(null);

        var isAdditionalApplicant = smeProcessHelper.isAdditionalApplicant(delegateExecution);

        assertTrue(isAdditionalApplicant);
    }

    @Test
    void isAdditionalApplicant_withValidData_returnFalse() {
        caseDefinition.setBusinessRelationRequired(null);
        caseDefinition.getApplicants().forEach(applicant -> applicant.setIsRegistrar(true));

        var isAdditionalApplicant = smeProcessHelper.isAdditionalApplicant(delegateExecution);

        assertFalse(isAdditionalApplicant);
    }

    @Test
    void isBusinessRelationFinished_whenBusinessRelationsRequiredIsUnknown_shouldReturnFalse() {
        caseDefinition.setBusinessRelationRequired(null);

        var evaluationResult = smeProcessHelper.isBusinessRelationDataProvided(delegateExecution);

        assertThat(evaluationResult).isFalse();
    }

    @Test
    void isBusinessRelationDataProvided_whenBusinessRelationsIsNotRequired_shouldReturnTrue() {
        caseDefinition.setBusinessRelationRequired(false);

        var evaluationResult = smeProcessHelper.isBusinessRelationDataProvided(delegateExecution);

        assertThat(evaluationResult).isTrue();
    }

    @ParameterizedTest
    @MethodSource("businessRelationsDataNotProvidedStatuses")
    @NullSource
    void isBusinessRelationDataProvided_whenBusinessRelationsIsRequiredAndDataIsNotProvided_shouldReturnFalse(
        BusinessRelationsCaseData businessRelationsCaseData
    ) {
        caseDefinition.setBusinessRelationRequired(true);
        caseDefinition.setBusinessRelations(businessRelationsCaseData);

        var evaluationResult = smeProcessHelper.isBusinessRelationDataProvided(delegateExecution);

        assertThat(evaluationResult).isFalse();
    }

    private static Stream<Arguments> businessRelationsDataNotProvidedStatuses() {
        return Stream.of(BusinessRelationsCaseData.Status.values())
            .filter(Stream.of(hasInReviewStatus, hasCompleteStatus).reduce(x -> false, Predicate::and))
            .map(status -> new BusinessRelationsCaseData().withStatus(status))
            .map(Arguments::of);
    }

    @Test
    void isBusinessRelationDataProvided_whenBusinessRelationsIsRequiredAndDataIsProvided_shouldReturnTrue() {
        caseDefinition.setBusinessRelationRequired(true);
        caseDefinition.setBusinessRelations(
            new BusinessRelationsCaseData().withStatus(BusinessRelationsCaseData.Status.IN_REVIEW));

        var evaluationResult = smeProcessHelper.isBusinessRelationDataProvided(delegateExecution);

        assertThat(evaluationResult).isTrue();
    }

    @Test
    void isBusinessRelationComplete_withValidData_returnTrue() {
        var businessRelationsCaseData = new BusinessRelationsCaseData();
        businessRelationsCaseData.setStatus(BusinessRelationsCaseData.Status.COMPLETE);
        caseDefinition.setBusinessRelationRequired(true);
        caseDefinition.setBusinessRelations(businessRelationsCaseData);

        var isBusinessRelationComplete = smeProcessHelper.isBusinessRelationComplete(delegateExecution);

        assertTrue(isBusinessRelationComplete);
    }

    @Test
    void isBusinessRelationComplete_withValidData_returnFalse() {
        var businessRelationsCaseData = new BusinessRelationsCaseData();
        businessRelationsCaseData.setStatus(BusinessRelationsCaseData.Status.IN_REVIEW);
        caseDefinition.setBusinessRelations(businessRelationsCaseData);

        boolean isBusinessRelationComplete = smeProcessHelper.isBusinessRelationComplete(delegateExecution);

        assertFalse(isBusinessRelationComplete);
    }

    @Test
    void isBusinessRelationComplete_withInvalidData_returnFalse() {
        caseDefinition.setBusinessRelations(null);

        var isBusinessRelationComplete = smeProcessHelper.isBusinessRelationComplete(delegateExecution);

        assertFalse(isBusinessRelationComplete);
    }

    @Test
    void amlKycSucceeded_withValidData_returnTrue() {
        var registrar = caseDefinition.getApplicants().stream().filter(Applicant::getIsRegistrar).findFirst()
            .orElseThrow();
        var amlInfo = new AmlInfo();
        amlInfo.setStatus(Status.SUCCESS);
        registrar.setAntiMoneyLaunderingInfo(amlInfo);

        var isKycSucceeded = smeProcessHelper.amlKycSucceeded(delegateExecution, registrar.getId());

        assertTrue(isKycSucceeded);
    }

    @Test
    void amlKycSucceeded_withValidData_returnFalse() {
        var registrar = caseDefinition.getApplicants().stream().filter(Applicant::getIsRegistrar).findFirst()
            .orElseThrow();
        var amlInfo = new AmlInfo();
        amlInfo.setStatus(Status.FAILED);
        registrar.setAntiMoneyLaunderingInfo(amlInfo);

        var isKycSucceeded = smeProcessHelper.amlKycSucceeded(delegateExecution, registrar.getId());

        assertFalse(isKycSucceeded);
    }

    @Test
    void amlKycSucceeded_withInvalidData_throwsException() {
        var userId = UUID.randomUUID().toString();
        assertThrows(NoSuchElementException.class, () -> smeProcessHelper.amlKycSucceeded(delegateExecution, userId));
    }

    @Test
    void amlKycFailed_withValidData_returnTrue() {
        var registrar = caseDefinition.getApplicants().stream().filter(Applicant::getIsRegistrar).findFirst()
            .orElseThrow();
        var amlInfo = new AmlInfo();
        amlInfo.setStatus(Status.FAILED);
        registrar.setAntiMoneyLaunderingInfo(amlInfo);

        var isKycFailed = smeProcessHelper.amlKycFailed(delegateExecution, registrar.getId());

        assertTrue(isKycFailed);
    }

    @Test
    void amlKycFailed_withValidData_returnFalse() {
        var registrar = caseDefinition.getApplicants().stream().filter(Applicant::getIsRegistrar).findFirst()
            .orElseThrow();
        var amlInfo = new AmlInfo();
        amlInfo.setStatus(Status.SUCCESS);
        registrar.setAntiMoneyLaunderingInfo(amlInfo);

        var isKycFailed = smeProcessHelper.amlKycFailed(delegateExecution, registrar.getId());

        assertFalse(isKycFailed);
    }

    @Test
    void amlKycFailed_withInvalidData_htrowsException() {
        var userId = UUID.randomUUID().toString();
        assertThrows(NoSuchElementException.class, () -> smeProcessHelper.amlKycFailed(delegateExecution, userId));
    }

    @Test
    void getAdditionalApplicants_withAdditionalApplicants_returnList() {
        var additionalApplicants = smeProcessHelper.getAdditionalApplicants(delegateExecution);

        assertNotNull(additionalApplicants);
    }

    @Test
    void getAdditionalApplicants_withoutAdditionalApplicants_returnEmptyList() {
        caseDefinition.setApplicants(caseDefinition.getApplicants().stream().filter(Applicant::getIsRegistrar).collect(
            Collectors.toList()));
        var additionalApplicants = smeProcessHelper.getAdditionalApplicants(delegateExecution);

        assertTrue(additionalApplicants.isEmpty());
    }

    @Test
    void getRegistrarId_withValidData_returnId() {
        var registrar = caseDefinition.getApplicants().stream().filter(Applicant::getIsRegistrar).findFirst()
            .orElseThrow();
        registrar.setPersonalAddress(new Address());

        var id = smeProcessHelper.getRegistrarId(delegateExecution);

        assertEquals(registrar.getId(), id);
    }

    @Test
    void getRegistrarId_withoutRegistrar_throwsException() {
        caseDefinition.setApplicants(caseDefinition.getApplicants().stream()
            .filter(applicant -> !applicant.getIsRegistrar()).collect(Collectors.toList()));

        assertThrows(NoSuchElementException.class, () -> smeProcessHelper.getRegistrarId(delegateExecution));
    }

    @Test
    void readyForDocumentGathering_withValidData_returnTrue() {

        var industry = new Industry();
        var industryList = new ArrayList<Industry>();
        industry.setCode(TRANSPORTATION_AND_WAREHOUSING_GENERAL);
        industryList.add(industry);
        caseDefinition.getCompanyLookupInfo().getBusinessStructureInfo().setType(Type.SOLE_PROPRIETORSHIP);
        caseDefinition.getCompanyLookupInfo().setBusinessIdentityInfo(new BusinessIdentityInfo());
        caseDefinition.getCompanyLookupInfo().setBusinessDetailsInfo(new BusinessDetails());
        caseDefinition.getCompanyLookupInfo().getBusinessIdentityInfo().setIndustries(industryList);
        caseDefinition.getCompanyLookupInfo().getBusinessDetailsInfo().setStateOperatingIn("TX");
        var isAdditionalApplicant = smeProcessHelper.readyForDocumentGathering(delegateExecution);
        assertTrue(isAdditionalApplicant);
    }

    @Test
    void readyForDocumentGathering_withOtherBusinessType_returnTrue() {

        var industry = new Industry();
        var industryList = new ArrayList<Industry>();
        industry.setCode(TRANSPORTATION_AND_WAREHOUSING_GENERAL);
        industryList.add(industry);
        caseDefinition.getCompanyLookupInfo().getBusinessStructureInfo().setType(Type.PARTNERSHIP);
        caseDefinition.getCompanyLookupInfo().getBusinessStructureInfo().setSubtype(Subtype.GENERAL);
        caseDefinition.getCompanyLookupInfo().setBusinessIdentityInfo(new BusinessIdentityInfo());
        caseDefinition.getCompanyLookupInfo().setBusinessDetailsInfo(new BusinessDetails());
        caseDefinition.getCompanyLookupInfo().getBusinessIdentityInfo().setIndustries(industryList);
        caseDefinition.getCompanyLookupInfo().getBusinessDetailsInfo().setStateOperatingIn("TX");
        boolean isAdditionalApplicant = smeProcessHelper.readyForDocumentGathering(delegateExecution);
        assertTrue(isAdditionalApplicant);
    }

    @Test
    void readyForDocumentGathering_withInvalidData_returnFalse() {
        caseDefinition.getCompanyLookupInfo().setBusinessStructureInfo(null);
        var isAdditionalApplicant = smeProcessHelper.readyForDocumentGathering(delegateExecution);
        assertFalse(isAdditionalApplicant);
    }

    @Test
    void riskAssessmentApproved_withValidData_returnTrue() {
        caseDefinition.getRiskAssessment().setStatus(RiskAssessmentCaseData.Status.APPROVED);
        var riskAssessmentApproved = smeProcessHelper.riskAssessmentApproved(delegateExecution);
        assertTrue(riskAssessmentApproved);
    }

    @Test
    void riskAssessmentApproved_withInvalidData_returnFalse() {
        caseDefinition.getRiskAssessment().setStatus(RiskAssessmentCaseData.Status.REJECTED);
        var riskAssessmentApproved = smeProcessHelper.riskAssessmentApproved(delegateExecution);
        assertFalse(riskAssessmentApproved);
    }

    @Test
    void riskAssessmentRejected_withValidData_returnTrue() {
        caseDefinition.getRiskAssessment().setStatus(RiskAssessmentCaseData.Status.REJECTED);
        var riskAssessmentRejected = smeProcessHelper.riskAssessmentRejected(delegateExecution);
        assertTrue(riskAssessmentRejected);
    }

    @Test
    void riskAssessmentRejected_withInvalidData_returnFalse() {
        caseDefinition.getRiskAssessment().setStatus(RiskAssessmentCaseData.Status.APPROVED);
        var riskAssessmentRejected = smeProcessHelper.riskAssessmentRejected(delegateExecution);
        assertFalse(riskAssessmentRejected);
    }

    private BusinessRelationsCaseData getBusinessRelationInStatus(BusinessRelationsCaseData.Status status) {
        var businessRelation = new BusinessRelationsCaseData();
        businessRelation.setStatus(status);
        return businessRelation;
    }
}
