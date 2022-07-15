package com.backbase.flow.onboarding.us;

import static com.backbase.flow.onboarding.us.helpers.AccountTypeStepCalls.submitIndividualAccountType;
import static com.backbase.flow.onboarding.us.helpers.AccountTypeStepCalls.submitJointAccountType;
import static com.backbase.flow.onboarding.us.helpers.AddressStepCalls.submitAddress;
import static com.backbase.flow.onboarding.us.helpers.AnchorStepCalls.prefillAnchorData;
import static com.backbase.flow.onboarding.us.helpers.AnchorStepCalls.submitAnchorData;
import static com.backbase.flow.onboarding.us.helpers.AnchorStepCalls.submitCoApplicantAnchorData;
import static com.backbase.flow.onboarding.us.helpers.CitizenshipStepCalls.submitCitizenshipType;
import static com.backbase.flow.onboarding.us.helpers.IdentityVerificationStepCalls.identityInitiation;
import static com.backbase.flow.onboarding.us.helpers.IdentityVerificationStepCalls.identityResult;
import static com.backbase.flow.onboarding.us.helpers.IdentityVerificationStepCalls.verificationType;
import static com.backbase.flow.onboarding.us.helpers.InitStepCalls.fetchCoApplicant;
import static com.backbase.flow.onboarding.us.helpers.KycStepCalls.requestKycQuestions;
import static com.backbase.flow.onboarding.us.helpers.KycStepCalls.submitKycQuestions;
import static com.backbase.flow.onboarding.us.helpers.NonResidentDataStepCalls.submitNonResidentData;
import static com.backbase.flow.onboarding.us.helpers.OtpVerificationStepCalls.requestOtp;
import static com.backbase.flow.onboarding.us.helpers.OtpVerificationStepCalls.requestOtpCoApplicant;
import static com.backbase.flow.onboarding.us.helpers.OtpVerificationStepCalls.verifyFinalizeWithInvalidOtp;
import static com.backbase.flow.onboarding.us.helpers.OtpVerificationStepCalls.verifyOtp;
import static com.backbase.flow.onboarding.us.helpers.OtpVerificationStepCalls.verifyOtpCoApplicant;
import static com.backbase.flow.onboarding.us.helpers.OtpVerificationStepCalls.verifyOtpFinalize;
import static com.backbase.flow.onboarding.us.helpers.OtpVerificationStepCalls.verifyOtpFinalizeCoApplicant;
import static com.backbase.flow.onboarding.us.helpers.ProductSelectionListStepCalls.getProductList;
import static com.backbase.flow.onboarding.us.helpers.SelectProductsStepCalls.selectProducts;
import static com.backbase.flow.onboarding.us.helpers.SetupCredentialsStepCalls.moveViaEmptyHandler;
import static com.backbase.flow.onboarding.us.helpers.SetupCredentialsStepCalls.setupCredentials;
import static com.backbase.flow.onboarding.us.helpers.SsnStepCalls.submitSsn;
import static com.backbase.flow.onboarding.us.helpers.TermsAndConditionsStepCalls.agreeToTerms;
import static com.backbase.flow.onboarding.us.helpers.it.BaseAssertions.assertIsCoApplicantFlow;
import static com.backbase.flow.onboarding.us.helpers.it.BaseAssertions.assertIsMainApplicantFlow;
import static com.backbase.flow.onboarding.us.helpers.it.BaseAssertions.assertCompleteVerifyNonResidentDataTask;
import static com.backbase.flow.onboarding.us.helpers.it.BaseAssertions.getCaseData;
import static com.backbase.flow.onboarding.us.helpers.it.FinalizeOnboardingAssertions.assertAmlUserTaskCanBeCompletedSuccessfullyByUser;
import static com.backbase.flow.onboarding.us.helpers.it.FinalizeOnboardingAssertions.assertIdvUserTaskCanBeCompletedSuccessfullyByUser;
import static com.backbase.flow.onboarding.us.helpers.it.FinalizeOnboardingAssertions.assertRequestOtp;
import static com.backbase.flow.onboarding.us.helpers.it.FinalizeOnboardingAssertions.assertSetupCredentials;
import static com.backbase.flow.onboarding.us.helpers.it.FinalizeOnboardingAssertions.assertUserTaskIsCreatedCorrectly;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertAgreeWithTerms;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertCaseStatus;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertFetchCoApplicant;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertFetchCoApplicant_CoApplicant;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertIdentityInitiation;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertIdentityResult;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertIdtIdvTaskCompleted;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertIdtIdvTaskIsOpen;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertPrefillAnchorData;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertProductSelectionList;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertRequestKycQuestions;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSelectProducts;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitAddress;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitAddress_CoApplicant;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitAnchorData;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitCoApplicantAddressData;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitCoApplicantAnchorData;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitIndividualAccountType;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitJointAccountType;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitKycQuestionsIdentityVerified;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitKycQuestionsInReviewDone;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitNonResidentCitizenshipType;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitNonResidentData;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitResidentCitizenshipType;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitSsn;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertVerificationType;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertVerifyOtp;
import static org.assertj.core.api.Assertions.assertThat;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.CitizenshipInfo.CitizenshipType;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.interaction.api.model.InteractionResponseDto;
import com.backbase.flow.onboarding.us.helpers.it.FinalizeOnboardingAssertions;
import com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FinalizeOnboardingIT extends BaseIT {

    private static final String FIRST_NAME_MOCK_KYC_VALIDATION_OK = "John";
    private static final String CO_APPLICANT_FIRST_NAME_MOCK_KYC_VALIDATION_OK = "Jane";
    private static final String AGE_18_DOB = LocalDate.now().minusYears(18).toString();
    private static final String FINALIZE_ONBOARDING_INTERACTION = "finalize-onboarding";
    private static final String IDV_REVIEW_TASK = "Review ID&V";
    private static final String CITIZENSHIP_REVIEW_TASK = "Decide on validity of data from W-8BEN form";
    private static final String AML_REVIEW_TASK = "Review AML hit";
    private static final String FIRST_NAME_AML_POTENTIAL_MATCH = "potential_match";
    private static final String APPROVED = "approved";
    private static final String REVIEW = "review";

    @BeforeEach
    public void beforeEach() {
        setInteraction("onboarding");
    }

    @Test
    void citizenshipReview_happyFlow() throws Exception {
        String caseKey = fullFlow_citizenshipInReview();
        final String taskId = assertUserTaskIsCreatedCorrectly(CITIZENSHIP_REVIEW_TASK, caseKey, true);
        assertCompleteVerifyNonResidentDataTask(taskId, true);

        finalizeOnboarding(caseKey, false);
    }

    @Test
    void idvReview_happyFlow() throws Exception {
        String caseKey = fullFlow_idvInReview(FIRST_NAME_MOCK_KYC_VALIDATION_OK, true);
        final String taskId = assertUserTaskIsCreatedCorrectly(IDV_REVIEW_TASK, caseKey, true);
        assertIdvUserTaskCanBeCompletedSuccessfullyByUser(taskId, true);

        finalizeOnboarding(caseKey, false);
    }

    @Test
    void amlReview_happyFlow() throws Exception {
        String caseKey = fullFlow_amlInReview();
        final String taskId = assertUserTaskIsCreatedCorrectly(AML_REVIEW_TASK, caseKey, true);
        assertAmlUserTaskCanBeCompletedSuccessfullyByUser(taskId, false);

        finalizeOnboarding(caseKey, false);
    }

    @Test
    void citizenshipReview_happyFlow_jointAccount() throws Exception {
        String caseKey = fullFlow_citizenshipInReview_jointAccount();
        final String taskId = assertUserTaskIsCreatedCorrectly(CITIZENSHIP_REVIEW_TASK, caseKey, true);
        assertCompleteVerifyNonResidentDataTask(taskId, true);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        finalizeOnboarding(caseKey, true);

        assertIsCoApplicantFlow(getCaseData(caseKey));
    }

    @Test
    void idvReview_happyFlow_jointAccount() throws Exception {
        String caseKey = fullFlow_idvInReview_jointAccount();
        final String taskId = assertUserTaskIsCreatedCorrectly(IDV_REVIEW_TASK, caseKey, true);
        assertIdvUserTaskCanBeCompletedSuccessfullyByUser(taskId, true);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        finalizeOnboarding(caseKey, true);

        assertIsCoApplicantFlow(getCaseData(caseKey));
    }

    @Test
    void amlReview_happyFlow_jointAccount() throws Exception {
        String caseKey = fullFlow_amlInReview_jointAccount();
        final String taskId = assertUserTaskIsCreatedCorrectly(AML_REVIEW_TASK, caseKey, true);
        assertAmlUserTaskCanBeCompletedSuccessfullyByUser(taskId, false);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        finalizeOnboarding(caseKey, true);

        assertIsCoApplicantFlow(getCaseData(caseKey));
    }

    @Test
    void citizenshipReview_happyFlow_coApplicant() throws Exception {
        String caseKey = fullFlow_jointAccount();

        assertIsCoApplicantFlow(getCaseData(caseKey));
        fullFlow_citizenshipInReview_coApplicant(caseKey);

        final String taskId = assertUserTaskIsCreatedCorrectly(CITIZENSHIP_REVIEW_TASK, caseKey, true);
        assertCompleteVerifyNonResidentDataTask(taskId, true);

        assertIsCoApplicantFlow(getCaseData(caseKey));
        finalizeOnboardingCoApplicant(caseKey);

        assertIsCoApplicantFlow(getCaseData(caseKey));
    }

    @Test
    void idvReview_happyFlow_coApplicant() throws Exception {
        String caseKey = fullFlow_jointAccount();

        assertIsCoApplicantFlow(getCaseData(caseKey));
        fullFlow_idvInReview_coApplicant(caseKey, CO_APPLICANT_FIRST_NAME_MOCK_KYC_VALIDATION_OK, true);

        final String taskId = assertUserTaskIsCreatedCorrectly(IDV_REVIEW_TASK, caseKey, true);
        assertIdvUserTaskCanBeCompletedSuccessfullyByUser(taskId, true);

        assertIsCoApplicantFlow(getCaseData(caseKey));
        finalizeOnboardingCoApplicant(caseKey);

        assertIsCoApplicantFlow(getCaseData(caseKey));
    }

    @Test
    void amlReview_happyFlow_coApplicant() throws Exception {
        String caseKey = fullFlow_jointAccount();

        assertIsCoApplicantFlow(getCaseData(caseKey));
        fullFlow_amlInReview_coApplicant(caseKey);

        final String taskId = assertUserTaskIsCreatedCorrectly(AML_REVIEW_TASK, caseKey, true);
        assertAmlUserTaskCanBeCompletedSuccessfullyByUser(taskId, false);

        assertIsCoApplicantFlow(getCaseData(caseKey));
        finalizeOnboardingCoApplicant(caseKey);

        assertIsCoApplicantFlow(getCaseData(caseKey));
    }

    @Test
    void citizenshipReview_noReviewResultsDoesNotResume() throws Exception {
        fullFlow_citizenshipInReview();
        finalizeOnboardingWithError();
    }

    @Test
    void idvReview_noReviewResultsDoesNotResume() throws Exception {
        fullFlow_idvInReview(FIRST_NAME_AML_POTENTIAL_MATCH, false);
        finalizeOnboardingWithError();
    }

    @Test
    void amlReview_noReviewResultsDoesNotResume() throws Exception {
        fullFlow_amlInReview();
        finalizeOnboardingWithError();
    }

    @Test
    void citizenshipReview_declined() throws Exception {
        final String caseKey = fullFlow_citizenshipInReview();
        final String taskId = assertUserTaskIsCreatedCorrectly(CITIZENSHIP_REVIEW_TASK, caseKey, true);
        assertCompleteVerifyNonResidentDataTask(taskId, false);

        finalizeOnboardingWithError();
    }

    @Test
    void idvReview_declined() throws Exception {
        final String caseKey = fullFlow_idvInReview(FIRST_NAME_MOCK_KYC_VALIDATION_OK, true);
        final String taskId = assertUserTaskIsCreatedCorrectly(IDV_REVIEW_TASK, caseKey, true);
        assertIdvUserTaskCanBeCompletedSuccessfullyByUser(taskId, false);

        finalizeOnboardingWithError();
    }

    @Test
    void amlReview_declined() throws Exception {
        final String caseKey = fullFlow_amlInReview();
        final String taskId = assertUserTaskIsCreatedCorrectly(AML_REVIEW_TASK, caseKey, true);
        assertAmlUserTaskCanBeCompletedSuccessfullyByUser(taskId, true);

        finalizeOnboardingWithError();
    }

    @Test
    void incorrectOtp() throws Exception {
        final String caseKey = fullFlow_idvInReview(FIRST_NAME_MOCK_KYC_VALIDATION_OK, true);
        final String taskId = assertUserTaskIsCreatedCorrectly(IDV_REVIEW_TASK, caseKey, true);
        assertIdvUserTaskCanBeCompletedSuccessfullyByUser(taskId, false);
        setInteraction(FINALIZE_ONBOARDING_INTERACTION);

        final InteractionResponseDto requestOtpResponse = requestOtp(null);
        final String interactionId = requestOtpResponse.getInteractionId();
        assertRequestOtp(requestOtpResponse);

        final InteractionResponseDto verifyOtpResponse = verifyFinalizeWithInvalidOtp(interactionId);
        assertThat(verifyOtpResponse.getActionErrors()).hasSize(1);

        final String actualCaseKey = getCaseId(UUID.fromString(interactionId));
        assertThat(actualCaseKey).isNotEqualTo(caseKey);
    }

    private void finalizeOnboardingWithError() throws Exception {
        setInteraction(FINALIZE_ONBOARDING_INTERACTION);

        final InteractionResponseDto requestOtpResponse = requestOtp(null);
        final String interactionId = requestOtpResponse.getInteractionId();
        assertRequestOtp(requestOtpResponse);

        final InteractionResponseDto verifyOtpResponse = verifyOtpFinalize(interactionId);
        assertThat(verifyOtpResponse.getActionErrors()).hasSize(1);
    }

    private void finalizeOnboarding(String caseKey, boolean isJointAccount) throws Exception {
        setInteraction(FINALIZE_ONBOARDING_INTERACTION);

        final InteractionResponseDto requestOtpResponse = requestOtp(null);
        final String interactionId = requestOtpResponse.getInteractionId();
        assertRequestOtp(requestOtpResponse);

        Applicant mainApplicant = getCaseData(caseKey).getMainApplicant();
        final InteractionResponseDto verifyOtpResponse = verifyOtpFinalize(interactionId);
        FinalizeOnboardingAssertions.assertVerifyOtp(verifyOtpResponse, mainApplicant);

        final String actualCaseKey = getCaseId(UUID.fromString(interactionId));
        assertThat(actualCaseKey).isEqualTo(caseKey);

        final InteractionResponseDto setupCredentialsResponse = setupCredentials(interactionId);
        assertSetupCredentials(setupCredentialsResponse, mainApplicant, isJointAccount, true);
    }

    private void finalizeOnboardingCoApplicant(String caseKey) throws Exception {
        setInteraction(FINALIZE_ONBOARDING_INTERACTION);

        final InteractionResponseDto requestOtpResponse = requestOtpCoApplicant(null);
        final String interactionId = requestOtpResponse.getInteractionId();
        assertRequestOtp(requestOtpResponse);

        final Applicant coApplicant = getCaseData(caseKey).getCoApplicant();
        final InteractionResponseDto verifyOtpResponse = verifyOtpFinalizeCoApplicant(interactionId);
        FinalizeOnboardingAssertions.assertVerifyOtp(verifyOtpResponse, coApplicant);

        final String actualCaseKey = getCaseId(UUID.fromString(interactionId));
        assertThat(actualCaseKey).isEqualTo(caseKey);

        final InteractionResponseDto setupCredentialsResponse = setupCredentials(interactionId);
        assertSetupCredentials(setupCredentialsResponse, coApplicant, true, false);
    }

    private String fullFlow_jointAccount() throws Exception {
        final InteractionResponseDto fetchCoApplicantResponse = fetchCoApplicant(null);
        final String interactionId = fetchCoApplicantResponse.getInteractionId();
        assertFetchCoApplicant(fetchCoApplicantResponse);

        final InteractionResponseDto productSelectionListResponse = getProductList(interactionId);
        assertProductSelectionList(productSelectionListResponse);

        final InteractionResponseDto selectProductsResponse = selectProducts(interactionId);
        final String caseKey = getCaseId(UUID.fromString(interactionId));
        assertSelectProducts(selectProductsResponse, getCaseData(caseKey));

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto agreementResponse = agreeToTerms(interactionId);
        assertAgreeWithTerms(agreementResponse, getCaseData(caseKey).getMainApplicant());

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto anchorDataResponse = submitAnchorData(FIRST_NAME_MOCK_KYC_VALIDATION_OK,
            AGE_18_DOB, interactionId);
        assertSubmitAnchorData(anchorDataResponse, getCaseData(caseKey).getMainApplicant());

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto requestOtpResponse = requestOtp(interactionId);
        OnboardingAssertions.assertRequestOtp(requestOtpResponse);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto verifyOtpResponse = verifyOtp(interactionId);
        assertVerifyOtp(verifyOtpResponse, getCaseData(caseKey).getMainApplicant());

        assertIdtIdvTaskIsOpen(caseKey);
        final InteractionResponseDto verificationTypeResponse = verificationType(interactionId);
        assertVerificationType(verificationTypeResponse);

        final InteractionResponseDto identityInitiationResponse = identityInitiation(interactionId);
        assertIdentityInitiation(identityInitiationResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto identityResultResponse = identityResult(interactionId, APPROVED);
        assertIdentityResult(identityResultResponse, getCaseData(caseKey).getMainApplicant(), APPROVED);
        assertIdtIdvTaskCompleted(caseKey);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitAddressResponse = submitAddress(interactionId);
        assertSubmitAddress(submitAddressResponse, getCaseData(caseKey).getMainApplicant());

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitAccountType = submitJointAccountType(interactionId);
        assertSubmitJointAccountType(submitAccountType, getCaseData(caseKey));

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto coApplicantAnchorDataResponse = submitCoApplicantAnchorData(
            CO_APPLICANT_FIRST_NAME_MOCK_KYC_VALIDATION_OK, AGE_18_DOB, interactionId);
        assertSubmitCoApplicantAnchorData(coApplicantAnchorDataResponse, getCaseData(caseKey));

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto coApplicantAddressResponse = submitAddress(interactionId);
        assertSubmitCoApplicantAddressData(coApplicantAddressResponse, getCaseData(caseKey));

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitCitizenshipTypeResponse = submitCitizenshipType(interactionId,
            CitizenshipType.US_CITIZEN);
        assertSubmitResidentCitizenshipType(submitCitizenshipTypeResponse, getCaseData(caseKey).getMainApplicant());

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitSsnResponse = submitSsn(interactionId);
        assertSubmitSsn(submitSsnResponse, getCaseData(caseKey).getMainApplicant());

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitKycQuestionsResponse = submitKycQuestions(interactionId, true);
        assertSubmitKycQuestionsIdentityVerified(submitKycQuestionsResponse, getCaseData(caseKey).getMainApplicant(),
            APPROVED);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto setupCredentialsResponse = setupCredentials(interactionId);
        assertSetupCredentials(setupCredentialsResponse, getCaseData(caseKey).getMainApplicant(), true, true);

        assertCaseStatus(caseKey, "Primary case status", "open", true);
        assertIsCoApplicantFlow(getCaseData(caseKey));

        return caseKey;

    }

    String fullFlow_citizenshipInReview() throws Exception {
        final InteractionResponseDto fetchCoApplicantResponse = fetchCoApplicant(null);
        final String interactionId = fetchCoApplicantResponse.getInteractionId();
        assertFetchCoApplicant(fetchCoApplicantResponse);

        final InteractionResponseDto productSelectionListResponse = getProductList(interactionId);
        assertProductSelectionList(productSelectionListResponse);

        final InteractionResponseDto selectProductsResponse = selectProducts(interactionId);
        final String caseKey = getCaseId(UUID.fromString(interactionId));
        assertSelectProducts(selectProductsResponse, getCaseData(caseKey));

        final InteractionResponseDto agreementResponse = agreeToTerms(interactionId);
        assertAgreeWithTerms(agreementResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto anchorDataResponse = submitAnchorData(FIRST_NAME_MOCK_KYC_VALIDATION_OK,
            AGE_18_DOB, interactionId);

        assertThat(caseKey).isNotNull();
        assertSubmitAnchorData(anchorDataResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto requestOtpResponse = requestOtp(interactionId);
        assertRequestOtp(requestOtpResponse);

        final InteractionResponseDto verifyOtpResponse = verifyOtp(interactionId);
        assertVerifyOtp(verifyOtpResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto verificationTypeResponse = verificationType(interactionId);
        assertVerificationType(verificationTypeResponse);

        final InteractionResponseDto identityInitiationResponse = identityInitiation(interactionId);
        assertIdentityInitiation(identityInitiationResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto identityResultResponse = identityResult(interactionId, APPROVED);
        assertIdentityResult(identityResultResponse, getCaseData(caseKey).getMainApplicant(), APPROVED);

        final InteractionResponseDto submitAddressResponse = submitAddress(interactionId);
        assertSubmitAddress(submitAddressResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto submitAccountType = submitIndividualAccountType(interactionId);
        assertSubmitIndividualAccountType(submitAccountType, getCaseData(caseKey));

        final InteractionResponseDto submitCitizenshipTypeResponse = submitCitizenshipType(interactionId,
            CitizenshipType.NON_RESIDENT_ALIEN);
        assertSubmitNonResidentCitizenshipType(submitCitizenshipTypeResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto submitNonResidentDataResponse = submitNonResidentData(interactionId);
        assertSubmitNonResidentData(submitNonResidentDataResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto submitKycQuestionsResponse = submitKycQuestions(interactionId, true);
        assertSubmitKycQuestionsInReviewDone(submitKycQuestionsResponse);

        return caseKey;
    }


    String fullFlow_idvInReview(String firstName, boolean amlNoMatch) throws Exception {
        final InteractionResponseDto fetchCoApplicantResponse = fetchCoApplicant(null);
        final String interactionId = fetchCoApplicantResponse.getInteractionId();
        assertFetchCoApplicant(fetchCoApplicantResponse);

        final InteractionResponseDto productSelectionListResponse = getProductList(interactionId);
        assertProductSelectionList(productSelectionListResponse);

        final InteractionResponseDto selectProductsResponse = selectProducts(interactionId);
        final String caseKey = getCaseId(UUID.fromString(interactionId));
        assertSelectProducts(selectProductsResponse, getCaseData(caseKey));

        final InteractionResponseDto agreementResponse = agreeToTerms(interactionId);
        assertAgreeWithTerms(agreementResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto anchorDataResponse = submitAnchorData(firstName, AGE_18_DOB, interactionId);

        assertThat(caseKey).isNotNull();
        assertSubmitAnchorData(anchorDataResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto requestOtpResponse = requestOtp(interactionId);
        assertRequestOtp(requestOtpResponse);

        final InteractionResponseDto verifyOtpResponse = verifyOtp(interactionId);
        assertVerifyOtp(verifyOtpResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto verificationTypeResponse = verificationType(interactionId);
        assertVerificationType(verificationTypeResponse);

        final InteractionResponseDto identityInitiationResponse = identityInitiation(interactionId);
        assertIdentityInitiation(identityInitiationResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto identityResultResponse = identityResult(interactionId, REVIEW);
        assertIdentityResult(identityResultResponse, getCaseData(caseKey).getMainApplicant(), REVIEW);

        final InteractionResponseDto submitAddressResponse = submitAddress(interactionId);
        assertSubmitAddress(submitAddressResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto submitAccountType = submitIndividualAccountType(interactionId);
        assertSubmitIndividualAccountType(submitAccountType, getCaseData(caseKey));

        final InteractionResponseDto submitCitizenshipTypeResponse = submitCitizenshipType(interactionId,
            CitizenshipType.US_CITIZEN);
        assertSubmitResidentCitizenshipType(submitCitizenshipTypeResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto submitSsnResponse = submitSsn(interactionId);
        assertSubmitSsn(submitSsnResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto submitKycQuestionsResponse = submitKycQuestions(interactionId, amlNoMatch);
        assertSubmitKycQuestionsInReviewDone(submitKycQuestionsResponse);

        return caseKey;
    }

    private String fullFlow_citizenshipInReview_jointAccount() throws Exception {
        final InteractionResponseDto fetchCoApplicantResponse = fetchCoApplicant(null);
        final String interactionId = fetchCoApplicantResponse.getInteractionId();
        assertFetchCoApplicant(fetchCoApplicantResponse);

        final InteractionResponseDto productSelectionListResponse = getProductList(interactionId);
        assertProductSelectionList(productSelectionListResponse);

        final InteractionResponseDto selectProductsResponse = selectProducts(interactionId);
        final String caseKey = getCaseId(UUID.fromString(interactionId));
        assertSelectProducts(selectProductsResponse, getCaseData(caseKey));

        final InteractionResponseDto agreementResponse = agreeToTerms(interactionId);
        assertAgreeWithTerms(agreementResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto anchorDataResponse = submitAnchorData(FIRST_NAME_MOCK_KYC_VALIDATION_OK,
            AGE_18_DOB, interactionId);
        assertThat(caseKey).isNotNull();
        assertSubmitAnchorData(anchorDataResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto requestOtpResponse = requestOtp(interactionId);
        OnboardingAssertions.assertRequestOtp(requestOtpResponse);

        final InteractionResponseDto verifyOtpResponse = verifyOtp(interactionId);
        assertVerifyOtp(verifyOtpResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto verificationTypeResponse = verificationType(interactionId);
        assertVerificationType(verificationTypeResponse);

        final InteractionResponseDto identityInitiationResponse = identityInitiation(interactionId);
        assertIdentityInitiation(identityInitiationResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto identityResultResponse = identityResult(interactionId, APPROVED);
        assertIdentityResult(identityResultResponse, getCaseData(caseKey).getMainApplicant(), APPROVED);

        final InteractionResponseDto submitAddressResponse = submitAddress(interactionId);
        assertSubmitAddress(submitAddressResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto submitAccountType = submitJointAccountType(interactionId);
        assertSubmitJointAccountType(submitAccountType, getCaseData(caseKey));

        final InteractionResponseDto coApplicantAnchorDataResponse = submitCoApplicantAnchorData(
            CO_APPLICANT_FIRST_NAME_MOCK_KYC_VALIDATION_OK, AGE_18_DOB, interactionId);
        assertSubmitCoApplicantAnchorData(coApplicantAnchorDataResponse, getCaseData(caseKey));

        final InteractionResponseDto coApplicantAddressResponse = submitAddress(interactionId);
        assertSubmitCoApplicantAddressData(coApplicantAddressResponse, getCaseData(caseKey));

        final InteractionResponseDto submitCitizenshipTypeResponse = submitCitizenshipType(interactionId,
            CitizenshipType.NON_RESIDENT_ALIEN);
        assertSubmitNonResidentCitizenshipType(submitCitizenshipTypeResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto submitNonResidentDataResponse = submitNonResidentData(interactionId);
        assertSubmitNonResidentData(submitNonResidentDataResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto submitKycQuestionsResponse = submitKycQuestions(interactionId, true);
        assertSubmitKycQuestionsInReviewDone(submitKycQuestionsResponse);

        return caseKey;
    }

    private String fullFlow_idvInReview_jointAccount() throws Exception {
        final InteractionResponseDto fetchCoApplicantResponse = fetchCoApplicant(null);
        final String interactionId = fetchCoApplicantResponse.getInteractionId();
        assertFetchCoApplicant(fetchCoApplicantResponse);

        final InteractionResponseDto productSelectionListResponse = getProductList(interactionId);
        assertProductSelectionList(productSelectionListResponse);

        final InteractionResponseDto selectProductsResponse = selectProducts(interactionId);
        final String caseKey = getCaseId(UUID.fromString(interactionId));
        assertSelectProducts(selectProductsResponse, getCaseData(caseKey));

        final InteractionResponseDto agreementResponse = agreeToTerms(interactionId);
        assertAgreeWithTerms(agreementResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto anchorDataResponse = submitAnchorData(FIRST_NAME_MOCK_KYC_VALIDATION_OK,
            AGE_18_DOB, interactionId);
        assertThat(caseKey).isNotNull();
        assertSubmitAnchorData(anchorDataResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto requestOtpResponse = requestOtp(interactionId);
        OnboardingAssertions.assertRequestOtp(requestOtpResponse);

        final InteractionResponseDto verifyOtpResponse = verifyOtp(interactionId);
        assertVerifyOtp(verifyOtpResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto verificationTypeResponse = verificationType(interactionId);
        assertVerificationType(verificationTypeResponse);

        final InteractionResponseDto identityInitiationResponse = identityInitiation(interactionId);
        assertIdentityInitiation(identityInitiationResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto identityResultResponse = identityResult(interactionId, REVIEW);
        assertIdentityResult(identityResultResponse, getCaseData(caseKey).getMainApplicant(), REVIEW);

        final InteractionResponseDto submitAddressResponse = submitAddress(interactionId);
        assertSubmitAddress(submitAddressResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto submitAccountType = submitJointAccountType(interactionId);
        assertSubmitJointAccountType(submitAccountType, getCaseData(caseKey));

        final InteractionResponseDto coApplicantAnchorDataResponse = submitCoApplicantAnchorData(
            CO_APPLICANT_FIRST_NAME_MOCK_KYC_VALIDATION_OK, AGE_18_DOB, interactionId);
        assertSubmitCoApplicantAnchorData(coApplicantAnchorDataResponse, getCaseData(caseKey));

        final InteractionResponseDto coApplicantAddressResponse = submitAddress(interactionId);
        assertSubmitCoApplicantAddressData(coApplicantAddressResponse, getCaseData(caseKey));

        final InteractionResponseDto submitCitizenshipTypeResponse = submitCitizenshipType(interactionId,
            CitizenshipType.US_CITIZEN);
        assertSubmitResidentCitizenshipType(submitCitizenshipTypeResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto submitSsnResponse = submitSsn(interactionId);
        assertSubmitSsn(submitSsnResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto submitKycQuestionsResponse = submitKycQuestions(interactionId, true);
        assertSubmitKycQuestionsInReviewDone(submitKycQuestionsResponse);

        return caseKey;
    }

    private void fullFlow_citizenshipInReview_coApplicant(String caseKey)
        throws Exception {
        Onboarding caseData = getCaseData(caseKey);
        assertIsCoApplicantFlow(getCaseData(caseKey));

        final InteractionResponseDto fetchCoApplicantResponse = fetchCoApplicant(
            UUID.fromString(caseData.getCoApplicantId()));
        assertFetchCoApplicant_CoApplicant(fetchCoApplicantResponse);

        final String interactionId = fetchCoApplicantResponse.getInteractionId();
        final String actualCaseKey = getCaseId(UUID.fromString(interactionId));
        assertThat(actualCaseKey).isEqualTo(caseKey);

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto agreementResponse = agreeToTerms(interactionId);
        assertAgreeWithTerms(agreementResponse, getCaseData(caseKey).getCoApplicant());

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto prefillAnchorDataResponse = prefillAnchorData(
            UUID.fromString(caseData.getCoApplicantId()), interactionId);
        assertPrefillAnchorData(prefillAnchorDataResponse);

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto anchorDataResponse = submitCoApplicantAnchorData(
            CO_APPLICANT_FIRST_NAME_MOCK_KYC_VALIDATION_OK, AGE_18_DOB,
            interactionId);
        assertSubmitAnchorData(anchorDataResponse, getCaseData(caseKey).getCoApplicant());

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto requestOtpResponse = requestOtpCoApplicant(interactionId);
        assertRequestOtp(requestOtpResponse);

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto verifyOtpResponse = verifyOtpCoApplicant(interactionId);
        assertVerifyOtp(verifyOtpResponse, getCaseData(caseKey).getCoApplicant());

        final InteractionResponseDto verificationTypeResponse = verificationType(interactionId);
        assertVerificationType(verificationTypeResponse);

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto identityInitiationResponse = identityInitiation(interactionId);
        assertIdentityInitiation(identityInitiationResponse, getCaseData(caseKey).getCoApplicant());

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto identityResultResponse = identityResult(interactionId, APPROVED);
        assertIdentityResult(identityResultResponse, getCaseData(caseKey).getCoApplicant(), APPROVED);

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitAddressResponse = submitAddress(interactionId);
        assertSubmitAddress_CoApplicant(submitAddressResponse, getCaseData(caseKey).getCoApplicant());

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitCitizenshipTypeResponse = submitCitizenshipType(interactionId,
            CitizenshipType.NON_RESIDENT_ALIEN);
        assertSubmitNonResidentCitizenshipType(submitCitizenshipTypeResponse, getCaseData(caseKey).getCoApplicant());

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitNonResidentDataResponse = submitNonResidentData(interactionId);
        assertSubmitNonResidentData(submitNonResidentDataResponse, getCaseData(caseKey).getCoApplicant());

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto kycQuestionsResponse = requestKycQuestions(interactionId);
        assertRequestKycQuestions(kycQuestionsResponse);

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitKycQuestionsResponse = submitKycQuestions(interactionId, true);
        assertSubmitKycQuestionsInReviewDone(submitKycQuestionsResponse);

        assertIsCoApplicantFlow(getCaseData(caseKey));
    }

    private void fullFlow_idvInReview_coApplicant(String caseKey, String firstName, boolean amlNoMatch)
        throws Exception {
        Onboarding caseData = getCaseData(caseKey);
        assertIsCoApplicantFlow(getCaseData(caseKey));

        final InteractionResponseDto fetchCoApplicantResponse = fetchCoApplicant(
            UUID.fromString(caseData.getCoApplicantId()));
        assertFetchCoApplicant_CoApplicant(fetchCoApplicantResponse);

        final String interactionId = fetchCoApplicantResponse.getInteractionId();
        final String actualCaseKey = getCaseId(UUID.fromString(interactionId));
        assertThat(actualCaseKey).isEqualTo(caseKey);

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto agreementResponse = agreeToTerms(interactionId);
        assertAgreeWithTerms(agreementResponse, getCaseData(caseKey).getCoApplicant());

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto prefillAnchorDataResponse = prefillAnchorData(
            UUID.fromString(caseData.getCoApplicantId()), interactionId);
        assertPrefillAnchorData(prefillAnchorDataResponse);

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto anchorDataResponse = submitCoApplicantAnchorData(firstName, AGE_18_DOB,
            interactionId);
        assertSubmitAnchorData(anchorDataResponse, getCaseData(caseKey).getCoApplicant());

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto requestOtpResponse = requestOtpCoApplicant(interactionId);
        assertRequestOtp(requestOtpResponse);

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto verifyOtpResponse = verifyOtpCoApplicant(interactionId);
        assertVerifyOtp(verifyOtpResponse, getCaseData(caseKey).getCoApplicant());

        final InteractionResponseDto verificationTypeResponse = verificationType(interactionId);
        assertVerificationType(verificationTypeResponse);

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto identityInitiationResponse = identityInitiation(interactionId);
        assertIdentityInitiation(identityInitiationResponse, getCaseData(caseKey).getCoApplicant());

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto identityResultResponse = identityResult(interactionId, REVIEW);
        assertIdentityResult(identityResultResponse, getCaseData(caseKey).getCoApplicant(), REVIEW);

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitAddressResponse = submitAddress(interactionId);
        assertSubmitAddress_CoApplicant(submitAddressResponse, getCaseData(caseKey).getCoApplicant());

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitCitizenshipTypeResponse = submitCitizenshipType(interactionId,
            CitizenshipType.US_CITIZEN);
        assertSubmitResidentCitizenshipType(submitCitizenshipTypeResponse, getCaseData(caseKey).getMainApplicant());

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitSsnResponse = submitSsn(interactionId);
        assertSubmitSsn(submitSsnResponse, getCaseData(caseKey).getCoApplicant());

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto kycQuestionsResponse = requestKycQuestions(interactionId);
        assertRequestKycQuestions(kycQuestionsResponse);

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitKycQuestionsResponse = submitKycQuestions(interactionId, amlNoMatch);
        assertSubmitKycQuestionsInReviewDone(submitKycQuestionsResponse);

        assertIsCoApplicantFlow(getCaseData(caseKey));
    }

    private String fullFlow_amlInReview() throws Exception {
        final InteractionResponseDto fetchCoApplicantResponse = fetchCoApplicant(null);
        final String interactionId = fetchCoApplicantResponse.getInteractionId();
        assertFetchCoApplicant(fetchCoApplicantResponse);

        final InteractionResponseDto productSelectionListResponse = getProductList(interactionId);
        assertProductSelectionList(productSelectionListResponse);

        final InteractionResponseDto selectProductsResponse = selectProducts(interactionId);
        final String caseKey = getCaseId(UUID.fromString(interactionId));
        assertSelectProducts(selectProductsResponse, getCaseData(caseKey));

        final InteractionResponseDto agreementResponse = agreeToTerms(interactionId);
        assertAgreeWithTerms(agreementResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto anchorDataResponse = submitAnchorData(FIRST_NAME_AML_POTENTIAL_MATCH, AGE_18_DOB,
            interactionId);
        assertSubmitAnchorData(anchorDataResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto requestOtpResponse = requestOtp(interactionId);
        OnboardingAssertions.assertRequestOtp(requestOtpResponse);

        final InteractionResponseDto verifyOtpResponse = verifyOtp(interactionId);
        assertVerifyOtp(verifyOtpResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto verificationTypeResponse = verificationType(interactionId);
        assertVerificationType(verificationTypeResponse);

        final InteractionResponseDto identityInitiationResponse = identityInitiation(interactionId);
        assertIdentityInitiation(identityInitiationResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto identityResultResponse = identityResult(interactionId, APPROVED);
        assertIdentityResult(identityResultResponse, getCaseData(caseKey).getMainApplicant(), APPROVED);

        final InteractionResponseDto submitAddressResponse = submitAddress(interactionId);
        assertSubmitAddress(submitAddressResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto submitAccountType = submitIndividualAccountType(interactionId);
        assertSubmitIndividualAccountType(submitAccountType, getCaseData(caseKey));

        final InteractionResponseDto submitCitizenshipTypeResponse = submitCitizenshipType(interactionId,
            CitizenshipType.US_CITIZEN);
        assertSubmitResidentCitizenshipType(submitCitizenshipTypeResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto submitSsnResponse = submitSsn(interactionId);
        assertSubmitSsn(submitSsnResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto submitKycQuestionsResponse = submitKycQuestions(interactionId, false);
        assertSubmitKycQuestionsInReviewDone(submitKycQuestionsResponse);

        return caseKey;
    }

    private String fullFlow_amlInReview_jointAccount() throws Exception {
        final InteractionResponseDto fetchCoApplicantResponse = fetchCoApplicant(null);
        final String interactionId = fetchCoApplicantResponse.getInteractionId();
        assertFetchCoApplicant(fetchCoApplicantResponse);

        final InteractionResponseDto productSelectionListResponse = getProductList(interactionId);
        assertProductSelectionList(productSelectionListResponse);

        final InteractionResponseDto selectProductsResponse = selectProducts(interactionId);
        final String caseKey = getCaseId(UUID.fromString(interactionId));
        assertSelectProducts(selectProductsResponse, getCaseData(caseKey));

        final InteractionResponseDto agreementResponse = agreeToTerms(interactionId);
        assertAgreeWithTerms(agreementResponse, getCaseData(caseKey).getMainApplicant());

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto anchorDataResponse = submitAnchorData(FIRST_NAME_AML_POTENTIAL_MATCH, AGE_18_DOB,
            interactionId);
        assertSubmitAnchorData(anchorDataResponse, getCaseData(caseKey).getMainApplicant());

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto requestOtpResponse = requestOtp(interactionId);
        OnboardingAssertions.assertRequestOtp(requestOtpResponse);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto verifyOtpResponse = verifyOtp(interactionId);
        assertVerifyOtp(verifyOtpResponse, getCaseData(caseKey).getMainApplicant());

        identityVerificationDefaultStep(interactionId, caseKey);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitAddressResponse = submitAddress(interactionId);
        assertSubmitAddress(submitAddressResponse, getCaseData(caseKey).getMainApplicant());

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitAccountType = submitJointAccountType(interactionId);
        assertSubmitJointAccountType(submitAccountType, getCaseData(caseKey));

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto coApplicantAnchorDataResponse = submitCoApplicantAnchorData(
            CO_APPLICANT_FIRST_NAME_MOCK_KYC_VALIDATION_OK, AGE_18_DOB, interactionId);
        assertSubmitCoApplicantAnchorData(coApplicantAnchorDataResponse, getCaseData(caseKey));

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto coApplicantAddressResponse = submitAddress(interactionId);
        assertSubmitCoApplicantAddressData(coApplicantAddressResponse, getCaseData(caseKey));

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitCitizenshipTypeResponse = submitCitizenshipType(interactionId,
            CitizenshipType.US_CITIZEN);
        assertSubmitResidentCitizenshipType(submitCitizenshipTypeResponse, getCaseData(caseKey).getMainApplicant());

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitSsnResponse = submitSsn(interactionId);
        assertSubmitSsn(submitSsnResponse, getCaseData(caseKey).getMainApplicant());

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitKycQuestionsResponse = submitKycQuestions(interactionId, false);
        assertSubmitKycQuestionsInReviewDone(submitKycQuestionsResponse);

        assertIsMainApplicantFlow(getCaseData(caseKey));

        return caseKey;
    }

    private void identityVerificationDefaultStep(String interactionId, String caseKey) throws Exception {
        final InteractionResponseDto verificationTypeResponse = verificationType(interactionId);
        assertVerificationType(verificationTypeResponse);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto identityInitiationResponse = identityInitiation(interactionId);
        assertIdentityInitiation(identityInitiationResponse, getCaseData(caseKey).getMainApplicant());

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto identityResultResponse = identityResult(interactionId, APPROVED);
        assertIdentityResult(identityResultResponse, getCaseData(caseKey).getMainApplicant(), APPROVED);
    }

    private void fullFlow_amlInReview_coApplicant(String caseKey) throws Exception {
        Onboarding caseData = getCaseData(caseKey);

        final InteractionResponseDto fetchCoApplicantResponse = fetchCoApplicant(
            UUID.fromString(caseData.getCoApplicantId()));
        assertFetchCoApplicant_CoApplicant(fetchCoApplicantResponse);

        final String interactionId = fetchCoApplicantResponse.getInteractionId();
        final String actualCaseKey = getCaseId(UUID.fromString(interactionId));
        assertThat(actualCaseKey).isEqualTo(caseKey);

        final InteractionResponseDto agreementResponse = agreeToTerms(interactionId);
        assertAgreeWithTerms(agreementResponse, getCaseData(caseKey).getCoApplicant());

        final InteractionResponseDto prefillAnchorDataResponse = prefillAnchorData(
            UUID.fromString(caseData.getCoApplicantId()), interactionId);
        assertPrefillAnchorData(prefillAnchorDataResponse);

        final InteractionResponseDto anchorDataResponse = submitCoApplicantAnchorData(FIRST_NAME_AML_POTENTIAL_MATCH,
            AGE_18_DOB, interactionId);
        assertSubmitAnchorData(anchorDataResponse, getCaseData(caseKey).getCoApplicant());

        final InteractionResponseDto requestOtpResponse = requestOtpCoApplicant(interactionId);
        OnboardingAssertions.assertRequestOtp(requestOtpResponse);

        final InteractionResponseDto verifyOtpResponse = verifyOtpCoApplicant(interactionId);
        assertVerifyOtp(verifyOtpResponse, getCaseData(caseKey).getCoApplicant());

        final InteractionResponseDto verificationTypeResponse = verificationType(interactionId);
        assertVerificationType(verificationTypeResponse);

        final InteractionResponseDto identityInitiationResponse = identityInitiation(interactionId);
        assertIdentityInitiation(identityInitiationResponse, getCaseData(caseKey).getCoApplicant());

        final InteractionResponseDto identityResultResponse = identityResult(interactionId, APPROVED);
        assertIdentityResult(identityResultResponse, getCaseData(caseKey).getCoApplicant(), APPROVED);

        final InteractionResponseDto submitAddressResponse = submitAddress(interactionId);
        assertSubmitAddress_CoApplicant(submitAddressResponse, getCaseData(caseKey).getCoApplicant());

        final InteractionResponseDto submitCitizenshipTypeResponse = submitCitizenshipType(interactionId,
            CitizenshipType.US_CITIZEN);
        assertSubmitResidentCitizenshipType(submitCitizenshipTypeResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto submitSsnResponse = submitSsn(interactionId);
        assertSubmitSsn(submitSsnResponse, getCaseData(caseKey).getCoApplicant());

        final InteractionResponseDto submitKycQuestionsResponse = submitKycQuestions(interactionId, false);
        assertSubmitKycQuestionsInReviewDone(submitKycQuestionsResponse);
    }

}