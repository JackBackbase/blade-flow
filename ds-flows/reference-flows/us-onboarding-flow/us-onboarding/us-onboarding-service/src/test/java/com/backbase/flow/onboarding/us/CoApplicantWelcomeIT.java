package com.backbase.flow.onboarding.us;

import static com.backbase.flow.onboarding.us.helpers.AccountTypeStepCalls.submitJointAccountType;
import static com.backbase.flow.onboarding.us.helpers.AddressStepCalls.submitAddress;
import static com.backbase.flow.onboarding.us.helpers.AnchorStepCalls.submitAnchorData;
import static com.backbase.flow.onboarding.us.helpers.AnchorStepCalls.submitCoApplicantAnchorData;
import static com.backbase.flow.onboarding.us.helpers.CitizenshipStepCalls.submitCitizenshipType;
import static com.backbase.flow.onboarding.us.helpers.IdentityVerificationStepCalls.identityInitiation;
import static com.backbase.flow.onboarding.us.helpers.IdentityVerificationStepCalls.identityResult;
import static com.backbase.flow.onboarding.us.helpers.IdentityVerificationStepCalls.verificationType;
import static com.backbase.flow.onboarding.us.helpers.IdentityVerifiedStepCalls.moveViaEmptyHandler;
import static com.backbase.flow.onboarding.us.helpers.InitStepCalls.fetchCoApplicant;
import static com.backbase.flow.onboarding.us.helpers.InitStepCalls.fetchCoApplicantData;
import static com.backbase.flow.onboarding.us.helpers.KycStepCalls.submitKycQuestions;
import static com.backbase.flow.onboarding.us.helpers.OtpVerificationStepCalls.requestOtp;
import static com.backbase.flow.onboarding.us.helpers.OtpVerificationStepCalls.verifyOtp;
import static com.backbase.flow.onboarding.us.helpers.ProductSelectionListStepCalls.getProductList;
import static com.backbase.flow.onboarding.us.helpers.SelectProductsStepCalls.selectProducts;
import static com.backbase.flow.onboarding.us.helpers.SetupCredentialsStepCalls.setupCredentials;
import static com.backbase.flow.onboarding.us.helpers.SsnStepCalls.submitSsn;
import static com.backbase.flow.onboarding.us.helpers.TermsAndConditionsStepCalls.agreeToTerms;
import static com.backbase.flow.onboarding.us.helpers.it.BaseAssertions.assertCaseStatus;
import static com.backbase.flow.onboarding.us.helpers.it.BaseAssertions.getCaseData;
import static com.backbase.flow.onboarding.us.helpers.it.CoApplicantWelcomeAssertions.assertFetchCoApplicantData;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertAgreeWithTerms;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertFetchCoApplicant;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertIdentityInitiation;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertIdentityResult;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertProductSelectionList;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertRequestOtp;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSelectProducts;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSetupCredentialsJointAccountMainApplicant;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitAddress;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitAnchorData;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitCoApplicantAddressData;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitCoApplicantAnchorData;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitJointAccountType;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitKycQuestionsIdentityVerified;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitResidentCitizenshipType;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertSubmitSsn;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertVerificationType;
import static com.backbase.flow.onboarding.us.helpers.it.OnboardingAssertions.assertVerifyOtp;

import com.backbase.flow.application.uso.casedata.CitizenshipInfo.CitizenshipType;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.interaction.api.model.InteractionResponseDto;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoApplicantWelcomeIT extends BaseIT {

    private static final String FIRST_NAME_MOCK_KYC_VALIDATION_OK = "John";
    private static final String CO_APPLICANT_FIRST_NAME_MOCK_KYC_VALIDATION_OK = "Jane";
    private static final String AGE_18_DOB = LocalDate.now().minusYears(18).toString();
    private static final String APPROVED = "approved";


    @BeforeEach
    public void beforeEach() {
        setInteraction("onboarding");
    }

    @Test
    void coApplicantExists_happyFlow() throws Exception {
        final String interactionId = fullFlow_with_coApplicant();
        setInteraction("co-applicant-welcome");

        final String caseKey = getCaseId(UUID.fromString(interactionId));
        Onboarding onboarding = getCaseData(caseKey);

        final InteractionResponseDto fetchCoApplicantData = fetchCoApplicantData(onboarding.getCoApplicantId());

        assertFetchCoApplicantData(fetchCoApplicantData, caseKey);
    }

    private String fullFlow_with_coApplicant() throws Exception {
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
            CitizenshipType.US_CITIZEN);
        assertSubmitResidentCitizenshipType(submitCitizenshipTypeResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto submitSsnResponse = submitSsn(interactionId);
        assertSubmitSsn(submitSsnResponse, getCaseData(caseKey).getMainApplicant());

        final InteractionResponseDto submitKycQuestionsResponse = submitKycQuestions(interactionId, true);
        assertSubmitKycQuestionsIdentityVerified(submitKycQuestionsResponse, getCaseData(caseKey).getMainApplicant(),
            APPROVED);

        final InteractionResponseDto setupCredentialsResponse = setupCredentials(interactionId);
        assertSetupCredentialsJointAccountMainApplicant(setupCredentialsResponse, getCaseData(caseKey).getMainApplicant());

        assertCaseStatus(caseKey, "Primary case status", "open", true);

        return interactionId;
    }

}
