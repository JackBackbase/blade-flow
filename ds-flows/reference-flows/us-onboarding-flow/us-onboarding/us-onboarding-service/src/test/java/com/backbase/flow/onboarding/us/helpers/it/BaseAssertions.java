package com.backbase.flow.onboarding.us.helpers.it;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backbase.flow.application.uso.casedata.AgreementInfo;
import com.backbase.flow.application.uso.casedata.AgreementInfo.PolicyType;
import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.application.uso.casedata.SelectedProduct;
import com.backbase.flow.casedata.api.model.FlowCaseStatusDto;
import com.backbase.flow.interaction.api.model.InteractionResponseDto;
import com.backbase.flow.interaction.api.model.ModelError;
import com.backbase.flow.onboarding.us.BaseIT;
import com.backbase.flow.onboarding.us.interaction.dto.SubmitInBranchDto;
import com.backbase.flow.process.api.model.FlowTaskCompleteDto;
import com.backbase.flow.service.aml.casedata.AntiMoneyLaunderingInfo.RiskLevel;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

public class BaseAssertions {

    private static final String TASK_API_ROOT = "/client-api/process/v2/task";
    private static final String TASKS_ENDPOINT = TASK_API_ROOT;
    private static final String TASK_ENDPOINT = TASK_API_ROOT + "/{taskId}";
    private static final String TASK_COMPLETE_ENDPOINT = TASK_API_ROOT + "/{taskId}/complete";
    private static final String CASE_DATA_ENDPOINT_ROOT = "/client-api/casedata/v3/cases";
    private static final String CASE_DATA = CASE_DATA_ENDPOINT_ROOT + "/{caseId}";
    private static final String CASE_STATUS = CASE_DATA_ENDPOINT_ROOT + "/{caseId}/status";

    private static final String TOKEN_FOR_USER = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiY25leHAiOnRydWUsImFubG9jIjp0cnVlLCJhbmV4cCI6dHJ1ZSwiZW5ibCI6dHJ1ZSwicm9sIjpbIlJPTEVfMSIsIlJPTEVfMiJdfQ.EjJMB-6iYGTbl0cSHC6_VPEy38ybaKF6f6WkscZhbOY";

    private static ObjectMapper objectMapper;

    public static final String INIT_STEP = "init";
    public static final String SELECT_PRODUCTS_STEP = "select-products";
    public static final String TERMS_AND_CONDITIONS_STEP = "terms-and-conditions";
    public static final String ANCHOR_DATA_STEP = "anchor-data";
    public static final String OTP_VERIFICATION_STEP = "otp-verification";
    public static final String IDENTITY_VERIFICATION_STEP = "identity-verification";
    public static final String ADDRESS_STEP = "address";
    public static final String CO_APPLICANT_STEP = "co-applicant";
    public static final String CO_APPLICANT_DATA_STEP = "co-applicant-data";
    public static final String CO_APPLICANT_ADDRESS_STEP = "co-applicant-address";
    public static final String CITIZENSHIP_STEP = "citizenship";
    public static final String SSN_STEP = "ssn";
    public static final String NON_RESIDENT_DATA_STEP = "non-resident-data";
    public static final String KYC_STEP = "kyc";
    public static final String CREDENTIALS_STEP = "credentials";
    public static final String PERSONAL_INFO = "personal-info";
    public static final String SUBMIT_IN_BRANCH = "submit-in-branch";
    public static final String SUCCESSFULLY_DONE_STEP = "successfully-done";
    public static final String SUCCESSFULLY_DONE_CO_APPLICANT_STEP = "successfully-done-co-applicant";
    public static final String IN_REVIEW_DONE_STEP = "in-review-done";
    public static final String DECLINED_STEP = "declined";
    public static final String ADDRESS_VALIDATION_IDT_TASK_NAME = "Address";
    public static final String IDV_IDT_TASK_NAME = "ID protection";
    public static final String KYC_IDT_TASK_NAME = "Additional information";
    public static final String CITIZENSHIP_IDT_TASK_NAME = "Citizenship";


    static {
        BaseAssertions.objectMapper = BaseIT.getObjectMapper();
    }

    static void assertCaseDataSubmitCredentialsFilled(Applicant applicant) {
        assertThat(applicant.getEmail()).isNotNull();
    }

    static void assertCaseDataSubmitCitizenshipFilled(Applicant applicant) {
        assertThat(applicant.getCitizenship()).isNotNull();
        assertThat(applicant.getCitizenship().getCitizenshipType()).isNotNull();
    }

    static void assertCaseDataSubmitNonResidentDataFilled(Applicant applicant) {
        assertThat(applicant.getCitizenship()).isNotNull();
        assertThat(applicant.getCitizenship().getNationalTin()).isNotNull();
        assertThat(applicant.getCitizenship().getForeignTin()).isNotNull();
        assertThat(applicant.getCitizenship().getW8ben()).isNotNull();
        assertThat(applicant.getCitizenship().getWithholdingTaxAccepted()).isNotNull();
        assertThat(applicant.getCitizenship().getNonResident()).isNotNull();
        assertThat(applicant.getCitizenship().getNonResident().getCitizenshipCountryCode()).isNotNull();
        assertThat(applicant.getCitizenship().getNonResident().getResidencyAddress()).isNotNull();
        assertThat(applicant.getCitizenship().getNonResident().getResidencyAddress().getZipCode()).isNotNull();
        assertThat(applicant.getCitizenship().getNonResident().getResidencyAddress().getCity()).isNotNull();
        assertThat(applicant.getCitizenship().getNonResident().getResidencyAddress().getCountryCode()).isNotNull();
        assertThat(applicant.getCitizenship().getNonResident().getResidencyAddress().getNumberAndStreet()).isNotNull();
    }

    static void assertCaseDataSubmitSsnFilled(Applicant applicant) {
        assertThat(applicant.getCitizenship().getSsn()).isNotNull();
    }

    static void assertApplicantAgreementFilled(Applicant applicant) {
        assertThat(applicant.getAgreements().size()).isEqualTo(PolicyType.values().length);
        assertThat(applicant.getAgreements()).allMatch(AgreementInfo::getAccepted);
        assertThat(applicant.getAgreements()).allMatch(agreementInfo -> !agreementInfo.getAcceptedAt().isEmpty());
    }

    static void assertApplicantPhoneVerifiedFilled(Applicant applicant) {
        assertThat(applicant.getPhoneNumberVerified()).isTrue();
    }

    static void assertApplicantIdentityVerificationInitiationFilled(Applicant applicant) {
        assertThat(applicant.getIdentityVerificationInitiation()).isNotNull();
        assertThat(applicant.getIdentityVerificationInitiation().getUserReference()).isNotNull();
        assertThat(applicant.getIdentityVerificationInitiation().getTransactionReference()).isNotNull();
    }

    static void assertApplicantIdentityVerificationResultFilled(Applicant applicant, String mockedVerificationId) {
        assertThat(applicant.getIdentityVerificationResult()).isNotNull();
        assertThat(applicant.getIdentityVerificationResult().getVerificationId()).isNotNull();
        assertThat(applicant.getIdentityVerificationResult().getVerificationId()).isEqualTo(mockedVerificationId);
    }

    static void assertApplicantIdentityVerificationAfterHookResponse(Applicant applicant, String mockedVerificationId) {
        assertThat(applicant.getIdentityVerificationResult()).isNotNull();
        assertThat(applicant.getIdentityVerificationResult().getVerificationId()).isNotNull();
        assertThat(applicant.getIdentityVerificationResult().getDocumentStatus()).isNotNull();
        String expectedStatus = "";
        switch (mockedVerificationId) {
            case "approved":
                expectedStatus = "APPROVED_VERIFIED";
                break;
            case "review":
                expectedStatus = "ERROR_NOT_READABLE_ID";
                break;
            case "fail":
                expectedStatus = "DENIED_FRAUD";
                break;
            default:
                break;
        }
        assertThat(applicant.getIdentityVerificationResult().getVerificationId()).isEqualTo(mockedVerificationId);
        assertThat(applicant.getIdentityVerificationResult().getDocumentStatus()).isEqualTo(expectedStatus);

    }

    static void assertApplicantSubmitAddressFilled(Applicant applicant) {
        assertThat(applicant.getAddress()).isNotNull();
        assertThat(applicant.getAddress().getNumberAndStreet()).isNotNull();
        assertThat(applicant.getAddress().getZipCode()).isNotNull();
        assertThat(applicant.getAddress().getCity()).isNotNull();
        assertThat(applicant.getAddress().getState()).isNotNull();
    }

    static void assertCaseDataSubmitAccountTypeFilled(Onboarding onboarding) {
        assertThat(onboarding.getIsJointAccount()).isNotNull();
    }

    static void assertApplicantAnchorDataFilled(Applicant applicant) {
        assertThat(applicant.getFirstName()).isNotNull();
        assertThat(applicant.getLastName()).isNotNull();
        assertThat(applicant.getDateOfBirth()).isNotNull();
    }

    static void assertApplicantPersonalInfoFilled(Applicant applicant) {
        assertThat(applicant.getFirstName()).isNotNull();
        assertThat(applicant.getLastName()).isNotNull();
        assertThat(applicant.getDateOfBirth()).isNotNull();
        assertThat(applicant.getEmail()).isNotNull();
        assertThat(applicant.getPhoneNumber()).isNotNull();
    }

    static void assertSelectProductsFilled(Onboarding onboarding) {
        assertThat(onboarding.getProductSelection().getSelectedProducts()).isNotEmpty();
        SelectedProduct product = onboarding.getProductSelection().getSelectedProducts().get(0);
        assertThat(product.getReferenceId()).isNotNull();
        assertThat(product.getProductName()).isNotNull();
    }

    static void assertCaseIsArchived(String caseKey) throws Exception {
        getCaseAsJsonString(caseKey)
            .andExpect(jsonPath("$.archived", equalTo(true)));
    }

    static void assertPrefillAnchorData(InteractionResponseDto response, String expectedNextStep) {
        assertNextStep(response, expectedNextStep);
    }

    static void assertSubmitAnchorData(InteractionResponseDto response, Applicant applicant,
        String expectedNextStep) {
        assertNextStep(response, expectedNextStep);
        assertApplicantAnchorDataFilled(applicant);
    }

    static void assertSubmitPersonalInfo(InteractionResponseDto response, Applicant applicant,
        String expectedNextStep) {
        assertNextStep(response, expectedNextStep);
        assertApplicantPersonalInfoFilled(applicant);
    }

    static void assertRequestOtp(InteractionResponseDto response, String expectedNextStep) {
        assertNextStep(response, expectedNextStep);
    }

    static void assertVerifyOtp(InteractionResponseDto response, Applicant applicant, String expectedNextStep) {
        assertNextStep(response, expectedNextStep);
        assertApplicantPhoneVerifiedFilled(applicant);
    }

    static void assertInvalidOtpCode(InteractionResponseDto response, Applicant applicant, String expectedNextStep) {
        assertThat(response.getStep().getStatus()).isNull();
        assertThat(response.getStep().getName()).isEqualTo(expectedNextStep);
        assertThat(response.getActionErrors().size()).isOne();
        assertThat(response.getActionErrors())
            .containsExactly(new ModelError().message("Wrong code. Please check and try again.").key("OTP-003"));
    }

    static void assertVerificationType(InteractionResponseDto response, String expectedNextStep) {
        assertNextStep(response, expectedNextStep);
        assertThat(response.getBody()).hasToString("{verificationType=ID_AND_IDENTITY}");
    }

    static void assertIdentityInitiation(InteractionResponseDto response, Applicant applicant,
        String expectedNextStep) {
        assertNextStep(response, expectedNextStep);
        assertApplicantIdentityVerificationInitiationFilled(applicant);
    }

    static void assertIdentityResult(InteractionResponseDto response, Applicant applicant, String verificationId,
        String expectedNextStep) {
        assertNextStep(response, expectedNextStep);
        assertApplicantIdentityVerificationResultFilled(applicant, verificationId);
    }

    static void assertSubmitAddress(InteractionResponseDto response, Applicant applicant, String expectedNextStep) {
        assertNextStep(response, expectedNextStep);
        assertApplicantSubmitAddressFilled(applicant);
    }

    static void assertSubmitAccountType(InteractionResponseDto response, Onboarding onboarding,
        String expectedNextStep) {
        assertNextStep(response, expectedNextStep);
        assertCaseDataSubmitAccountTypeFilled(onboarding);
    }

    static void assertFetchCitizenshipDataBeforeSubmit(InteractionResponseDto response, Applicant applicant,
        String expectedNextStep) {
        assertNextStep(response, expectedNextStep);
        assertThat(applicant.getCitizenship()).isNull();
    }

    static void assertFetchCitizenshipDataAfterSubmit(InteractionResponseDto response, Applicant applicant,
        String expectedNextStep) {
        assertNextStep(response, expectedNextStep);
        assertThat(applicant.getCitizenship().getCitizenshipType()).isNotNull();
        assertThat(applicant.getCitizenship().getSsn()).isNull();
    }

    static void assertSubmitCitizenshipType(InteractionResponseDto response, Applicant applicant,
        String expectedNextStep) {
        assertNextStep(response, expectedNextStep);
        assertCaseDataSubmitCitizenshipFilled(applicant);
    }

    static void assertSubmitNonResidentData(InteractionResponseDto response, Applicant applicant,
        String expectedNextStep) {
        assertNextStep(response, expectedNextStep);
        assertCaseDataSubmitNonResidentDataFilled(applicant);
    }

    static void assertSubmitSsn(InteractionResponseDto response, Applicant applicant, String expectedNextStep) {
        assertNextStep(response, expectedNextStep);
        assertCaseDataSubmitSsnFilled(applicant);
    }

    static void assertRequestKycQuestions(InteractionResponseDto response, String expectedNextStep) {
        assertNextStep(response, expectedNextStep);
        assertThat(response.getBody()).isNotNull();
    }

    static void assertSubmitKycQuestionsWithIdv(InteractionResponseDto response, Applicant applicant,
        String expectedNextStep, String mockedVerificationId) {
        assertNextStep(response, expectedNextStep);
        assertApplicantIdentityVerificationAfterHookResponse(applicant, mockedVerificationId);
    }

    static void assertSubmitKycQuestions(InteractionResponseDto response, String expectedNextStep) {
        assertNextStep(response, expectedNextStep);
    }

    static void assertProductSelectionList(InteractionResponseDto response, String expectedNextStep) {
        assertNextStep(response, expectedNextStep);
        assertThat(response.getBody()).isNotNull();
    }

    static void assertSelectProducts(InteractionResponseDto response, Onboarding onboarding,
        String expectedNextStep) {
        assertNextStep(response, expectedNextStep);
        assertSelectProductsFilled(onboarding);
    }

    static void assertSubmitInBranch(InteractionResponseDto response, String caseKey,
        String expectedNextStep) {
        assertNextStep(response, expectedNextStep);
        assertSubmitInBranch(response, caseKey);
    }

    static void assertAgreeWithTerms(InteractionResponseDto response, Applicant applicant,
        String expectedNextStep) {
        assertNextStep(response, expectedNextStep);
        assertApplicantAgreementFilled(applicant);
    }

    static void assertMoveViaEmptyHandler(InteractionResponseDto response, String expectedNextStep) {
        assertNextStep(response, expectedNextStep);
    }

    static void assertSetupCredentials(InteractionResponseDto response, Applicant applicant, String expectedNextStep) {
        assertNextStep(response, expectedNextStep);
        assertCaseDataSubmitCredentialsFilled(applicant);
    }

    static void assertNextStep(InteractionResponseDto response, String expectedNextStep) {
        assertThat(response.getStep().getStatus()).isNull();
        assertThat(response.getStep().getName()).isEqualTo(expectedNextStep);
        assertThat(response.getActionErrors().size()).isZero();
    }

    static void assertBackStep(InteractionResponseDto response, String currentStep, String expectedBackStep) {
        assertThat(response.getSteps().get(currentStep).getBack()).isEqualTo(expectedBackStep);
    }

    private static void assertSubmitInBranch(InteractionResponseDto response, String caseKey) {
        assertThat(((HashMap) response.getBody()).get("caseKey")).isEqualTo(caseKey);
    }

    public static void assertIsMainApplicantFlow(Onboarding onboarding) {
        assertThat(onboarding.getCoApplicantId()).isNullOrEmpty();
        assertThat(onboarding.getIsMainApplicantFlow()).isTrue();
    }

    public static void assertIsCoApplicantFlow(Onboarding onboarding) {
        assertThat(onboarding.getCoApplicantId()).isNotEmpty();
        assertThat(onboarding.getIsMainApplicantFlow()).isFalse();
    }

    public static String assertUserTaskIsCreatedCorrectly(String taskName, String caseKey, boolean isOpen)
        throws Exception {

        String expectedStatus = "closed";
        if (isOpen) {
            expectedStatus = "open";
        }

        MvcResult mvcResult = BaseIT.getMockMvc().perform(get(TASKS_ENDPOINT)
            .param("withOpen", "true")
            .param("withClosed", "true")
            .param("caseKey", caseKey)
            .header("Authorization", TOKEN_FOR_USER)
            .param("search", taskName)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name", equalTo(taskName)))
            .andExpect(jsonPath("$[0].status", equalTo(expectedStatus)))
            .andReturn();

        return JsonPath.read(mvcResult.getResponse().getContentAsString(), "$[0].id");
    }

    public static void assertRetrieveVerifyNonResidentData(String taskId, String caseKey)
        throws Exception {
        System.out.println(taskId);
        BaseIT.getMockMvc().perform(get(TASK_ENDPOINT.replace("{taskId}", taskId))
            .header("Authorization", TOKEN_FOR_USER)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.caseKey", equalTo(caseKey)))
            .andExpect(jsonPath("$.taskData.fullName").isNotEmpty())
            .andExpect(jsonPath("$.taskData.dateOfBirth").isNotEmpty())
            .andExpect(jsonPath("$.taskData.citizenship.foreignTin").isNotEmpty())
            .andExpect(jsonPath("$.taskData.citizenship.nonResident").isNotEmpty())
            .andExpect(jsonPath("$.taskData.citizenship.nonResident.citizenshipCountryCode").isNotEmpty())
            .andExpect(jsonPath("$.taskData.citizenship.nonResident.citizenshipCountry").isNotEmpty())
            .andExpect(jsonPath("$.taskData.citizenship.nonResident.residencyAddress").isNotEmpty())
            .andExpect(jsonPath("$.taskData.citizenship.nonResident.residencyAddress.countryCode").isNotEmpty())
            .andExpect(jsonPath("$.taskData.citizenship.nonResident.residencyAddress.country").isNotEmpty())
            .andExpect(jsonPath("$.taskData.citizenship.withholdingTaxAccepted").isNotEmpty())
            .andExpect(jsonPath("$.taskData.citizenship.w8ben").isNotEmpty())
            .andExpect(jsonPath("$.taskData.citizenship.citizenshipType").isNotEmpty())
            .andExpect(jsonPath("$.taskData.citizenship.citizenshipReview").isNotEmpty())
            .andExpect(jsonPath("$.taskData.citizenship.citizenshipReview.needed").isNotEmpty());
    }

    public static void assertUserTaskDataCanBeRetrievedCorrectlyByUser(String taskId, String caseKey) throws Exception {
        BaseIT.getMockMvc().perform(get(TASK_ENDPOINT.replace("{taskId}", taskId))
            .header("Authorization", TOKEN_FOR_USER)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.caseKey", equalTo(caseKey)))
            .andExpect(jsonPath("$.taskData.fullName").isNotEmpty())
            .andExpect(jsonPath("$.taskData.yearOfBirth").isNotEmpty())
            .andExpect(jsonPath("$.taskData.shareUrl").isNotEmpty())
            .andExpect(jsonPath("$.taskData.matches").isNotEmpty())
            .andExpect(jsonPath("$.taskData.matches").isArray());
    }

    public static void assertCompleteVerifyNonResidentDataTask(String taskId,
        boolean approved)
        throws Exception {

        var completeDto = new FlowTaskCompleteDto();
        completeDto.putTaskDataItem("approved", approved);
        if (!approved) {
            completeDto.putTaskDataItem("comment", "Example reason for rejection.");
        }

        BaseIT.getMockMvc().perform(post(TASK_COMPLETE_ENDPOINT.replace("{taskId}", taskId))
            .header("Authorization", TOKEN_FOR_USER)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(completeDto)))
            .andExpect(status().isAccepted());
    }

    public static void assertAmlUserTaskCanBeCompletedSuccessfullyByUser(String taskId, boolean amlMatch)
        throws Exception {

        class ReviewApprovedReason {

            public final String matchId = "1";
            public final Set<String> matchTypes = Set.of("pep");
            public final String comment = "some reason";
        }

        class ReviewDeclinedReason {

            public final String comment = "some reason";
        }

        var completeDto = new FlowTaskCompleteDto();
        completeDto.putTaskDataItem("reviewApproved", amlMatch);
        completeDto.putTaskDataItem("riskLevel", RiskLevel.MEDIUM);
        if (amlMatch) {
            completeDto.putTaskDataItem("reviewApprovedReason", new ReviewApprovedReason());
        } else {
            completeDto.putTaskDataItem("reviewDeclinedReason", new ReviewDeclinedReason());
        }

        BaseIT.getMockMvc().perform(post(TASK_COMPLETE_ENDPOINT.replace("{taskId}", taskId))
            .header("Authorization", TOKEN_FOR_USER)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(completeDto)))
            .andExpect(status().isAccepted());
    }

    public static void assertIdvUserTaskCanBeCompletedSuccessfullyByUser(String taskId, boolean approved)
        throws Exception {
        FlowTaskCompleteDto completeDto = new FlowTaskCompleteDto();
        completeDto.putTaskDataItem("decision", approved ? "approved" : "fail");
        BaseIT.getMockMvc().perform(post(TASK_COMPLETE_ENDPOINT.replace("{taskId}", taskId))
            .header("Authorization", TOKEN_FOR_USER)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(completeDto)))
            .andExpect(status().isAccepted());
    }

    public static void assertCaseStatus(String caseKey, String statusName, String statusValue, boolean isSet)
        throws Exception {
        final FlowCaseStatusDto caseStatus = new FlowCaseStatusDto();
        caseStatus.setCaseKey(caseKey);
        caseStatus.setStatusValue(statusValue);
        caseStatus.setStatusName(statusName);

        final List<FlowCaseStatusDto> statusList = objectMapper
            .readValue(BaseIT.getMockMvc().perform(get(CASE_STATUS, caseKey)
                    .header("Authorization", TOKEN_FOR_USER)
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON))
                    .andDo(print())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(),
                new TypeReference<>() {
                });

        if (isSet) {
            assertThat(statusList).usingElementComparatorIgnoringFields("lastModified").contains(caseStatus);
        } else {
            assertThat(statusList).usingElementComparatorIgnoringFields("lastModified").doesNotContain(caseStatus);
        }
    }

    public static Onboarding getCaseData(String caseKey) {
        try {
            final String caseAsJsonString = getCaseAsJsonString(caseKey)
                .andReturn()
                .getResponse()
                .getContentAsString();

            final TreeNode node = objectMapper.readTree(caseAsJsonString);

            return objectMapper.treeToValue(node.get("data"), Onboarding.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get case data.", e);
        }
    }

    private static ResultActions getCaseAsJsonString(String caseKey) {
        try {
            return BaseIT.getMockMvc().perform(get(CASE_DATA, caseKey)
                .header("Authorization", TOKEN_FOR_USER)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
        } catch (Exception e) {
            throw new RuntimeException("Failed to get case.", e);
        }
    }
}
