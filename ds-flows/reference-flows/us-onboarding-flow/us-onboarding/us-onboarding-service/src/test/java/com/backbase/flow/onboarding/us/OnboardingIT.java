package com.backbase.flow.onboarding.us;

import static com.backbase.flow.onboarding.us.helpers.AccountTypeStepCalls.submitIndividualAccountType;
import static com.backbase.flow.onboarding.us.helpers.AccountTypeStepCalls.submitJointAccountType;
import static com.backbase.flow.onboarding.us.helpers.AddressStepCalls.submitAddress;
import static com.backbase.flow.onboarding.us.helpers.AddressStepCalls.submitInvalidAddress;
import static com.backbase.flow.onboarding.us.helpers.AnchorStepCalls.prefillAnchorData;
import static com.backbase.flow.onboarding.us.helpers.AnchorStepCalls.submitAnchorData;
import static com.backbase.flow.onboarding.us.helpers.AnchorStepCalls.submitCoApplicantAnchorData;
import static com.backbase.flow.onboarding.us.helpers.CitizenshipStepCalls.fetchCitizenshipInfo;
import static com.backbase.flow.onboarding.us.helpers.CitizenshipStepCalls.submitCitizenshipType;
import static com.backbase.flow.onboarding.us.helpers.IdentityVerificationStepCalls.identityInitiation;
import static com.backbase.flow.onboarding.us.helpers.IdentityVerificationStepCalls.identityResult;
import static com.backbase.flow.onboarding.us.helpers.IdentityVerificationStepCalls.verificationType;
import static com.backbase.flow.onboarding.us.helpers.InitStepCalls.fetchCoApplicant;
import static com.backbase.flow.onboarding.us.helpers.KycStepCalls.requestKycQuestions;
import static com.backbase.flow.onboarding.us.helpers.KycStepCalls.submitKycQuestions;
import static com.backbase.flow.onboarding.us.helpers.NonResidentDataStepCalls.submitNonResidentData;
import static com.backbase.flow.onboarding.us.helpers.OtpVerificationStepCalls.fetchOtpData;
import static com.backbase.flow.onboarding.us.helpers.OtpVerificationStepCalls.requestOtp;
import static com.backbase.flow.onboarding.us.helpers.OtpVerificationStepCalls.requestOtpCoApplicant;
import static com.backbase.flow.onboarding.us.helpers.OtpVerificationStepCalls.verifyOtp;
import static com.backbase.flow.onboarding.us.helpers.OtpVerificationStepCalls.verifyOtpCoApplicant;
import static com.backbase.flow.onboarding.us.helpers.OtpVerificationStepCalls.verifyWithInvalidCode;
import static com.backbase.flow.onboarding.us.helpers.OtpVerificationStepCalls.verifyWithInvalidOtp;
import static com.backbase.flow.onboarding.us.helpers.ProductSelectionListStepCalls.getProductList;
import static com.backbase.flow.onboarding.us.helpers.SelectProductsStepCalls.selectProducts;
import static com.backbase.flow.onboarding.us.helpers.SetupCredentialsStepCalls.setupCredentials;
import static com.backbase.flow.onboarding.us.helpers.SsnStepCalls.submitIncorrectSsn;
import static com.backbase.flow.onboarding.us.helpers.SsnStepCalls.submitSsn;
import static com.backbase.flow.onboarding.us.helpers.TermsAndConditionsStepCalls.agreeToTerms;
import static com.backbase.flow.onboarding.us.helpers.it.BaseAssertions.assertIsCoApplicantFlow;
import static com.backbase.flow.onboarding.us.helpers.it.BaseAssertions.assertIsMainApplicantFlow;
import static com.backbase.flow.onboarding.us.helpers.it.BaseAssertions.assertRetrieveVerifyNonResidentData;
import static com.backbase.flow.onboarding.us.helpers.it.BaseAssertions.assertCompleteVerifyNonResidentDataTask;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.ADDRESS_STEP;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.ANCHOR_DATA_STEP;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.OTP_VERIFICATION_STEP;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.SSN_STEP;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertAgreeWithTerms;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertAmlUserTaskCanBeCompletedSuccessfullyByUser;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertCaseDataAnchorDataFilled;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertCaseDataIdentityVerificationInitiationFilled;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertCaseDataIdentityVerificationResultFilled;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertCaseIsArchived;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertCaseStatus;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertFetchCitizenshipDataAfterSubmit;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertFetchCitizenshipDataAfterSubmitNonResident;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertFetchCitizenshipDataBeforeSubmit;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertFetchCoApplicant;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertFetchCoApplicant_CoApplicant;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertFetchOtpData;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertIdentityInitiation;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertIdentityResult;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertIdtAddressValidationTaskCompleted;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertIdtAddressValidationTaskIsOpen;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertIdtCitizenshipTaskCompleted;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertIdtCitizenshipTaskIsOpen;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertIdtIdvTaskCompleted;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertIdtIdvTaskIsOpen;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertInvalidOtpCode;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertIdtKycTaskIsOpen;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertIdtKycTaskCompleted;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertPrefillAnchorData;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertProductSelectionList;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertRequestKycQuestions;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertRequestOtp;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSelectProducts;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSetupCredentials;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSetupCredentialsJointAccountMainApplicant;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitAddress;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitAddress_CoApplicant;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitAnchorData;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitCoApplicantAddressData;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitCoApplicantAnchorData;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitIndividualAccountType;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitJointAccountType;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitKycQuestionsDeclined;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitKycQuestionsIdentityVerified;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitKycQuestionsInReviewDone;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitNonResidentCitizenshipType;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitNonResidentData;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitResidentCitizenshipType;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitSsn;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertUserTaskDataCanBeRetrievedCorrectlyByUser;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertUserTaskIsCreatedCorrectly;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertVerificationType;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertVerifyOtp;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.getCaseData;
import static org.assertj.core.api.Assertions.assertThat;

import com.backbase.flow.application.uso.casedata.CitizenshipInfo.CitizenshipType;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.interaction.api.model.InteractionResponseDto;
import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OnboardingIT extends BaseIT {

    private static final String FIRST_NAME_MOCK_KYC_VALIDATION_OK = "John";
    private static final String CO_APPLICANT_FIRST_NAME_MOCK_KYC_VALIDATION_OK = "Jane";
    private static final String AGE_18_DOB = LocalDate.now().minusYears(18).toString();
    private static final String AGE_BELOW_18_DOB = LocalDate.now().minusYears(17).toString();
    private static final String AGE_ABOVE_100_DOB = LocalDate.now().minusYears(101).toString();

    private static final String APPROVED = "approved";
    private static final String REVIEW = "review";
    private static final String FAIL = "fail";
    private static final String APPROVED_AFTER_RETRY = "approved_after_retry";
    private static final String NO_RESULT = "no_result";

    private static final String DOCUMENT_STATUS_APPROVED = "APPROVED_VERIFIED";
    private static final int JUST_TO_BE_SURE_TIMEOUT = 1000;

    @BeforeEach
    public void beforeEach() {
        setInteraction("onboarding");
    }

    @Test
    void happyFlow() throws Exception {
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
        assertSubmitAnchorData(anchorDataResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto fetchOtpDataResponse = fetchOtpData(interactionId);
        assertFetchOtpData(fetchOtpDataResponse);

        final InteractionResponseDto requestOtpResponse = requestOtp(interactionId);
        assertRequestOtp(requestOtpResponse);

        final InteractionResponseDto verifyOtpResponse = verifyOtp(interactionId);
        // not resuming previous case
        final String actualCaseKey = getCaseId(UUID.fromString(interactionId));
        assertThat(actualCaseKey).isEqualTo(caseKey);
        assertVerifyOtp(verifyOtpResponse, getCaseData(caseKey).getMainApplicant());

        identityVerificationDefaultStep(interactionId, caseKey, APPROVED);

        assertIdtAddressValidationTaskIsOpen(caseKey);
        final InteractionResponseDto submitAddressResponse = submitAddress(interactionId);
        assertSubmitAddress(submitAddressResponse, getCaseData(caseKey).getMainApplicant());
        assertIdtAddressValidationTaskCompleted(caseKey);

        final InteractionResponseDto submitAccountType = submitIndividualAccountType(interactionId);
        assertSubmitIndividualAccountType(submitAccountType, getCaseData(caseKey));

        assertIdtCitizenshipTaskIsOpen(caseKey);
        final InteractionResponseDto fetchCitizenshipInfoBeforeSubmitResponse = fetchCitizenshipInfo(interactionId);
        assertFetchCitizenshipDataBeforeSubmit(fetchCitizenshipInfoBeforeSubmitResponse,
            getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto submitCitizenshipTypeResponse = submitCitizenshipType(interactionId,
            CitizenshipType.US_CITIZEN);
        assertSubmitResidentCitizenshipType(submitCitizenshipTypeResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto fetchCitizenshipInfoBeforeAfterResponse = fetchCitizenshipInfo(interactionId);
        assertFetchCitizenshipDataAfterSubmit(fetchCitizenshipInfoBeforeAfterResponse,
            getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto submitSsnResponse = submitSsn(interactionId);
        assertSubmitSsn(submitSsnResponse, getCaseData(caseKey).getMainApplicant());
        assertIdtCitizenshipTaskCompleted(caseKey);

        assertIdtKycTaskIsOpen(caseKey);
        final InteractionResponseDto kycQuestionsResponse = requestKycQuestions(interactionId);
        assertRequestKycQuestions(kycQuestionsResponse);

        final InteractionResponseDto submitKycQuestionsResponse = submitKycQuestions(interactionId, true);
        assertSubmitKycQuestionsIdentityVerified(submitKycQuestionsResponse, getCaseData(caseKey).getMainApplicant(),
            APPROVED);
        assertIdtKycTaskCompleted(caseKey);

        final InteractionResponseDto setupCredentialsResponse = setupCredentials(interactionId);
        assertSetupCredentials(setupCredentialsResponse, getCaseData(caseKey).getMainApplicant());

        assertCaseStatus(caseKey, "Primary case status", "open", true);
    }

    private void identityVerificationDefaultStep(String interactionId, String caseKey, String mockedResult) throws Exception {
        assertIdtIdvTaskIsOpen(caseKey);
        final InteractionResponseDto verificationTypeResponse = verificationType(interactionId);
        assertVerificationType(verificationTypeResponse);

        final InteractionResponseDto identityInitiationResponse = identityInitiation(interactionId);
        assertIdentityInitiation(identityInitiationResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto identityResultResponse = identityResult(interactionId, mockedResult);
        assertIdentityResult(identityResultResponse, getCaseData(caseKey).getMainApplicant(), mockedResult);
        assertIdtIdvTaskCompleted(caseKey);
    }

    @Test
    void happyFlow_PermanentResident() throws Exception {
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
        assertSubmitAnchorData(anchorDataResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto fetchOtpDataResponse = fetchOtpData(interactionId);
        assertFetchOtpData(fetchOtpDataResponse);

        final InteractionResponseDto requestOtpResponse = requestOtp(interactionId);
        assertRequestOtp(requestOtpResponse);

        final InteractionResponseDto verifyOtpResponse = verifyOtp(interactionId);
        // not resuming previous case
        final String actualCaseKey = getCaseId(UUID.fromString(interactionId));
        assertThat(actualCaseKey).isEqualTo(caseKey);
        assertVerifyOtp(verifyOtpResponse, getCaseData(caseKey).getMainApplicant());

        identityVerificationDefaultStep(interactionId, caseKey, APPROVED);

        assertIdtAddressValidationTaskIsOpen(caseKey);
        final InteractionResponseDto submitAddressResponse = submitAddress(interactionId);
        assertSubmitAddress(submitAddressResponse, getCaseData(caseKey).getMainApplicant());
        assertIdtAddressValidationTaskCompleted(caseKey);

        final InteractionResponseDto submitAccountType = submitIndividualAccountType(interactionId);
        assertSubmitIndividualAccountType(submitAccountType, getCaseData(caseKey));

        assertIdtCitizenshipTaskIsOpen(caseKey);
        final InteractionResponseDto fetchCitizenshipInfoBeforeSubmitResponse = fetchCitizenshipInfo(interactionId);
        assertFetchCitizenshipDataBeforeSubmit(fetchCitizenshipInfoBeforeSubmitResponse,
            getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto submitCitizenshipTypeResponse = submitCitizenshipType(interactionId,
            CitizenshipType.PERMANENT_RESIDENT);
        assertSubmitResidentCitizenshipType(submitCitizenshipTypeResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto fetchCitizenshipInfoBeforeAfterResponse = fetchCitizenshipInfo(interactionId);
        assertFetchCitizenshipDataAfterSubmit(fetchCitizenshipInfoBeforeAfterResponse,
            getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto submitSsnResponse = submitSsn(interactionId);
        assertSubmitSsn(submitSsnResponse, getCaseData(caseKey).getMainApplicant());
        assertIdtCitizenshipTaskCompleted(caseKey);

        assertIdtKycTaskIsOpen(caseKey);
        final InteractionResponseDto kycQuestionsResponse = requestKycQuestions(interactionId);
        assertRequestKycQuestions(kycQuestionsResponse);

        final InteractionResponseDto submitKycQuestionsResponse = submitKycQuestions(interactionId, true);
        assertSubmitKycQuestionsIdentityVerified(submitKycQuestionsResponse, getCaseData(caseKey).getMainApplicant(),
            APPROVED);
        assertIdtKycTaskCompleted(caseKey);

        assertCaseStatus(caseKey, "Primary case status", "open", true);
    }

    @Test
    void happyFlow_NonResidentAlien_createsTask() throws Exception {
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
        assertSubmitAnchorData(anchorDataResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto fetchOtpDataResponse = fetchOtpData(interactionId);
        assertFetchOtpData(fetchOtpDataResponse);

        final InteractionResponseDto requestOtpResponse = requestOtp(interactionId);
        assertRequestOtp(requestOtpResponse);

        final InteractionResponseDto verifyOtpResponse = verifyOtp(interactionId);
        // not resuming previous case
        final String actualCaseKey = getCaseId(UUID.fromString(interactionId));
        assertThat(actualCaseKey).isEqualTo(caseKey);
        assertVerifyOtp(verifyOtpResponse, getCaseData(caseKey).getMainApplicant());

        identityVerificationDefaultStep(interactionId, caseKey, APPROVED);

        assertIdtAddressValidationTaskIsOpen(caseKey);
        final InteractionResponseDto submitAddressResponse = submitAddress(interactionId);
        assertSubmitAddress(submitAddressResponse, getCaseData(caseKey).getMainApplicant());
        assertIdtAddressValidationTaskCompleted(caseKey);

        final InteractionResponseDto submitAccountType = submitIndividualAccountType(interactionId);
        assertSubmitIndividualAccountType(submitAccountType, getCaseData(caseKey));

        assertIdtCitizenshipTaskIsOpen(caseKey);
        final InteractionResponseDto fetchCitizenshipInfoBeforeSubmitResponse = fetchCitizenshipInfo(interactionId);
        assertFetchCitizenshipDataBeforeSubmit(fetchCitizenshipInfoBeforeSubmitResponse,
            getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto submitCitizenshipTypeResponse = submitCitizenshipType(interactionId,
            CitizenshipType.NON_RESIDENT_ALIEN);
        assertSubmitNonResidentCitizenshipType(submitCitizenshipTypeResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto fetchCitizenshipInfoBeforeAfterResponse = fetchCitizenshipInfo(interactionId);
        assertFetchCitizenshipDataAfterSubmitNonResident(fetchCitizenshipInfoBeforeAfterResponse,
            getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto submitNonResidentDataResponse = submitNonResidentData(interactionId);
        assertSubmitNonResidentData(submitNonResidentDataResponse, getCaseData(caseKey).getMainApplicant());
        assertIdtCitizenshipTaskCompleted(caseKey);

        assertIdtKycTaskIsOpen(caseKey);
        final InteractionResponseDto kycQuestionsResponse = requestKycQuestions(interactionId);
        assertRequestKycQuestions(kycQuestionsResponse);

        final InteractionResponseDto submitKycQuestionsResponse = submitKycQuestions(interactionId, true);
        assertSubmitKycQuestionsInReviewDone(submitKycQuestionsResponse);
        assertIdtKycTaskCompleted(caseKey);

        final String taskId = assertUserTaskIsCreatedCorrectly("Decide on validity of data from W-8BEN form", caseKey,
            true);
        assertRetrieveVerifyNonResidentData(taskId, caseKey);
        assertCompleteVerifyNonResidentDataTask(taskId, true);

        final Onboarding onboarding = getCaseData(caseKey);
        assertThat(onboarding.getMainApplicant().getEmail()).isNotNull();
        assertThat(onboarding.getMainApplicant().getCitizenship().getCitizenshipReview()).isNotNull();
        assertThat(onboarding.getMainApplicant().getCitizenship().getCitizenshipReview().getApproved()).isNotNull();
        assertThat(onboarding.getMainApplicant().getCitizenship().getCitizenshipReview().getNeeded()).isNotNull();
    }

    @Test
    void happyFlow_idvRetry() throws Exception {
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
        assertSubmitAnchorData(anchorDataResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto requestOtpResponse = requestOtp(interactionId);
        assertRequestOtp(requestOtpResponse);

        final InteractionResponseDto verifyOtpResponse = verifyOtp(interactionId);
        assertVerifyOtp(verifyOtpResponse, getCaseData(caseKey).getMainApplicant());

        identityVerificationDefaultStep(interactionId, caseKey, APPROVED_AFTER_RETRY);

        assertIdtAddressValidationTaskIsOpen(caseKey);
        final InteractionResponseDto submitAddressResponse = submitAddress(interactionId);
        assertSubmitAddress(submitAddressResponse, getCaseData(caseKey).getMainApplicant());
        assertIdtAddressValidationTaskCompleted(caseKey);

        final InteractionResponseDto submitAccountType = submitIndividualAccountType(interactionId);
        assertSubmitIndividualAccountType(submitAccountType, getCaseData(caseKey));

        assertIdtCitizenshipTaskIsOpen(caseKey);
        final InteractionResponseDto submitCitizenshipTypeResponse = submitCitizenshipType(interactionId,
            CitizenshipType.US_CITIZEN);
        assertSubmitResidentCitizenshipType(submitCitizenshipTypeResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto submitSsnResponse = submitSsn(interactionId);
        assertSubmitSsn(submitSsnResponse, getCaseData(caseKey).getMainApplicant());
        assertIdtCitizenshipTaskCompleted(caseKey);

        assertIdtKycTaskIsOpen(caseKey);
        final InteractionResponseDto kycQuestionsResponse = requestKycQuestions(interactionId);
        assertRequestKycQuestions(kycQuestionsResponse);

        final InteractionResponseDto submitKycQuestionsResponse = submitKycQuestions(interactionId, true);
        assertSubmitKycQuestionsInReviewDone(submitKycQuestionsResponse);
        assertIdtKycTaskCompleted(caseKey);

        assertCaseStatus(caseKey, "Primary case status", "open", true);

        Supplier<String> documentStatus = () -> getCaseData(caseKey)
            .getMainApplicant()
            .getIdentityVerificationResult()
            .getDocumentStatus();

        if (!DOCUMENT_STATUS_APPROVED.equals(documentStatus.get())) {
            long timeoutForIdvRetry = getTimeoutForIdvRetry(1);
            TimeUnit.MILLISECONDS.sleep(timeoutForIdvRetry);
        }
        assertThat(documentStatus.get()).isEqualTo(DOCUMENT_STATUS_APPROVED);
    }

    private long getTimeoutForIdvRetry(int retrieveAttempts) {
        String[] idvDelayDurations = springContext.getEnvironment()
            .getProperty("backbase.flow.integrations.identity-verification.retry-delay-durations",
                String[].class);
        Long jobExecutionMaxWaitInMillis = springContext.getEnvironment()
            .getProperty("camunda.bpm.job-execution.max-wait", Long.class);
        return Duration.parse(idvDelayDurations[retrieveAttempts - 1]).toMillis()
            + jobExecutionMaxWaitInMillis
            + JUST_TO_BE_SURE_TIMEOUT;
    }

    @Test
    void happyFlow_jointAccount_WithoutCoApplicant() throws Exception {
        happyFlow_jointAccount();
    }

    @Test
    void happyFlow_JointAccount_WithCoApplicant() throws Exception {
        String caseKey = happyFlow_jointAccount();
        happyFlow_coApplicant(caseKey);
    }

    @Test
    void ageOutsideRangeOf18And100() throws Exception {
        InteractionResponseDto fetchCoApplicantResponse = fetchCoApplicant(null);
        String interactionId = fetchCoApplicantResponse.getInteractionId();
        assertFetchCoApplicant(fetchCoApplicantResponse);

        InteractionResponseDto productSelectionListResponse = getProductList(interactionId);
        assertProductSelectionList(productSelectionListResponse);

        InteractionResponseDto selectProductsResponse = selectProducts(interactionId);
        String caseKey = getCaseId(UUID.fromString(interactionId));
        assertSelectProducts(selectProductsResponse, getCaseData(caseKey));

        InteractionResponseDto agreementResponse = agreeToTerms(interactionId);
        final String below18interactionId = agreementResponse.getInteractionId();
        final String below18caseKey = getCaseId(UUID.fromString(below18interactionId));
        assertAgreeWithTerms(agreementResponse, getCaseData(below18caseKey).getMainApplicant());

        final InteractionResponseDto responseBelow18 = submitAnchorData(FIRST_NAME_MOCK_KYC_VALIDATION_OK,
            AGE_BELOW_18_DOB, below18interactionId);

        assertThat(responseBelow18.getActionErrors()).isNotEmpty();
        assertThat(responseBelow18.getStep().getStatus()).isNull();
        assertThat(responseBelow18.getStep().getName()).isEqualTo(ANCHOR_DATA_STEP);
        assertCaseDataAnchorDataFilled(getCaseData(below18caseKey).getMainApplicant());

        fetchCoApplicantResponse = fetchCoApplicant(null);
        interactionId = fetchCoApplicantResponse.getInteractionId();
        assertFetchCoApplicant(fetchCoApplicantResponse);

        productSelectionListResponse = getProductList(interactionId);
        assertProductSelectionList(productSelectionListResponse);

        selectProductsResponse = selectProducts(interactionId);
        caseKey = getCaseId(UUID.fromString(interactionId));
        assertSelectProducts(selectProductsResponse, getCaseData(caseKey));

        agreementResponse = agreeToTerms(interactionId);
        final String above100interactionId = agreementResponse.getInteractionId();
        final String above100caseKey = getCaseId(UUID.fromString(above100interactionId));
        assertAgreeWithTerms(agreementResponse, getCaseData(above100caseKey).getMainApplicant());

        final InteractionResponseDto responseAbove100 = submitAnchorData(FIRST_NAME_MOCK_KYC_VALIDATION_OK,
            AGE_ABOVE_100_DOB, above100interactionId);

        assertThat(responseAbove100.getActionErrors()).isNotEmpty();
        assertThat(responseAbove100.getStep().getStatus()).isNull();
        assertThat(responseAbove100.getStep().getName()).isEqualTo(ANCHOR_DATA_STEP);
        assertCaseDataAnchorDataFilled(getCaseData(above100caseKey).getMainApplicant());
    }

    @Test
    void addressNotPassingVerificationNotPoBox() throws Exception {
        invalidAddressFlow();
    }

    @Test
    void invalidSsn() throws Exception {
        invalidSsnFlow();
    }

    @Test
    void incorrectOtp() throws Exception {
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
        assertSubmitAnchorData(anchorDataResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto requestOtpResponse = requestOtp(interactionId);
        assertRequestOtp(requestOtpResponse);

        final InteractionResponseDto verifyOtpResponse = verifyWithInvalidOtp(interactionId);
        assertThat(verifyOtpResponse.getStep().getStatus()).isNull();
        assertThat(verifyOtpResponse.getStep().getName()).isEqualTo(OTP_VERIFICATION_STEP);
        assertThat(verifyOtpResponse.getActionErrors().size()).isEqualTo(1);

        final Onboarding onboarding = getCaseData(caseKey);
        assertThat(onboarding.getMainApplicant().getPhoneNumberVerified()).isFalse();
    }

    @Test
    void amlMatchCreatesTask() throws Exception {
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
        final InteractionResponseDto anchorDataResponse = submitAnchorData("potential_match", AGE_18_DOB,
            interactionId);
        assertSubmitAnchorData(anchorDataResponse, getCaseData(caseKey).getMainApplicant());

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto requestOtpResponse = requestOtp(interactionId);
        assertRequestOtp(requestOtpResponse);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto verifyOtpResponse = verifyOtp(interactionId);
        assertVerifyOtp(verifyOtpResponse, getCaseData(caseKey).getMainApplicant());

        identityVerificationDefaultStep(interactionId, caseKey, APPROVED);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        assertIdtAddressValidationTaskIsOpen(caseKey);
        final InteractionResponseDto submitAddressResponse = submitAddress(interactionId);
        assertSubmitAddress(submitAddressResponse, getCaseData(caseKey).getMainApplicant());
        assertIdtAddressValidationTaskCompleted(caseKey);

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
        assertIdtCitizenshipTaskIsOpen(caseKey);
        final InteractionResponseDto submitCitizenshipTypeResponse = submitCitizenshipType(interactionId,
            CitizenshipType.US_CITIZEN);
        assertSubmitResidentCitizenshipType(submitCitizenshipTypeResponse, getCaseData(caseKey).getMainApplicant());

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitSsnResponse = submitSsn(interactionId);
        assertSubmitSsn(submitSsnResponse, getCaseData(caseKey).getMainApplicant());
        assertIdtCitizenshipTaskCompleted(caseKey);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        assertIdtKycTaskIsOpen(caseKey);
        final InteractionResponseDto kycQuestionsResponse = requestKycQuestions(interactionId);
        assertRequestKycQuestions(kycQuestionsResponse);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitKycQuestionsResponse = submitKycQuestions(interactionId, false);
        assertSubmitKycQuestionsInReviewDone(submitKycQuestionsResponse);
        assertIdtKycTaskCompleted(caseKey);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final String taskId = assertUserTaskIsCreatedCorrectly("Review AML hit", caseKey, true);
        assertUserTaskDataCanBeRetrievedCorrectlyByUser(taskId, caseKey);
        assertAmlUserTaskCanBeCompletedSuccessfullyByUser(taskId, false);

        final Onboarding onboarding = getCaseData(caseKey);
        assertThat(onboarding.getMainApplicant().getEmail()).isNotNull();
        assertThat(onboarding.getMainApplicant().getAntiMoneyLaunderingInfo()).isNotNull();
        assertThat(onboarding.getMainApplicant().getAntiMoneyLaunderingInfo().getAmlResult().getMatchStatus())
            .isNotNull();
        assertThat(onboarding.getMainApplicant().getAntiMoneyLaunderingInfo().getAmlResult().getMatchStatus())
            .isEqualToIgnoringCase("potential_match");

        assertIsMainApplicantFlow(getCaseData(caseKey));
    }

    @Test
    void fullFlow_noIdvResult_showInReviewAsLastStep() throws Exception {
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
        assertRequestOtp(requestOtpResponse);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto verifyOtpResponse = verifyOtp(interactionId);
        assertVerifyOtp(verifyOtpResponse, getCaseData(caseKey).getMainApplicant());

        identityVerificationDefaultStep(interactionId, caseKey, NO_RESULT);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        assertIdtAddressValidationTaskIsOpen(caseKey);
        final InteractionResponseDto submitAddressResponse = submitAddress(interactionId);
        assertSubmitAddress(submitAddressResponse, getCaseData(caseKey).getMainApplicant());
        assertIdtAddressValidationTaskCompleted(caseKey);

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
        assertIdtCitizenshipTaskIsOpen(caseKey);
        final InteractionResponseDto submitCitizenshipTypeResponse = submitCitizenshipType(interactionId,
            CitizenshipType.US_CITIZEN);
        assertSubmitResidentCitizenshipType(submitCitizenshipTypeResponse, getCaseData(caseKey).getMainApplicant());

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitSsnResponse = submitSsn(interactionId);
        assertSubmitSsn(submitSsnResponse, getCaseData(caseKey).getMainApplicant());
        assertIdtCitizenshipTaskCompleted(caseKey);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        assertIdtKycTaskIsOpen(caseKey);
        final InteractionResponseDto kycQuestionsResponse = requestKycQuestions(interactionId);
        assertRequestKycQuestions(kycQuestionsResponse);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitKycQuestionsResponse = submitKycQuestions(interactionId, true);
        assertSubmitKycQuestionsInReviewDone(submitKycQuestionsResponse);
        assertIdtKycTaskCompleted(caseKey);

        assertIsMainApplicantFlow(getCaseData(caseKey));
    }

    @Test
    void fullFlow_reviewResponseOnIDV_doesNotShowIdentityVerifiedAndShowInReviewAsLastStep() throws Exception {
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
        assertRequestOtp(requestOtpResponse);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto verifyOtpResponse = verifyOtp(interactionId);
        assertVerifyOtp(verifyOtpResponse, getCaseData(caseKey).getMainApplicant());

        identityVerificationDefaultStep(interactionId, caseKey, REVIEW);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        assertIdtAddressValidationTaskIsOpen(caseKey);
        final InteractionResponseDto submitAddressResponse = submitAddress(interactionId);
        assertSubmitAddress(submitAddressResponse, getCaseData(caseKey).getMainApplicant());
        assertIdtAddressValidationTaskCompleted(caseKey);

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
        assertIdtCitizenshipTaskIsOpen(caseKey);
        final InteractionResponseDto submitCitizenshipTypeResponse = submitCitizenshipType(interactionId,
            CitizenshipType.US_CITIZEN);
        assertSubmitResidentCitizenshipType(submitCitizenshipTypeResponse, getCaseData(caseKey).getMainApplicant());

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitSsnResponse = submitSsn(interactionId);
        assertSubmitSsn(submitSsnResponse, getCaseData(caseKey).getMainApplicant());
        assertIdtCitizenshipTaskCompleted(caseKey);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        assertIdtKycTaskIsOpen(caseKey);
        final InteractionResponseDto kycQuestionsResponse = requestKycQuestions(interactionId);
        assertRequestKycQuestions(kycQuestionsResponse);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitKycQuestionsResponse = submitKycQuestions(interactionId, true);
        assertSubmitKycQuestionsInReviewDone(submitKycQuestionsResponse);
        assertIdtKycTaskCompleted(caseKey);

        assertIsMainApplicantFlow(getCaseData(caseKey));
    }

    @Test
    void fullFlow_deniedResponseOnIDV_goesToDeclinedAsLastStep() throws Exception {
        final InteractionResponseDto fetchCoApplicantResponse = fetchCoApplicant(null);
        final String interactionId = fetchCoApplicantResponse.getInteractionId();
        assertFetchCoApplicant(fetchCoApplicantResponse);

        final InteractionResponseDto productSelectionListResponse = getProductList(interactionId);
        assertProductSelectionList(productSelectionListResponse);

        final InteractionResponseDto selectProductsResponse = selectProducts(interactionId);
        final String caseKey = getCaseId(UUID.fromString(interactionId));
        assertThat(caseKey).isNotNull();
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
        assertRequestOtp(requestOtpResponse);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto verifyOtpResponse = verifyOtp(interactionId);
        assertVerifyOtp(verifyOtpResponse, getCaseData(caseKey).getMainApplicant());

        identityVerificationDefaultStep(interactionId, caseKey, FAIL);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        assertIdtAddressValidationTaskIsOpen(caseKey);
        final InteractionResponseDto submitAddressResponse = submitAddress(interactionId);
        assertSubmitAddress(submitAddressResponse, getCaseData(caseKey).getMainApplicant());
        assertIdtAddressValidationTaskCompleted(caseKey);

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
        assertIdtCitizenshipTaskIsOpen(caseKey);
        final InteractionResponseDto submitCitizenshipTypeResponse = submitCitizenshipType(interactionId,
            CitizenshipType.US_CITIZEN);
        assertSubmitResidentCitizenshipType(submitCitizenshipTypeResponse, getCaseData(caseKey).getMainApplicant());

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitSsnResponse = submitSsn(interactionId);
        assertSubmitSsn(submitSsnResponse, getCaseData(caseKey).getMainApplicant());
        assertIdtCitizenshipTaskCompleted(caseKey);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        assertIdtKycTaskIsOpen(caseKey);
        final InteractionResponseDto kycQuestionsResponse = requestKycQuestions(interactionId);
        assertRequestKycQuestions(kycQuestionsResponse);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitKycQuestionsResponse = submitKycQuestions(interactionId, true);
        assertSubmitKycQuestionsDeclined(submitKycQuestionsResponse);
        assertIdtKycTaskCompleted(caseKey);

        assertCaseIsArchived(caseKey);
        assertCaseStatus(caseKey, "Primary case status", "closed", true);

        assertIsMainApplicantFlow(getCaseData(caseKey));
    }

    @Test
    void resume_incorrectOtp_doesNotResume() throws Exception {
        final String previousCaseKey = invalidSsnFlow();
        randomizeAnonymousIdAndCookie();

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
        assertSubmitAnchorData(anchorDataResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto requestOtpResponse = requestOtp(interactionId);
        assertRequestOtp(requestOtpResponse);

        final InteractionResponseDto verifyInvalidOtpResponse = verifyWithInvalidCode(interactionId);
        String actualCaseKey = getCaseId(UUID.fromString(interactionId));
        assertThat(actualCaseKey).isEqualTo(caseKey);
        assertInvalidOtpCode(verifyInvalidOtpResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto verifyOtpResponse = verifyOtp(interactionId);
        actualCaseKey = getCaseId(UUID.fromString(interactionId));
        assertThat(actualCaseKey).isEqualTo(previousCaseKey);
        assertVerifyOtp(verifyOtpResponse, getCaseData(previousCaseKey).getMainApplicant(), SSN_STEP);

        final InteractionResponseDto submitSsnResponse = submitSsn(interactionId);
        assertSubmitSsn(submitSsnResponse, getCaseData(previousCaseKey).getMainApplicant());
    }

    @Test
    void resume_noAddress() throws Exception {
        final String previousCaseKey = invalidAddressFlow();
        randomizeAnonymousIdAndCookie();

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
        assertSubmitAnchorData(anchorDataResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto requestOtpResponse = requestOtp(interactionId);
        assertRequestOtp(requestOtpResponse);

        final InteractionResponseDto verifyOtpResponse = verifyOtp(interactionId);
        final String actualCaseKey = getCaseId(UUID.fromString(interactionId));
        assertThat(actualCaseKey).isEqualTo(previousCaseKey);
        assertVerifyOtp(verifyOtpResponse, getCaseData(previousCaseKey).getMainApplicant(), ADDRESS_STEP);

        assertIdtAddressValidationTaskIsOpen(previousCaseKey);
        final InteractionResponseDto submitAddressResponse = submitAddress(interactionId);
        assertSubmitAddress(submitAddressResponse, getCaseData(previousCaseKey).getMainApplicant());
        assertIdtAddressValidationTaskCompleted(previousCaseKey);

        final InteractionResponseDto submitAccountType = submitIndividualAccountType(interactionId);
        assertSubmitIndividualAccountType(submitAccountType, getCaseData(previousCaseKey));

        final InteractionResponseDto submitCitizenshipTypeResponse = submitCitizenshipType(interactionId,
            CitizenshipType.US_CITIZEN);
        assertSubmitResidentCitizenshipType(submitCitizenshipTypeResponse,
            getCaseData(previousCaseKey).getMainApplicant());

        final InteractionResponseDto submitSsnResponse = submitSsn(interactionId);
        assertSubmitSsn(submitSsnResponse, getCaseData(previousCaseKey).getMainApplicant());

        assertCaseDataIdentityVerificationInitiationFilled(getCaseData(previousCaseKey).getMainApplicant());
        assertCaseDataIdentityVerificationResultFilled(getCaseData(previousCaseKey).getMainApplicant(), APPROVED);
    }

    @Test
    void resume_noSsn() throws Exception {
        final String previousCaseKey = invalidSsnFlow();
        randomizeAnonymousIdAndCookie();

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
        assertSubmitAnchorData(anchorDataResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto requestOtpResponse = requestOtp(interactionId);
        assertRequestOtp(requestOtpResponse);

        final InteractionResponseDto verifyOtpResponse = verifyOtp(interactionId);
        final String actualCaseKey = getCaseId(UUID.fromString(interactionId));
        assertThat(actualCaseKey).isEqualTo(previousCaseKey);
        assertVerifyOtp(verifyOtpResponse, getCaseData(previousCaseKey).getMainApplicant(), SSN_STEP);

        final InteractionResponseDto submitSsnResponse = submitSsn(interactionId);
        assertSubmitSsn(submitSsnResponse, getCaseData(previousCaseKey).getMainApplicant());
    }

    @Test
    void resume_doesNotResumeArchivedCase() throws Exception {
        fullFlow_deniedResponseOnIDV_goesToDeclinedAsLastStep();
        randomizeAnonymousIdAndCookie();
        happyFlow();
    }

    @Test
    void resume_afterOnboardingIsSuccessfulDoesNotResume() throws Exception {
        happyFlow();
        randomizeAnonymousIdAndCookie();

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
        assertSubmitAnchorData(anchorDataResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto requestOtpResponse = requestOtp(interactionId);
        assertRequestOtp(requestOtpResponse);

        final InteractionResponseDto verifyOtpResponse = verifyOtp(interactionId);
        final String actualCaseKey = getCaseId(UUID.fromString(interactionId));
        assertThat(actualCaseKey).isEqualTo(caseKey);
        assertVerifyOtp(verifyOtpResponse, getCaseData(caseKey).getMainApplicant());
    }

    @Test
    void resume_doesNotResumeCoApplicantCaseWhenStartedNewFlow() throws Exception {
        String caseKey = happyFlow_jointAccount();

        Onboarding caseData = getCaseData(caseKey);
        assertIsCoApplicantFlow(caseData);

        final InteractionResponseDto fetchCoApplicantResponse = fetchCoApplicant(
            UUID.fromString(caseData.getCoApplicantId()));
        assertFetchCoApplicant_CoApplicant(fetchCoApplicantResponse);

        final String interactionId = fetchCoApplicantResponse.getInteractionId();
        final String actualCaseKey = getCaseId(UUID.fromString(interactionId));
        assertThat(actualCaseKey).isEqualTo(caseKey);

        assertIsCoApplicantFlow(caseData);
        final InteractionResponseDto agreementResponse = agreeToTerms(interactionId);
        assertAgreeWithTerms(agreementResponse, getCaseData(caseKey).getCoApplicant());

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto prefillAnchorDataResponse = prefillAnchorData(
            UUID.fromString(caseData.getCoApplicantId()), interactionId);
        assertPrefillAnchorData(prefillAnchorDataResponse);

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto anchorDataResponse = submitCoApplicantAnchorData(
            CO_APPLICANT_FIRST_NAME_MOCK_KYC_VALIDATION_OK,
            AGE_18_DOB, interactionId);
        assertSubmitAnchorData(anchorDataResponse, getCaseData(caseKey).getCoApplicant());

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto requestOtpResponse = requestOtpCoApplicant(interactionId);
        assertRequestOtp(requestOtpResponse);

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto verifyOtpResponse = verifyOtpCoApplicant(interactionId);
        assertVerifyOtp(verifyOtpResponse, getCaseData(caseKey).getCoApplicant());

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto identityInitiationResponse = identityInitiation(interactionId);
        assertIdentityInitiation(identityInitiationResponse, getCaseData(caseKey).getCoApplicant());

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto identityResultResponse = identityResult(interactionId, APPROVED);
        assertIdentityResult(identityResultResponse, getCaseData(caseKey).getCoApplicant(), APPROVED);

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitAddressResponse = submitAddress(interactionId);
        assertSubmitAddress_CoApplicant(submitAddressResponse, getCaseData(caseKey).getCoApplicant());

        randomizeAnonymousIdAndCookie();
        happyFlow();
    }

    private String happyFlow_jointAccount() throws Exception {
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
        assertRequestOtp(requestOtpResponse);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto verifyOtpResponse = verifyOtp(interactionId);
        assertVerifyOtp(verifyOtpResponse, getCaseData(caseKey).getMainApplicant());

        identityVerificationDefaultStep(interactionId, caseKey, APPROVED);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        assertIdtAddressValidationTaskIsOpen(caseKey);
        final InteractionResponseDto submitAddressResponse = submitAddress(interactionId);
        assertSubmitAddress(submitAddressResponse, getCaseData(caseKey).getMainApplicant());
        assertIdtAddressValidationTaskCompleted(caseKey);

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
        assertIdtCitizenshipTaskIsOpen(caseKey);
        final InteractionResponseDto submitCitizenshipTypeResponse = submitCitizenshipType(interactionId,
            CitizenshipType.US_CITIZEN);
        assertSubmitResidentCitizenshipType(submitCitizenshipTypeResponse, getCaseData(caseKey).getMainApplicant());

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitSsnResponse = submitSsn(interactionId);
        assertSubmitSsn(submitSsnResponse, getCaseData(caseKey).getMainApplicant());
        assertIdtCitizenshipTaskCompleted(caseKey);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        assertIdtKycTaskIsOpen(caseKey);
        final InteractionResponseDto kycQuestionsResponse = requestKycQuestions(interactionId);
        assertRequestKycQuestions(kycQuestionsResponse);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitKycQuestionsResponse = submitKycQuestions(interactionId, true);
        assertSubmitKycQuestionsIdentityVerified(submitKycQuestionsResponse, getCaseData(caseKey).getMainApplicant(),
            APPROVED);
        assertIdtKycTaskCompleted(caseKey);

        assertIsMainApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto setupCredentialsResponse = setupCredentials(interactionId);
        assertSetupCredentialsJointAccountMainApplicant(setupCredentialsResponse,
            getCaseData(caseKey).getMainApplicant());

        assertCaseStatus(caseKey, "Primary case status", "open", true);
        assertIsCoApplicantFlow(getCaseData(caseKey));

        return caseKey;
    }

    private void happyFlow_coApplicant(String caseKey) throws Exception {
        Onboarding caseData = getCaseData(caseKey);
        assertIsCoApplicantFlow(caseData);

        final InteractionResponseDto fetchCoApplicantResponse = fetchCoApplicant(
            UUID.fromString(caseData.getCoApplicantId()));
        assertFetchCoApplicant_CoApplicant(fetchCoApplicantResponse);

        final String interactionId = fetchCoApplicantResponse.getInteractionId();
        final String actualCaseKey = getCaseId(UUID.fromString(interactionId));
        assertThat(actualCaseKey).isEqualTo(caseKey);

        assertIsCoApplicantFlow(caseData);
        final InteractionResponseDto agreementResponse = agreeToTerms(interactionId);
        assertAgreeWithTerms(agreementResponse, getCaseData(caseKey).getCoApplicant());

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto prefillAnchorDataResponse = prefillAnchorData(
            UUID.fromString(caseData.getCoApplicantId()), interactionId);
        assertPrefillAnchorData(prefillAnchorDataResponse);

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto anchorDataResponse = submitCoApplicantAnchorData(
            CO_APPLICANT_FIRST_NAME_MOCK_KYC_VALIDATION_OK,
            AGE_18_DOB, interactionId);
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
            CitizenshipType.US_CITIZEN);
        assertSubmitResidentCitizenshipType(submitCitizenshipTypeResponse, getCaseData(caseKey).getCoApplicant());

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitSsnResponse = submitSsn(interactionId);
        assertSubmitSsn(submitSsnResponse, getCaseData(caseKey).getCoApplicant());

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto kycQuestionsResponse = requestKycQuestions(interactionId);
        assertRequestKycQuestions(kycQuestionsResponse);

        assertIsCoApplicantFlow(getCaseData(caseKey));
        final InteractionResponseDto submitKycQuestionsResponse = submitKycQuestions(interactionId, true);
        assertSubmitKycQuestionsIdentityVerified(submitKycQuestionsResponse, getCaseData(caseKey).getMainApplicant(),
            APPROVED);

        assertIsCoApplicantFlow(getCaseData(caseKey));

        assertCaseStatus(caseKey, "Primary case status", "open", true);
    }

    String invalidAddressFlow() throws Exception {
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
        assertSubmitAnchorData(anchorDataResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto requestOtpResponse = requestOtp(interactionId);
        assertRequestOtp(requestOtpResponse);

        final InteractionResponseDto verifyOtpResponse = verifyOtp(interactionId);
        assertVerifyOtp(verifyOtpResponse, getCaseData(caseKey).getMainApplicant());

        identityVerificationDefaultStep(interactionId, caseKey, APPROVED);

        assertIdtAddressValidationTaskIsOpen(caseKey);
        final InteractionResponseDto submitInvalidAddressResponse = submitInvalidAddress(interactionId);
        assertThat(submitInvalidAddressResponse.getStep().getStatus()).isNull();
        assertThat(submitInvalidAddressResponse.getStep().getName()).isEqualTo(ADDRESS_STEP);
        assertThat(submitInvalidAddressResponse.getActionErrors().size()).isEqualTo(1);

        final Onboarding onboarding = getCaseData(caseKey);
        assertThat(onboarding.getMainApplicant().getAddress()).isNull();
        assertIdtAddressValidationTaskIsOpen(caseKey);

        return caseKey;
    }

    private String invalidSsnFlow() throws Exception {
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
        assertSubmitAnchorData(anchorDataResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto requestOtpResponse = requestOtp(interactionId);
        assertRequestOtp(requestOtpResponse);

        final InteractionResponseDto verifyOtpResponse = verifyOtp(interactionId);
        assertVerifyOtp(verifyOtpResponse, getCaseData(caseKey).getMainApplicant());

        identityVerificationDefaultStep(interactionId, caseKey, APPROVED);

        assertIdtAddressValidationTaskIsOpen(caseKey);
        final InteractionResponseDto submitAddressResponse = submitAddress(interactionId);
        assertSubmitAddress(submitAddressResponse, getCaseData(caseKey).getMainApplicant());
        assertIdtAddressValidationTaskCompleted(caseKey);

        final InteractionResponseDto submitAccountType = submitIndividualAccountType(interactionId);
        assertSubmitIndividualAccountType(submitAccountType, getCaseData(caseKey));

        assertIdtCitizenshipTaskIsOpen(caseKey);
        final InteractionResponseDto submitCitizenshipTypeResponse = submitCitizenshipType(interactionId,
            CitizenshipType.US_CITIZEN);
        assertSubmitResidentCitizenshipType(submitCitizenshipTypeResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto submitIncorrectSsnResponse = submitIncorrectSsn(interactionId);
        assertThat(submitIncorrectSsnResponse.getStep().getStatus()).isNull();
        assertThat(submitIncorrectSsnResponse.getStep().getName()).isEqualTo(SSN_STEP);
        assertThat(submitIncorrectSsnResponse.getActionErrors().size()).isEqualTo(1);
        assertIdtCitizenshipTaskCompleted(caseKey);

        final Onboarding onboarding = getCaseData(caseKey);
        assertThat(onboarding.getMainApplicant().getCitizenship().getSsn()).isNull();

        return caseKey;
    }

}
