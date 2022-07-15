package com.backbase.flow.sme.onboarding.it;

import static com.backbase.flow.process.ProcessConstants.PROCESS_VARIABLE_CASE_KEY;
import static com.backbase.flow.service.companylookup.infrastructure.BusinessDetailsHandler.BUSINESS_DETAILS;
import static com.backbase.flow.service.companylookup.infrastructure.BusinessIdentityHandler.BUSINESS_IDENTITY_NAME;
import static com.backbase.flow.service.companylookup.infrastructure.BusinessStructureHandler.BUSINESS_STRUCTURE;
import static com.backbase.flow.service.companylookup.infrastructure.CompanyDetailsHandler.COMPANY_DETAILS;
import static com.backbase.flow.service.companylookup.infrastructure.CompanyLookupHandler.COMPANY_LOOKUP;
import static com.backbase.flow.sme.onboarding.TestConstants.ADDRESS_VALIDATION_IDT;
import static com.backbase.flow.sme.onboarding.TestConstants.AML_PROCESS_ID;
import static com.backbase.flow.sme.onboarding.TestConstants.AML_REVIEW_ACTIVITY;
import static com.backbase.flow.sme.onboarding.TestConstants.BUSINESS_OWNERS;
import static com.backbase.flow.sme.onboarding.TestConstants.BUSINESS_RELATION_PROCESS_ID;
import static com.backbase.flow.sme.onboarding.TestConstants.BUSINESS_RELATION_REVIEW_INFORMATION_TASK_ID;
import static com.backbase.flow.sme.onboarding.TestConstants.BUSINESS_SUMMARY;
import static com.backbase.flow.sme.onboarding.TestConstants.CHECK_BUSINESS_RELATION_AND_DOCUMENT_REQUESTS_CONDITIONS;
import static com.backbase.flow.sme.onboarding.TestConstants.COMPLETE_BUSINESS_OWNER_STEP;
import static com.backbase.flow.sme.onboarding.TestConstants.COMPLETE_SUMMARY_STEP;
import static com.backbase.flow.sme.onboarding.TestConstants.COMPLETE_TASK;
import static com.backbase.flow.sme.onboarding.TestConstants.CONTROL_PERSONS;
import static com.backbase.flow.sme.onboarding.TestConstants.DOCUMENT_REQUEST_JOURNEY;
import static com.backbase.flow.sme.onboarding.TestConstants.GET_BUSINESS_PERSONS;
import static com.backbase.flow.sme.onboarding.TestConstants.GET_BUSINESS_ROLES;
import static com.backbase.flow.sme.onboarding.TestConstants.GET_PRODUCT_LIST;
import static com.backbase.flow.sme.onboarding.TestConstants.IDENTITY_VERIFICATION_INITIATION;
import static com.backbase.flow.sme.onboarding.TestConstants.IDENTITY_VERIFICATION_RESULT;
import static com.backbase.flow.sme.onboarding.TestConstants.LOAD_DOCUMENT_REQUESTS;
import static com.backbase.flow.sme.onboarding.TestConstants.MANUAL_REVIEW;
import static com.backbase.flow.sme.onboarding.TestConstants.OTP_VERIFICATION;
import static com.backbase.flow.sme.onboarding.TestConstants.POTENTIAL_MATCH;
import static com.backbase.flow.sme.onboarding.TestConstants.REQUEST_OTP;
import static com.backbase.flow.sme.onboarding.TestConstants.REVIEW_APPLICATION;
import static com.backbase.flow.sme.onboarding.TestConstants.REVIEW_DOCUMENTS_TASK;
import static com.backbase.flow.sme.onboarding.TestConstants.RISK_ASSESSMENT_PROCESS_ID;
import static com.backbase.flow.sme.onboarding.TestConstants.SELECT_CONTROL_PERSON;
import static com.backbase.flow.sme.onboarding.TestConstants.SELECT_PRODUCTS;
import static com.backbase.flow.sme.onboarding.TestConstants.SELECT_RELATION_TYPE;
import static com.backbase.flow.sme.onboarding.TestConstants.SME_ONBOARDING_CHECK_CASE;
import static com.backbase.flow.sme.onboarding.TestConstants.SME_ONBOARDING_CHECK_CASE_EXIST;
import static com.backbase.flow.sme.onboarding.TestConstants.SME_ONBOARDING_INIT;
import static com.backbase.flow.sme.onboarding.TestConstants.SME_ONBOARDING_LANDING;
import static com.backbase.flow.sme.onboarding.TestConstants.SME_ONBOARDING_LANDING_DATA;
import static com.backbase.flow.sme.onboarding.TestConstants.SUBMIT_ADDRESS;
import static com.backbase.flow.sme.onboarding.TestConstants.SUBMIT_DOCUMENT_REQUESTS;
import static com.backbase.flow.sme.onboarding.TestConstants.SUBMIT_SSN;
import static com.backbase.flow.sme.onboarding.TestConstants.UPDATE_CURRENT_USER_OWNER;
import static com.backbase.flow.sme.onboarding.TestConstants.UPDATE_CURRENT_USER_OWNER_STEP;
import static com.backbase.flow.sme.onboarding.TestConstants.UPDATE_OWNER;
import static com.backbase.flow.sme.onboarding.TestConstants.VERIFY_OTP;
import static com.backbase.flow.sme.onboarding.casedata.BusinessStructureInfo.Subtype.MULTIPLE_MEMBER;
import static com.backbase.flow.sme.onboarding.constants.ProcessConstants.BPM_DATA_GATHERING;
import static com.backbase.flow.sme.onboarding.data.PdfGenerationJourneyReader.FILESET_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.assertj.core.util.DateUtil.parseDatetime;
import static org.camunda.bpm.engine.history.HistoricProcessInstance.STATE_COMPLETED;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

import com.backbase.flow.document.api.model.FlowFilerefDto;
import com.backbase.flow.integration.domain.companylookup.CompanyLookup;
import com.backbase.flow.integration.domain.companylookup.dto.BusinessStructureDto;
import com.backbase.flow.integration.service.customer.interaction.handler.document.upload.UploadDocumentRequestDto;
import com.backbase.flow.integration.service.customer.interaction.handler.documentrequest.completetask.CompleteTaskDto;
import com.backbase.flow.integration.service.customer.interaction.handler.fileset.DocumentRequestFilerefDto;
import com.backbase.flow.integration.service.customer.interaction.handler.fileset.DocumentRequestFilesetDto;
import com.backbase.flow.integration.service.data.DocumentRequest;
import com.backbase.flow.integration.service.data.DocumentRequestData;
import com.backbase.flow.integration.service.process.manual.task.ReviewDocumentsDto;
import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.interaction.api.model.InteractionResponseDto;
import com.backbase.flow.service.aml.process.dto.ReviewAmlUserRequest;
import com.backbase.flow.service.businessrelations.interaction.dto.BusinessPersonResponse;
import com.backbase.flow.service.businessrelations.interaction.dto.CompleteBusinessOwnerStepResponse;
import com.backbase.flow.service.businessrelations.interaction.dto.CompleteSummaryRequest;
import com.backbase.flow.service.businessrelations.interaction.dto.RoleResponse;
import com.backbase.flow.service.businessrelations.interaction.dto.SelectControlPersonRequest;
import com.backbase.flow.service.businessrelations.interaction.dto.SelectRelationTypeRequest;
import com.backbase.flow.service.businessrelations.interaction.dto.SelectRelationTypeStepResponse;
import com.backbase.flow.service.businessrelations.interaction.dto.UpdateCurrentUserOwnerRequest;
import com.backbase.flow.service.businessrelations.interaction.dto.UpdateOwnerRequest;
import com.backbase.flow.service.businessrelations.interaction.dto.UpdateOwnerResponse;
import com.backbase.flow.service.businessrelations.process.dto.ReviewInformationRequest;
import com.backbase.flow.service.companylookup.domain.BusinessDetailsDto;
import com.backbase.flow.service.companylookup.domain.CompanyDetailsRequestDto;
import com.backbase.flow.service.dto.AddressDto;
import com.backbase.flow.service.dto.OtpChannel;
import com.backbase.flow.service.dto.OtpDto;
import com.backbase.flow.service.identityverification.casedata.IdentityVerificationResult;
import com.backbase.flow.service.productselection.interaction.dto.SelectProductRequest;
import com.backbase.flow.service.riskassessment.process.dto.ReviewApplicationUserRequest;
import com.backbase.flow.sme.onboarding.BaseIntegrationIT;
import com.backbase.flow.sme.onboarding.casedata.Address;
import com.backbase.flow.sme.onboarding.casedata.AmlInfo;
import com.backbase.flow.sme.onboarding.casedata.Applicant;
import com.backbase.flow.sme.onboarding.casedata.BusinessIdentityInfo;
import com.backbase.flow.sme.onboarding.casedata.BusinessInfo;
import com.backbase.flow.sme.onboarding.casedata.BusinessRelationsCaseData;
import com.backbase.flow.sme.onboarding.casedata.BusinessStructureInfo;
import com.backbase.flow.sme.onboarding.casedata.CompanyLookupInfo;
import com.backbase.flow.sme.onboarding.casedata.Industry;
import com.backbase.flow.sme.onboarding.casedata.PeopleInfo;
import com.backbase.flow.sme.onboarding.casedata.ProductSelection;
import com.backbase.flow.sme.onboarding.casedata.ReviewApprovedReason;
import com.backbase.flow.sme.onboarding.casedata.ReviewInfo;
import com.backbase.flow.sme.onboarding.casedata.RiskAssessmentCaseData;
import com.backbase.flow.sme.onboarding.casedata.RiskAssessmentDataResult;
import com.backbase.flow.sme.onboarding.casedata.SelectedProduct;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import com.backbase.flow.sme.onboarding.constants.ProcessConstants;
import com.backbase.flow.sme.onboarding.interaction.model.AnchorRequestDto;
import com.backbase.flow.sme.onboarding.interaction.model.CaseDataDto;
import com.backbase.flow.sme.onboarding.interaction.model.CaseResponseDto;
import com.backbase.flow.sme.onboarding.interaction.model.SsnDto;
import com.backbase.flow.sme.onboarding.interaction.model.TermsConditionsRequestDto;
import com.backbase.flow.sme.onboarding.interaction.model.TermsConditionsResponseDto;
import com.backbase.flow.sme.onboarding.interaction.utils.SmeUtils;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.SoftAssertions;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstanceQuery;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;

abstract class InteractionIT<S extends Enum<S>> extends BaseIntegrationIT {

    protected static final String FIRST_NAME = "John";
    protected static final String LAST_NAME = "Doe";
    protected static final LocalDate DATE_OF_BIRTH = LocalDate.now().minusYears(21);
    protected static final String EMAIL = "john@doe.com";
    protected static final String PHONE_NUMBER = "12345678901";
    protected static final String SSN = "012345678";
    protected static final String LEGAL_NAME = "potential_match";

    protected static final String ADDITIONAL_OWNER_FIRST_NAME = "Marry";
    protected static final String ADDITIONAL_OWNER_LAST_NAME = "McKenzie";
    protected static final String ADDITIONAL_OWNER_EMAIL = "marry@mckenzie.com";
    protected static final String ADDITIONAL_OWNER_PHONE_NUMBER = "+12345678902";

    private static final String OTP_CHANNEL = OtpChannel.EMAIL.toString();

    private static final String COMMENT = "comment";
    private static final String COMPANY_NAME = "backbase-1";
    private static final String COUNTRY_CODE = "US";
    private static final String JURISDICTION_CODE = "US-AL";
    private static final String HIGH = "high";
    private static final String LOW = "low";
    private static final String MATCH_TYPE = "matchType";
    private static final String PRODUCT_ITEM_REFERENCE_ID = "product-1";
    private static final String PRODUCT_ITEM_NAME = "Business Account 1";
    private static final SelectProductRequest.ProductItem PRODUCT_ITEM = new SelectProductRequest.ProductItem(
        PRODUCT_ITEM_REFERENCE_ID, PRODUCT_ITEM_NAME, null);

    private static final String UNDEFINED = "undefined";
    private static final String COMPANY_LOOKUP_STATE = "US-AL";
    private static final String ADDRESS_STATE = "AL";
    private static final Date DATE_ESTABLISHED = Date.from(
        LocalDate.now().minusYears(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    private static final AddressDto BUSINESS_ADDRESS = new AddressDto()
        .withApt("businessApt")
        .withCity("businessCity")
        .withState(ADDRESS_STATE)
        .withNumberAndStreet("businessNumberAndStreet")
        .withZipCode("12345")
        .withAddressName("Business");
    private static final AddressDto PERSONAL_ADDRESS = new AddressDto()
        .withApt("personalApt")
        .withCity("personalCity")
        .withState(ADDRESS_STATE)
        .withNumberAndStreet("personalNumberAndStreet")
        .withZipCode("12345")
        .withAddressName("Personal");
    protected static final List<Industry> BUSINESS_INDUSTRIES_WITHOUT_LICENSE =
        List.of(
            new Industry().withCode("11").withDescription("Agriculture, Forestry, Fishing and Hunting"),
            new Industry().withCode("22").withDescription("Utilities")
        );
    protected static final List<Industry> BUSINESS_INDUSTRIES_WITH_LICENSE =
        List.of(
            new Industry().withCode("21").withDescription("Mining, Quarrying, and Oil and Gas Extraction")
        );

    protected static final BusinessIdentityInfo BUSINESS_IDENTITY_WITHOUT_LICENSE =
        new BusinessIdentityInfo()
            .withDescription("businessDescription")
            .withWebsite("http://business.website.com")
            .withIndustries(BUSINESS_INDUSTRIES_WITHOUT_LICENSE);

    protected static final BusinessIdentityInfo BUSINESS_IDENTITY_WITH_LICENSE =
        new BusinessIdentityInfo()
            .withDescription("businessDescription")
            .withWebsite("http://business.website.com")
            .withIndustries(BUSINESS_INDUSTRIES_WITH_LICENSE);

    protected static final AnchorRequestDto APPLICANT =
        AnchorRequestDto.builder()
            .firstName(FIRST_NAME)
            .lastName(LAST_NAME)
            .dateOfBirth(DATE_OF_BIRTH)
            .emailAddress(EMAIL)
            .phoneNumber(PHONE_NUMBER)
            .build();

    protected static final SelectRelationTypeRequest RELATION_TYPE_OWNER =
        new SelectRelationTypeRequest(SelectRelationTypeRequest.RelationType.OWNER);

    private static final String APPROVED_VERIFICATION_ID = "approved";

    private static final BusinessStructureDto SOLE_PROPRIETORSHIP =
        new BusinessStructureDto(BusinessStructureInfo.Type.SOLE_PROPRIETORSHIP.value(), List.of());
    private static final BusinessStructureDto PARTNERSHIP =
        new BusinessStructureDto(BusinessStructureInfo.Type.PARTNERSHIP.value(), List.of(
            BusinessStructureInfo.Subtype.LIMITED.value(),
            BusinessStructureInfo.Subtype.GENERAL.value(),
            BusinessStructureInfo.Subtype.JOINT_VENTURE.value()));
    private static final BusinessStructureDto LLC =
        new BusinessStructureDto(BusinessStructureInfo.Type.LLC.value(), List.of(
            BusinessStructureInfo.Subtype.SINGLE_MEMBER.value(),
            BusinessStructureInfo.Subtype.MULTIPLE_MEMBER.value()));
    private static final BusinessStructureDto CORPORATION =
        new BusinessStructureDto(BusinessStructureInfo.Type.CORPORATION.value(), List.of(
            BusinessStructureInfo.Subtype.S_CORP.value(),
            BusinessStructureInfo.Subtype.C_CORP.value(),
            BusinessStructureInfo.Subtype.B_CORP.value(),
            BusinessStructureInfo.Subtype.NON_PROFIT.value(),
            BusinessStructureInfo.Subtype.CLOSE_CORP.value()));

    private static final Map<BusinessStructureInfo.Type, BusinessStructureDto> BUSINESS_STRUCTURES = Map.of(
        BusinessStructureInfo.Type.SOLE_PROPRIETORSHIP, SOLE_PROPRIETORSHIP,
        BusinessStructureInfo.Type.PARTNERSHIP, PARTNERSHIP,
        BusinessStructureInfo.Type.LLC, LLC,
        BusinessStructureInfo.Type.CORPORATION, CORPORATION);

    @Setter
    protected UUID caseKey;

    @Setter
    protected String interactionId;

    abstract List<StepAction<S>> happyPath();

    abstract String interactionName();

    protected InteractionResponseDto performWalkthroughRequest(String expectedStep) {
        var interactionResponse =
            action(SME_ONBOARDING_INIT, interactionId, new TermsConditionsRequestDto(true));
        checkInteractionResponse(interactionResponse, expectedStep);
        setInteractionId(interactionResponse.getInteractionId());
        return interactionResponse;
    }

    protected void checkWalkthrough(InteractionResponseDto interactionResponse) {
        var onboardingInitResponse = mapToObject(interactionResponse.getBody(), TermsConditionsResponseDto.class);
        setCaseKey(UUID.fromString(onboardingInitResponse.getCaseKey()));
        assertThat(onboardingInitResponse.isAccepted()).isTrue();
        assertThat(onboardingInitResponse.isAnchorDataReceived()).isFalse();
    }

    protected void performAnchorDataRequest(String firstName) {
        var anchorDataRequest = AnchorRequestDto.builder()
            .firstName(firstName)
            .lastName(LAST_NAME)
            .dateOfBirth(DATE_OF_BIRTH)
            .emailAddress(EMAIL)
            .phoneNumber(PHONE_NUMBER)
            .build();
        var interactionResponse = performAnchorDataRequest(anchorDataRequest);
        checkInteractionResponse(interactionResponse, OTP_VERIFICATION);
    }

    protected InteractionResponseDto performAnchorDataRequest(AnchorRequestDto anchorDataRequest) {
        var interactionResponse = action("sme-onboarding-anchor-data", interactionId, anchorDataRequest);
        var caseDataDto = mapToObject(interactionResponse.getBody(), CaseDataDto.class);
        Optional.ofNullable(caseDataDto)
            .map(CaseDataDto::getCaseKey)
            .ifPresent(this::setCaseKey);
        var interactionId = interactionResponse.getInteractionId();
        setInteractionId(interactionId);
        return interactionResponse;
    }

    protected void checkIncorrectAnchorData(AnchorRequestDto anchorDataRequest, String expectedStep) {
        var interactionResponse = performAnchorDataRequest(anchorDataRequest);
        checkError(interactionResponse, expectedStep,
            "firstName", "lastName", "dateOfBirth", "emailAddress", "phoneNumber");
    }

    protected void checkAnchorData(String firstName) {
        var aCase = getCase(caseKey);
        assertThat(aCase.isPreliminary()).isTrue();
        var applicants = aCase.getCaseData(SmeCaseDefinition.class)
            .getApplicants();
        assertThat(applicants).hasSize(1);
        var applicant = applicants.get(0);
        assertSoftly(softly -> {
            softly.assertThat(applicant)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(
                    new Applicant()
                        .withFirstName(firstName)
                        .withLastName(LAST_NAME)
                        .withDateOfBirth(DATE_OF_BIRTH)
                        .withEmail(EMAIL)
                        .withPhoneNumber(PHONE_NUMBER)
                        .withIsRegistrar(true)
                );
            softly.assertThat(applicant.getId()).isNotBlank();
        });
    }

    protected void performAvailableOtpChannelsRequest() {
        var interactionResponse = action("available-otp-channels", interactionId);
        checkInteractionResponse(interactionResponse, OTP_VERIFICATION);
        //noinspection unchecked
        var channels = (List<String>) interactionResponse.getBody();
        assertThat(channels).contains(OTP_CHANNEL);
    }

    protected void performFetchOtpDataRequest() {
        var interactionResponse = action("fetch-otp-email", interactionId);
        checkInteractionResponse(interactionResponse, OTP_VERIFICATION);
        var applicant = mapToObject(interactionResponse.getBody(), Applicant.class);
        assertThat(applicant.getEmail()).isEqualTo(EMAIL);
    }

    protected void performRequestOtpRequest() {
        var otpRequest = OtpDto.builder()
            .channel(OTP_CHANNEL)
            .email(EMAIL)
            .build();
        var interactionResponse = action(REQUEST_OTP, interactionId, otpRequest);
        checkInteractionResponse(interactionResponse, OTP_VERIFICATION);
    }

    protected void performVerifyOtpRequest() {
        var otpRequest = OtpDto.builder()
            .channel(OTP_CHANNEL)
            .email(EMAIL)
            .otp("123456")
            .build();
        var interactionResponse = action(VERIFY_OTP, interactionId, otpRequest);
        checkInteractionResponse(interactionResponse, SME_ONBOARDING_CHECK_CASE);
    }

    protected void checkOtp() {
        assertThat(getCaseData(caseKey).getApplicants())
            .hasSize(1)
            .first()
            .usingComparator(
                Comparator.comparing(Applicant::getEmail)
                    .thenComparing(Applicant::getEmailVerified)
            )
            .isEqualTo(
                new Applicant()
                    .withEmail(EMAIL)
                    .withEmailVerified(true)
            );
    }

    protected InteractionResponseDto performCheckCaseRequest() {
        return action(SME_ONBOARDING_CHECK_CASE_EXIST, interactionId);
    }

    protected void checkCheckCaseExists(InteractionResponseDto interactionResponse, String expectedStep,
                                        String expectedFirstName) {
        checkInteractionResponse(interactionResponse, expectedStep);
        var caseResponseDto = mapToObject(interactionResponse.getBody(), CaseResponseDto.class);
        assertThat(caseResponseDto.getFirstName()).isEqualTo(expectedFirstName);
        assertThat(caseResponseDto.getLastName()).isEqualTo(LAST_NAME);
        assertThat(caseResponseDto.isCaseExist()).isFalse();
        assertThat(caseResponseDto.isIdentityCredentialExist()).isFalse();
    }

    protected void performGetProductList() {
        var getProductListRequest = new InteractionRequestDto();
        var interactionResponse = action(GET_PRODUCT_LIST, interactionId, getProductListRequest);
        checkInteractionResponse(interactionResponse, SELECT_PRODUCTS);
    }

    protected void performProductSelectionRequest(String expectedStep) {
        var productSelectionRequest = new SelectProductRequest();
        productSelectionRequest.setSelection(List.of(PRODUCT_ITEM));
        var interactionResponse = action(SELECT_PRODUCTS, interactionId, productSelectionRequest);
        checkInteractionResponse(interactionResponse, expectedStep);
    }

    protected void checkProductSelection() {
        assertThat(getCaseData(caseKey).getProductSelection())
            .isNotNull()
            .extracting(ProductSelection::getSelectedProducts)
            .asList()
            .hasSize(1)
            .first()
            .isEqualTo(new SelectedProduct()
                .withReferenceId(PRODUCT_ITEM.getReferenceId())
                .withProductName(PRODUCT_ITEM.getProductName())
            );
    }

    protected void performGetBusinessStructuresList() {
        var collectionResponse = getCollectionItems("business-structures", HashMap.class);
        checkBusinessStructureCollections(mapToList(collectionResponse, BusinessStructureDto.class));
    }

    protected void checkBusinessStructureCollections(List<BusinessStructureDto> businessStructures) {
        assertThat(businessStructures)
            .hasSize(4)
            .containsExactlyInAnyOrderElementsOf(BUSINESS_STRUCTURES.values());
        assertThat(businessStructures).containsExactlyInAnyOrderElementsOf(BUSINESS_STRUCTURES.values());
    }

    protected void performSoleProprietorshipBusinessStructureRequest(String expectedStep) {
        var businessStructureRequest = BUSINESS_STRUCTURES.get(BusinessStructureInfo.Type.SOLE_PROPRIETORSHIP);
        new BusinessStructureDto(BusinessStructureInfo.Type.SOLE_PROPRIETORSHIP.value(), null);
        var interactionResponse = action(BUSINESS_STRUCTURE, interactionId, businessStructureRequest);
        checkInteractionResponse(interactionResponse, expectedStep);
    }

    protected void performLlcMultipleMemberBusinessStructureRequest() {
        var interactionResponse = action(BUSINESS_STRUCTURE, interactionId, new BusinessStructureInfo()
            .withType(BusinessStructureInfo.Type.LLC)
            .withSubtype(MULTIPLE_MEMBER)
        );
        checkInteractionResponse(interactionResponse, COMPANY_LOOKUP);
    }

    protected String performCompanyLookupRequest() {
        var interactionResponse = action(COMPANY_LOOKUP, interactionId, CompanyLookup.SearchRequest.builder()
            .name(COMPANY_NAME)
            .countryCode(COUNTRY_CODE)
            .jurisdictionCode(JURISDICTION_CODE)
            .build()
        );
        checkInteractionResponse(interactionResponse, COMPANY_LOOKUP);
        return mapToList(interactionResponse.getBody(), CompanyLookup.SearchResponse.class).get(0).getNumber();
    }

    protected void performCompanyDetailsRequest(String companyNumber) {
        var interactionResponse = action(COMPANY_DETAILS, interactionId,
            new CompanyDetailsRequestDto(companyNumber, JURISDICTION_CODE)
        );
        checkInteractionResponse(interactionResponse, BUSINESS_DETAILS);
    }

    protected void checkCompanyLookupWillBeNotExecuted() {
        assertThat(getCaseData(caseKey).getCompanyLookupInfo())
            .isNotNull()
            .extracting(info -> info.getBusinessStructureInfo().getType(), CompanyLookupInfo::getPerformCompanyLookup)
            .isEqualTo(List.of(BusinessStructureInfo.Type.SOLE_PROPRIETORSHIP, false));
    }

    protected void checkCompanyLookupWillBeExecuted() {
        assertThat(getCaseData(caseKey).getCompanyLookupInfo())
            .isNotNull()
            .extracting(info -> info.getBusinessStructureInfo().getType(), CompanyLookupInfo::getPerformCompanyLookup)
            .isEqualTo(List.of(BusinessStructureInfo.Type.LLC, true));
    }

    protected void performBusinessDetailsRequest() {
        var caseData = getCaseData(caseKey);
        var businessStructureInfo = caseData.getCompanyLookupInfo().getBusinessStructureInfo();
        var businessDetailsRequest =
            new BusinessDetailsDto(toCompanyLookupBusinessStructureInfo(businessStructureInfo), LEGAL_NAME, null, null,
                DATE_ESTABLISHED, COMPANY_LOOKUP_STATE);
        var interactionResponse = action(BUSINESS_DETAILS, interactionId, businessDetailsRequest);
        checkInteractionResponse(interactionResponse, "business-address");
    }

    protected void checkBusinessDetails() {
        var caseData = getCaseData(caseKey);
        var businessDetailsInfo = caseData.getCompanyLookupInfo().getBusinessDetailsInfo();
        assertThat(businessDetailsInfo.getLegalName()).isEqualTo(LEGAL_NAME);
        assertThat(parseDatetime(businessDetailsInfo.getDateEstablished())).isEqualToIgnoringMinutes(DATE_ESTABLISHED);
        assertThat(businessDetailsInfo.getStateOperatingIn()).isEqualTo(COMPANY_LOOKUP_STATE);
    }

    protected void checkCompanyDetails(String companyNumber) {
        var caseData = getCaseData(caseKey);
        var apiCompanyDetails = caseData.getCompanyLookupInfo().getApiCompanyDetails();
        assertThat(apiCompanyDetails.getDetails().getCompanyType()).isEqualTo("Public Limited Company");
        assertThat(apiCompanyDetails.getDetails().getEin()).isEqualTo(companyNumber);
        assertThat(apiCompanyDetails.getDetails().getStateOperatingIn()).isEqualTo(COMPANY_LOOKUP_STATE);
        assertThat(apiCompanyDetails.getAddress()).isNotNull();
        assertThat(apiCompanyDetails.getIndustries()).hasSize(1);
    }

    protected void performBusinessAddressRequest() {
        var interactionResponse = action(SUBMIT_ADDRESS, interactionId, BUSINESS_ADDRESS);
        checkInteractionResponse(interactionResponse, BUSINESS_IDENTITY_NAME);
    }

    protected void checkBusinessAddress() {
        var caseData = getCaseData(caseKey);
        var businessAddressInfo = caseData.getCompanyLookupInfo().getBusinessAddressInfo();
        assertThat(businessAddressInfo).extracting(
                Address::getApt,
                Address::getNumberAndStreet,
                Address::getCity,
                Address::getState,
                Address::getZipCode)
            .isEqualTo(List.of(
                BUSINESS_ADDRESS.getApt(),
                BUSINESS_ADDRESS.getNumberAndStreet(),
                BUSINESS_ADDRESS.getCity(),
                BUSINESS_ADDRESS.getState(),
                BUSINESS_ADDRESS.getZipCode()));
    }

    protected void performBusinessIdentityRequest(final BusinessIdentityInfo businessIdentityInfo,
                                                  String expectedStep) {
        var interactionResponse = action(BUSINESS_IDENTITY_NAME, interactionId, businessIdentityInfo);
        checkInteractionResponse(interactionResponse, expectedStep);
    }

    protected void checkBusinessIdentity(final BusinessIdentityInfo expectedBusinessIdentityInfo) {
        var caseData = getCaseData(caseKey);
        var businessIdentityInfo = caseData.getCompanyLookupInfo().getBusinessIdentityInfo();
        assertThat(businessIdentityInfo).isEqualTo(expectedBusinessIdentityInfo);
    }

    protected void performCheckBusinessRelationsAndDocumentRequest(String expectedStep) {
        var interactionResponse = action(CHECK_BUSINESS_RELATION_AND_DOCUMENT_REQUESTS_CONDITIONS, interactionId);
        checkInteractionResponse(interactionResponse, expectedStep);
    }

    protected void checkBusinessRelationsAndDocumentRequest(
        boolean businessRelationsJourneyRequired,
        boolean documentRequestJourneyRequired
    ) {
        final var caseData = getCaseData(caseKey);
        assertThat(caseData.getBusinessRelationRequired()).isEqualTo(businessRelationsJourneyRequired);
        assertThat(caseData.getDocumentRequired()).isEqualTo(documentRequestJourneyRequired);
    }

    protected SelectRelationTypeStepResponse performRelationType() {
        var interactionResponse = action(SELECT_RELATION_TYPE, interactionId, RELATION_TYPE_OWNER);
        checkInteractionResponse(interactionResponse, UPDATE_CURRENT_USER_OWNER_STEP);
        return mapToObject(interactionResponse.getBody(), SelectRelationTypeStepResponse.class);
    }

    protected void checkRelationType(SelectRelationTypeStepResponse relationTypeResponse) {
        final var caseData = getCaseData(caseKey);
        assertThat(caseData.getBusinessRelationRequired()).isTrue();
        assertThat(caseData.getBusinessRelations().getBusinessRelationType().value()).isEqualTo(
            relationTypeResponse.getRelationType());
        assertThat(relationTypeResponse.getMinimumOwnershipPercentage())
            .isEqualTo(serviceItemBusinessRelationsProperties.getMinimumOwnershipPercentage());
    }

    protected List<RoleResponse> performGetBusinessRoles(String expectedStep) {
        var interactionResponse = action(GET_BUSINESS_ROLES, interactionId);
        checkInteractionResponse(interactionResponse, expectedStep);
        return mapToList(interactionResponse.getBody(), RoleResponse.class);
    }

    protected void checkBusinessRoles(List<RoleResponse> businessRoles) {
        assertThat(businessRoles)
            .extracting(RoleResponse::getName)
            .containsExactlyInAnyOrderElementsOf(roleListProvider.getRoles());
    }

    protected List<BusinessPersonResponse> performGetBusinessPersons() {
        var interactionResponse = action(GET_BUSINESS_PERSONS, interactionId);
        checkInteractionResponse(interactionResponse, UPDATE_CURRENT_USER_OWNER_STEP);
        return mapToList(interactionResponse.getBody(), BusinessPersonResponse.class);
    }

    protected BusinessPersonResponse checkBusinessPersons(List<BusinessPersonResponse> businessPersonsResponse) {
        final var caseData = getCaseData(caseKey);
        final var registrar = SmeUtils.getRegistrar(caseData);
        assertThat(businessPersonsResponse).hasSize(1);
        final var businessPerson = businessPersonsResponse.get(0);
        assertSoftly(softly -> {
            softly.assertThat(registrar.getFirstName())
                .isEqualTo(businessPerson.getFirstName());
            softly.assertThat(registrar.getLastName())
                .isEqualTo(businessPerson.getLastName());
            softly.assertThat(registrar.getEmail())
                .isEqualTo(businessPerson.getEmail());
            softly.assertThat(registrar.getPhoneNumber())
                .isEqualTo(businessPerson.getPhone());
            softly.assertThat(registrar.getRole())
                .isEqualTo(businessPerson.getRole());
            softly.assertThat(registrar.getOwnershipPercentage())
                .isEqualTo(businessPerson.getOwnershipPercentage());
            softly.assertThat(registrar.getControlPerson())
                .isEqualTo(businessPerson.isControlPerson());
            softly.assertThat(registrar.getRelationType().value())
                .isEqualTo(businessPerson.getRelationType());
        });
        return businessPerson;
    }

    protected void performUpdateCurrentUserOwner(
        BusinessPersonResponse currentUser,
        CurrentUserData amendedCurrentUserData
    ) {
        var interactionResponse = action(UPDATE_CURRENT_USER_OWNER, interactionId,
            businessPersonToCurrentUser(currentUser, amendedCurrentUserData));
        checkInteractionResponse(interactionResponse, BUSINESS_OWNERS);
    }

    protected void checkCurrentUserOwner(BusinessPersonResponse currentUser, CurrentUserData amendedCurrentUserData) {
        final var caseData = getCaseData(caseKey);
        final var currentUserOwner = SmeUtils.getRegistrar(caseData);

        assertThat(currentUserOwner)
            .extracting(Applicant::getId, Applicant::getRole, Applicant::getOwnershipPercentage)
            .isEqualTo(List.of(
                currentUser.getId(),
                amendedCurrentUserData.getSelectedRole(),
                amendedCurrentUserData.getOwnershipPercentage()));
    }

    protected UpdateOwnerRequest performAddAdditionalBusinessOwner(List<RoleResponse> roles) {
        var additionalOwner = UpdateOwnerRequest.builder()
            .firstName(ADDITIONAL_OWNER_FIRST_NAME)
            .lastName(ADDITIONAL_OWNER_LAST_NAME)
            .email(ADDITIONAL_OWNER_EMAIL)
            .phone(ADDITIONAL_OWNER_PHONE_NUMBER)
            .role(getRandomRole(roles))
            .ownershipPercentage(getRandomOwnershipPercentage())
            .build();
        var interactionResponse = action(UPDATE_OWNER, interactionId, additionalOwner);
        checkInteractionResponse(interactionResponse, BUSINESS_OWNERS);
        var updateOwnerResponse = mapToObject(interactionResponse.getBody(), UpdateOwnerResponse.class);
        additionalOwner.setId(updateOwnerResponse.getId().toString());
        return additionalOwner;
    }

    protected void checkAddAdditionalBusinessOwner(UpdateOwnerRequest additionalOwner) {
        final var caseData = getCaseData(caseKey);
        final var persistedAdditionalOwner = SmeUtils.getApplicant(caseData, additionalOwner.getId());
        assertSoftly(softly -> {
            softly.assertThat(persistedAdditionalOwner.getFirstName())
                .isEqualTo(additionalOwner.getFirstName());
            softly.assertThat(persistedAdditionalOwner.getLastName())
                .isEqualTo(additionalOwner.getLastName());
            softly.assertThat(persistedAdditionalOwner.getEmail())
                .isEqualTo(additionalOwner.getEmail());
            softly.assertThat(persistedAdditionalOwner.getPhoneNumber())
                .isEqualTo(additionalOwner.getPhone());
            softly.assertThat(persistedAdditionalOwner.getRole())
                .isEqualTo(additionalOwner.getRole());
            softly.assertThat(persistedAdditionalOwner.getOwnershipPercentage())
                .isEqualTo(additionalOwner.getOwnershipPercentage());
            softly.assertThat(persistedAdditionalOwner.getControlPerson())
                .isEqualTo(false);
        });
    }

    protected CompleteBusinessOwnerStepResponse performCompleteBusinessOwner() {
        var interactionResponse = action(COMPLETE_BUSINESS_OWNER_STEP, interactionId);
        checkInteractionResponse(interactionResponse, CONTROL_PERSONS);
        return mapToObject(interactionResponse.getBody(), CompleteBusinessOwnerStepResponse.class);
    }

    protected void checkCompleteBusinessOwner(CompleteBusinessOwnerStepResponse completeBusinessOwnerStepResponse) {
        final var caseData = getCaseData(caseKey);
        final var registrar = SmeUtils.getRegistrar(caseData);
        assertThat(completeBusinessOwnerStepResponse.getRelationType())
            .isEqualTo(registrar.getRelationType().value());
        assertThat(registrar.getControlPerson()).isFalse();
    }

    protected void performSelectControlPerson(String controlPersonId) {
        var interactionResponse = action(SELECT_CONTROL_PERSON, interactionId,
            new SelectControlPersonRequest(controlPersonId));
        checkInteractionResponse(interactionResponse, BUSINESS_SUMMARY);
    }

    protected void checkSelectControlPerson() {
        final var caseData = getCaseData(caseKey);
        final var registrar = SmeUtils.getRegistrar(caseData);
        assertThat(registrar.getControlPerson()).isTrue();
    }

    protected void performCompleteSummary(boolean idt, String expectedStep) {
        var interactionResponse = action(COMPLETE_SUMMARY_STEP, interactionId,
            new CompleteSummaryRequest(idt));
        checkInteractionResponse(interactionResponse, expectedStep);
    }

    protected void checkCompleteSummary() {
        final var caseData = getCaseData(caseKey);
        final var businessRelations = caseData.getBusinessRelations();
        assertThat(businessRelations.getStatus()).isEqualTo(BusinessRelationsCaseData.Status.IN_REVIEW);
    }

    @SneakyThrows
    protected void performDocumentUploadRequests() {
        var documentRequest = performLoadDocumentRequest();
        performUploadDocumentRequest(documentRequest);
        performCompleteDocumentUploadTask(documentRequest.getInternalId());
        performReviewDocumentRequest();
        performSubmitDocumentRequest();
    }

    protected void performIdentityVerification(String expectedStep, String interactionId) {
        action(IDENTITY_VERIFICATION_INITIATION, interactionId);
        var identityVerificationResult = new IdentityVerificationResult();
        identityVerificationResult.setVerificationId(APPROVED_VERIFICATION_ID);
        var interactionResponse = action(IDENTITY_VERIFICATION_RESULT, interactionId, identityVerificationResult);
        checkInteractionResponse(interactionResponse, expectedStep);
    }

    protected void checkIdentityVerification() {
        var caseData = getCaseData(caseKey);
        var identityVerificationInitiation = caseData.getIdentityVerificationInitiation();
        assertThat(identityVerificationInitiation).isNotNull();
        assertThat(identityVerificationInitiation.getUserReference()).isNotBlank();
        assertThat(identityVerificationInitiation.getTransactionReference()).isNotBlank();
        final var identityVerificationResult = caseData.getIdentityVerificationResult();
        assertThat(identityVerificationResult).isNotNull();
        assertThat(identityVerificationResult.getVerificationId()).isEqualTo(APPROVED_VERIFICATION_ID);
    }

    protected void performPersonalAddressRequest(String expectedStep, String interactionId) {
        var interactionResponse = action(SUBMIT_ADDRESS, interactionId, PERSONAL_ADDRESS);
        checkInteractionResponse(interactionResponse, expectedStep);
    }

    protected void checkPersonalAddress() {
        var caseData = getCaseData(caseKey);
        var personalAddress = SmeUtils.getRegistrar(caseData).getPersonalAddress();
        assertThat(personalAddress)
            .extracting(
                Address::getApt,
                Address::getNumberAndStreet,
                Address::getCity,
                Address::getState,
                Address::getZipCode)
            .isEqualTo(List.of(
                PERSONAL_ADDRESS.getApt(),
                PERSONAL_ADDRESS.getNumberAndStreet(),
                PERSONAL_ADDRESS.getCity(),
                PERSONAL_ADDRESS.getState(),
                PERSONAL_ADDRESS.getZipCode()));
    }

    protected void checkTasksForPersonalAddressValidation() {
        var tasks = getTasksByCaseKeyAndDefinitionKey(caseKey.toString(), ADDRESS_VALIDATION_IDT);
        assertThat(tasks).isEmpty();
    }

    protected void performSsnRequest() {
        var interactionResponse = action(SUBMIT_SSN, interactionId, new SsnDto(SSN));
        checkInteractionResponse(interactionResponse, SME_ONBOARDING_LANDING);
    }

    protected void performLandingRequest() {
        var interactionResponse = action(SME_ONBOARDING_LANDING_DATA, interactionId);
        checkInteractionResponse(interactionResponse, SME_ONBOARDING_LANDING);
    }

    protected void checkSsn() {
        var caseData = getCaseData(caseKey);
        var registrarApplicant = SmeUtils.getRegistrar(caseData);

        assertThat(registrarApplicant)
            .isNotNull()
            .extracting(Applicant::getSsn)
            .isEqualTo(SSN);
    }

    protected InteractionResponseDto uploadDocument(
        String interactionId, Object payload, InputStream inputStream
    ) {
        return interaction(interactionName(), "upload-document", interactionId, payload,
            IMAGE_JPEG_VALUE, inputStream);
    }

    protected void checkLanding() {
        var caseData = getCaseData(caseKey);

        assertThat(caseData.getIsLanded()).isTrue();
    }

    protected InteractionResponseDto action(String action, String interactionId, Object payload) {
        return interaction(interactionName(), action, interactionId, payload);
    }

    protected InteractionResponseDto action(String action, String interactionId) {
        return action(action, interactionId, null);
    }

    protected void checkProcessIsFinishedSuccessfully() {
        checkProcessIsFinished();
        getAndSaveBusinessDocument(caseKey, FILESET_NAME, String.format("%s-contract.pdf", LEGAL_NAME));
    }

    protected void checkProcessIsFinished() {
        var process = historyService.createHistoricProcessInstanceQuery()
            .processDefinitionKey(BPM_DATA_GATHERING)
            .variableValueEquals(ProcessConstants.PROCESS_VARIABLE_CASE_KEY, caseKey.toString())
            .singleResult();
        assertThat(process.getState())
            .isEqualTo(STATE_COMPLETED);
        assertThat(getCase(caseKey).isArchived())
            .isTrue();
        assertThat(getActiveUserTaskCount(caseKey))
            .isZero();
    }

    protected void happyPathUntil(S step) {
        happyPath().stream()
            .takeWhile(stepAction -> !step.equals(stepAction.getStep()))
            .map(StepAction::getAction)
            .forEach(Runnable::run);
    }

    protected void happyPathAfter(S step) {
        happyPath().stream()
            .dropWhile(stepAction -> !step.equals(stepAction.getStep()))
            .skip(1)
            .map(StepAction::getAction)
            .forEach(Runnable::run);
    }

    protected void happyPathBetween(S afterStep, S untilStep) {
        happyPath().stream()
            .dropWhile(stepAction -> !afterStep.equals(stepAction.getStep()))
            .skip(1)
            .takeWhile(stepAction -> !untilStep.equals(stepAction.getStep()))
            .map(StepAction::getAction)
            .forEach(Runnable::run);
    }

    protected String startIdt(UUID caseKey, String processDefinitionKey, String taskDefinitionKey) {
        var idtInteractions = startIdts(caseKey, processDefinitionKey, taskDefinitionKey);
        assertThat(idtInteractions).hasSize(1);
        return idtInteractions.get(0);
    }

    @SneakyThrows
    protected List<String> startIdts(UUID caseKey, String processDefinitionKey, String taskDefinitionKey) {
        var idtTaskIds = getIdtIds(caseKey, processDefinitionKey, taskDefinitionKey);
        return idtTaskIds.stream()
            .map(this::createTaskAndJobPair)
            .map(this::getInteractionId)
            .collect(Collectors.toList());
    }

    protected void checkEventWasRaised(String eventName) {
        checkEventWasRaised(eventName, 1);
    }

    protected void checkEventWasRaised(String eventName, int times) {
        var events = getEventsByName(caseKey, eventName);
        assertThat(events).hasSize(times);
    }

    protected void checkUserTaskIsActive(String processDefinitionKey, String taskDefinitionKey) {
        checkUserTasksAreActive(processDefinitionKey, taskDefinitionKey, 1);
    }

    protected void checkUserTasksAreActive(
        String processDefinitionKey,
        String taskDefinitionKey,
        int expectedTasksNumber
    ) {
        var activeUserTasks = getActiveUserTasks(processDefinitionKey, taskDefinitionKey);
        assertThat(activeUserTasks).hasSize(expectedTasksNumber);
        activeUserTasks.forEach(this::checkUserTaskIsActive);
    }

    protected void checkUserTaskIsCompleted(String processDefinitionKey, String taskDefinitionKey) {
        checkUserTasksAreCompleted(processDefinitionKey, taskDefinitionKey, 1);
    }

    protected void checkUserTasksAreCompleted(
        String processDefinitionKey,
        String taskDefinitionKey,
        int expectedTasksNumber
    ) {
        var completedUserTasks = getCompletedUserTasks(processDefinitionKey, taskDefinitionKey);
        assertThat(completedUserTasks).hasSize(expectedTasksNumber);
        completedUserTasks.forEach(this::checkUserTaskIsCompleted);
    }

    protected void performCompleteAmlTask() {
        completeAmlUserTask(getAmlReviewTaskId(), ReviewAmlUserRequest.RiskLevel.LOW);
    }

    protected void performFailedAmlTask() {
        completeAmlUserTask(getAmlReviewTaskId(), ReviewAmlUserRequest.RiskLevel.HIGH);
    }

    protected String getAmlReviewTaskId() {
        var amlReviewTask = getActiveUserTask(AML_PROCESS_ID, AML_REVIEW_ACTIVITY);
        assertThat(amlReviewTask).isNotNull();
        return amlReviewTask.getId();
    }

    protected void performCompleteRiskAssessmentTask() {
        var riskAssessmentTask = getActiveUserTask(RISK_ASSESSMENT_PROCESS_ID, REVIEW_APPLICATION);
        assertThat(riskAssessmentTask)
            .isNotNull();
        completeRiskAssessmentTask(riskAssessmentTask.getId());
    }

    protected void performCompleteBusinessRelationReviewTask() {
        var businessRelationReviewTask = getActiveUserTask(BUSINESS_RELATION_PROCESS_ID,
            BUSINESS_RELATION_REVIEW_INFORMATION_TASK_ID);
        assertThat(businessRelationReviewTask)
            .isNotNull();
        completeBusinessRelationTask(businessRelationReviewTask.getId());
    }

    protected void checkRiskAssessmentData(List<Industry> industries) {
        var caseData = getCaseData(caseKey);
        var riskAssessment = caseData.getRiskAssessment();

        assertSoftly(softly -> {
            checkRiskAssessment(riskAssessment, softly);
            checkPeopleInfo(riskAssessment.getPeopleInfo(), softly);
            checkBusinessInfo(riskAssessment.getBusinessInfo(), industries, softly);
            checkReviewInfo(riskAssessment.getReviewInfo(), softly);
        });
    }

    protected void checkAmlUserData() {
        var caseData = getCaseData(caseKey);
        var antiMoneyLaunderingInfo = caseData.getApplicants().get(0).getAntiMoneyLaunderingInfo();
        checkAmlData(antiMoneyLaunderingInfo);
    }

    protected void checkAmlBusinessData() {
        var caseData = getCaseData(caseKey);
        var antiMoneyLaunderingInfo =
            caseData.getCompanyLookupInfo().getBusinessDetailsInfo().getAntiMoneyLaunderingInfo();
        checkAmlData(antiMoneyLaunderingInfo);
    }

    private void checkAmlData(AmlInfo antiMoneyLaunderingInfo) {
        assertSoftly(softly -> {
            softly.assertThat(antiMoneyLaunderingInfo.getStatus())
                .isEqualTo(AmlInfo.Status.SUCCESS);
            softly.assertThat(antiMoneyLaunderingInfo.getReviewNeeded())
                .isEqualTo(true);
            softly.assertThat(antiMoneyLaunderingInfo.getReviewApprovedReason())
                .isEqualTo(new ReviewApprovedReason().withComment(COMMENT).withMatchTypes(List.of(MATCH_TYPE)));
            softly.assertThat(antiMoneyLaunderingInfo.getAmlResult().getMatchStatus())
                .isEqualTo(POTENTIAL_MATCH);
        });
    }

    private void checkRiskAssessment(RiskAssessmentCaseData riskAssessment, SoftAssertions softly) {
        softly.assertThat(riskAssessment.getRiskAssessmentApplicationRiskScore())
            .isEqualTo(10.0);
        softly.assertThat(riskAssessment.getRiskAssessmentApplicationRiskLevel())
            .isEqualTo(HIGH);
        softly.assertThat(riskAssessment.getReviewRequired())
            .isEqualTo(true);
        softly.assertThat(riskAssessment.getStatus())
            .isEqualTo(RiskAssessmentCaseData.Status.APPROVED);
    }

    private void checkPeopleInfo(PeopleInfo peopleInfo, SoftAssertions softly) {
        softly.assertThat(peopleInfo.getPeopleRiskScore())
            .isEqualTo(10.0);
        softly.assertThat(peopleInfo.getPeopleRiskLevel())
            .isEqualTo(HIGH);
    }

    private void checkBusinessInfo(BusinessInfo businessInfo, List<Industry> industries, SoftAssertions softly) {
        softly.assertThat(businessInfo.getBusinessIndustryRiskScore())
            .isEqualTo(1.0);
        softly.assertThat(businessInfo.getBusinessIndustryRiskLevel())
            .isEqualTo(LOW);
        var businessIndustriesResults = businessInfo.getBusinessIndustriesResult();
        softly.assertThat(businessIndustriesResults)
            .hasSameSizeAs(industries);
        for (int i = 0; i < businessIndustriesResults.size(); i++) {
            checkIndustryRiskResult(businessIndustriesResults.get(i), industries.get(i), softly);
        }
        softly.assertThat(businessInfo.getBusinessCountryRegisteredInRiskScore())
            .isEqualTo(0.0);
        softly.assertThat(businessInfo.getBusinessCountryRegisteredInRiskLevel())
            .isEqualTo(UNDEFINED);
        softly.assertThat(businessInfo.getAmlKybRiskScore())
            .isEqualTo(0.0);
        softly.assertThat(businessInfo.getAmlKybRiskLevel())
            .isEqualTo(UNDEFINED);
        softly.assertThat(businessInfo.getTaskAmlKybResult())
            .isEqualTo(UNDEFINED);
        softly.assertThat(businessInfo.getBusinessRiskScore())
            .isEqualTo(1.0);
        softly.assertThat(businessInfo.getBusinessRiskLevel())
            .isEqualTo(LOW);
    }

    private void checkIndustryRiskResult(RiskAssessmentDataResult result, Industry industry, SoftAssertions softly) {
        softly.assertThat(result.getValue())
            .isEqualTo(industry.getDescription());
        softly.assertThat(result.getRiskScore())
            .isEqualTo(1.0);
        softly.assertThat(result.getRiskLevel())
            .isEqualTo(LOW);
        softly.assertThat(result.getSelected())
            .isTrue();
    }

    private void checkReviewInfo(ReviewInfo reviewInfo, SoftAssertions softly) {
        softly.assertThat(reviewInfo.getReviewApproved())
            .isEqualTo(true);
        softly.assertThat(reviewInfo.getReviewApprovedComment())
            .isEqualTo(COMMENT);
        softly.assertThat(reviewInfo.getReviewRejectedComment())
            .isEqualTo(null);
    }

    protected String getRandomRole(List<RoleResponse> businessRoles) {
        var businessRolesNames = businessRoles.stream()
            .map(RoleResponse::getName)
            .filter(name -> !"Other".equals(name))
            .collect(Collectors.toList());
        Collections.shuffle(businessRolesNames);
        return businessRolesNames.get(0);
    }

    protected double getRandomOwnershipPercentage() {
        return ThreadLocalRandom.current().nextDouble(30.0, 45.0);
    }

    protected long getActiveUserTaskCount(UUID caseKey) {
        return taskService.createTaskQuery()
            .processVariableValueEquals(PROCESS_VARIABLE_CASE_KEY, caseKey.toString())
            .active()
            .count();
    }

    private com.backbase.flow.service.companylookup.casedata.BusinessStructureInfo toCompanyLookupBusinessStructureInfo(
        BusinessStructureInfo businessStructureInfo) {
        var companyLookupBusinessStructureInfo =
            new com.backbase.flow.service.companylookup.casedata.BusinessStructureInfo();
        companyLookupBusinessStructureInfo.setType(
            businessStructureInfo.getType() != null ? businessStructureInfo.getType().value() : null);
        companyLookupBusinessStructureInfo.setSubtype(
            businessStructureInfo.getSubtype() != null ? businessStructureInfo.getSubtype().value() : null);
        return companyLookupBusinessStructureInfo;
    }

    private DocumentRequest performLoadDocumentRequest() {
        var interactionResponse = action(LOAD_DOCUMENT_REQUESTS, interactionId);
        var documentRequestData = mapToObject(interactionResponse.getBody(), DocumentRequestData.class);
        return checkLoadDocumentRequest(documentRequestData);
    }

    private DocumentRequest checkLoadDocumentRequest(final DocumentRequestData documentRequestData) {
        assertThat(documentRequestData.getDocumentRequests())
            .hasSize(1)
            .first()
            .extracting(DocumentRequest::getDocumentType, DocumentRequest::getStatus)
            .isEqualTo(List.of("Business License", DocumentRequest.Status.OPEN));
        return documentRequestData.getDocumentRequests().get(0);
    }

    @SneakyThrows
    private void performUploadDocumentRequest(final DocumentRequest documentRequest) {
        var inputStream = getClass().getResourceAsStream("/business-license.jpeg");

        var uploadDocumentRequest = new UploadDocumentRequestDto();
        uploadDocumentRequest.setTempGroupId(documentRequest.getGroupId());
        uploadDocumentRequest.setInternalId(documentRequest.getInternalId());

        var interactionResponse = uploadDocument(interactionId, uploadDocumentRequest, inputStream);
        checkInteractionResponse(interactionResponse, DOCUMENT_REQUEST_JOURNEY);

        var documentRequestData = mapToObject(interactionResponse.getBody(), DocumentRequestFilesetDto.class);
        checkUploadDocumentRequest(documentRequestData);
    }

    private void checkUploadDocumentRequest(DocumentRequestFilesetDto documentRequestFilesetDto) {
        assertThat(documentRequestFilesetDto.getFiles())
            .hasSize(1)
            .first()
            .extracting(DocumentRequestFilerefDto::getName, DocumentRequestFilerefDto::getMediaType,
                DocumentRequestFilerefDto::getStatus)
            .isEqualTo(List.of("myBusinessLicense", IMAGE_JPEG_VALUE, FlowFilerefDto.StatusEnum.TO_BE_ADDED));
    }

    private void performCompleteDocumentUploadTask(String internalId) {
        var completeTaskDto = new CompleteTaskDto();
        completeTaskDto.setComment(COMMENT);
        completeTaskDto.setTempGroupId(internalId);
        completeTaskDto.setInternalId(internalId);
        final var interactionResponse = action(COMPLETE_TASK, interactionId, completeTaskDto);
        checkInteractionResponse(interactionResponse, DOCUMENT_REQUEST_JOURNEY);
    }

    private void performReviewDocumentRequest() {
        var reviewDocumentTask = getActiveUserTask(MANUAL_REVIEW, REVIEW_DOCUMENTS_TASK);
        completeUserTask(reviewDocumentTask.getId(), new ReviewDocumentsDto(true, null));
    }

    private void performSubmitDocumentRequest() {
        var interactionResponse = action(SUBMIT_DOCUMENT_REQUESTS, interactionId, null);
        checkInteractionResponse(interactionResponse, IDENTITY_VERIFICATION_INITIATION);
    }

    private List<Task> getTasksByCaseKeyAndDefinitionKey(String caseKey, String definitionKey) {
        return taskService.createTaskQuery()
            .active()
            .taskDefinitionKey(definitionKey)
            .processVariableValueEquals(PROCESS_VARIABLE_CASE_KEY, caseKey)
            .list();
    }

    private Task getActiveUserTask(String processDefinitionKey, String taskDefinitionKey) {
        return getActiveUserTaskQuery(processDefinitionKey, taskDefinitionKey)
            .singleResult();
    }

    private List<Task> getActiveUserTasks(String processDefinitionKey, String taskDefinitionKey) {
        return getActiveUserTaskQuery(processDefinitionKey, taskDefinitionKey)
            .list();
    }

    private HistoricTaskInstance getCompletedUserTask(String processDefinitionKey, String taskDefinitionKey) {
        return getCompletedUserTaskQuery(processDefinitionKey, taskDefinitionKey)
            .singleResult();
    }

    private List<HistoricTaskInstance> getCompletedUserTasks(String processDefinitionKey, String taskDefinitionKey) {
        return getCompletedUserTaskQuery(processDefinitionKey, taskDefinitionKey)
            .list();
    }

    private void completeAmlUserTask(String taskId, ReviewAmlUserRequest.RiskLevel riskLevel) {
        var taskData = new ReviewAmlUserRequest(
            true,
            new ReviewAmlUserRequest.ReviewApprovedReasonRequest("1", Set.of(MATCH_TYPE), COMMENT),
            null,
            riskLevel
        );
        completeUserTask(taskId, taskData);
    }

    private void completeRiskAssessmentTask(String taskId) {
        var taskData = new ReviewApplicationUserRequest(
            true,
            new ReviewApplicationUserRequest.ReviewApprovedReason(COMMENT),
            null
        );
        completeUserTask(taskId, taskData);
    }

    private void completeBusinessRelationTask(String taskId) {
        var taskData = new ReviewInformationRequest(true, COMMENT, null);
        completeUserTask(taskId, taskData);
    }

    @SneakyThrows
    private List<String> getIdtIds(UUID caseKey, String processDefinitionKey, String taskDefinitionKey) {
        return taskService.createTaskQuery()
            .processVariableValueEquals(PROCESS_VARIABLE_CASE_KEY, caseKey.toString())
            .processDefinitionKey(processDefinitionKey)
            .taskDefinitionKey(taskDefinitionKey)
            .list()
            .stream()
            .map(Task::getId)
            .collect(Collectors.toList());
    }

    private Pair<String, String> createTaskAndJobPair(String taskId) {
        return Pair.of(taskId, openTask(taskId));
    }

    private String getInteractionId(Pair<String, String> taskAndJobPair) {
        return getInteractionIdFromTaskAndJob(taskAndJobPair.getLeft(), taskAndJobPair.getRight());
    }

    private void checkUserTaskIsActive(Task task) {
        assertThat(task).isNotNull();
        assertThat(((TaskEntity) task).getDeleteReason()).isNull();
    }

    private void checkUserTaskIsCompleted(HistoricTaskInstance historicTaskInstance) {
        assertThat(historicTaskInstance).isNotNull();
        assertThat(historicTaskInstance.getEndTime()).isNotNull();
        assertThat(historicTaskInstance.getDeleteReason()).isEqualTo("completed");
    }

    private TaskQuery getActiveUserTaskQuery(String processDefinitionKey, String taskDefinitionKey) {
        return taskService.createTaskQuery()
            .processDefinitionKey(processDefinitionKey)
            .processVariableValueEquals(PROCESS_VARIABLE_CASE_KEY, caseKey.toString())
            .taskDefinitionKey(taskDefinitionKey);
    }

    private HistoricTaskInstanceQuery getCompletedUserTaskQuery(String processDefinitionKey, String taskDefinitionKey) {
        return historyService.createHistoricTaskInstanceQuery()
            .processDefinitionKey(processDefinitionKey)
            .processVariableValueEquals(PROCESS_VARIABLE_CASE_KEY, caseKey.toString())
            .taskDefinitionKey(taskDefinitionKey)
            .finished();
    }

    private static UpdateCurrentUserOwnerRequest businessPersonToCurrentUser(
        BusinessPersonResponse businessPerson,
        CurrentUserData currentUserData
    ) {
        return UpdateCurrentUserOwnerRequest.builder()
            .id(businessPerson.getId())
            .firstName(businessPerson.getFirstName())
            .lastName(businessPerson.getLastName())
            .email(businessPerson.getEmail())
            .phone(businessPerson.getPhone())
            .role(currentUserData.getSelectedRole())
            .otherRole(businessPerson.getOtherRole())
            .ownershipPercentage(currentUserData.getOwnershipPercentage())
            .build();
    }
}
