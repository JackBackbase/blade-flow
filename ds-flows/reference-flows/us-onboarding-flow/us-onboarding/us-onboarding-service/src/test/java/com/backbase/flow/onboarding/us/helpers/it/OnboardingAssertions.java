package com.backbase.flow.onboarding.us.helpers.it;

import static org.assertj.core.api.Assertions.assertThat;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.interaction.api.model.InteractionResponseDto;

public class OnboardingAssertions extends BaseAssertions {

    public static void assertPrefillAnchorData(InteractionResponseDto response) {
        assertPrefillAnchorData(response, ANCHOR_DATA_STEP);
    }

    public static void assertSubmitAnchorData(InteractionResponseDto response, Applicant applicant) {
        assertSubmitAnchorData(response, applicant, OTP_VERIFICATION_STEP);
        assertBackStep(response, OTP_VERIFICATION_STEP, ANCHOR_DATA_STEP);
    }

    public static void assertSubmitCoApplicantAnchorData(InteractionResponseDto response, Onboarding onboarding) {
        assertSubmitAnchorData(response, onboarding.getCoApplicant(), CO_APPLICANT_ADDRESS_STEP);
    }

    public static void assertSubmitCoApplicantAddressData(InteractionResponseDto response, Onboarding onboarding) {
        assertSubmitAddress(response, onboarding.getCoApplicant(), CITIZENSHIP_STEP);
        assertBackStep(response, CITIZENSHIP_STEP, CO_APPLICANT_STEP);
    }

    public static void assertFetchOtpData(InteractionResponseDto response) {
        assertRequestOtp(response, OTP_VERIFICATION_STEP);
    }

    public static void assertRequestOtp(InteractionResponseDto response) {
        assertRequestOtp(response, OTP_VERIFICATION_STEP);
    }

    public static void assertVerifyOtp(InteractionResponseDto response, Applicant applicant) {
        assertVerifyOtp(response, applicant, IDENTITY_VERIFICATION_STEP);
    }

    public static void assertVerifyOtp(InteractionResponseDto response, Applicant applicant, String stepOverride) {
        BaseAssertions.assertVerifyOtp(response, applicant, stepOverride);
    }

    public static void assertInvalidOtpCode(InteractionResponseDto response, Applicant applicant) {
        assertInvalidOtpCode(response, applicant, OTP_VERIFICATION_STEP);
    }

    public static void assertVerificationType(InteractionResponseDto response) {
        assertVerificationType(response, IDENTITY_VERIFICATION_STEP);
    }

    public static void assertIdentityInitiation(InteractionResponseDto response, Applicant applicant) {
        assertIdentityInitiation(response, applicant, IDENTITY_VERIFICATION_STEP);
    }

    public static void assertIdentityResult(InteractionResponseDto response, Applicant applicant,
        String verificationId) {
        assertIdentityResult(response, applicant, verificationId, ADDRESS_STEP);
    }

    public static void assertSubmitAddress(InteractionResponseDto response, Applicant applicant) {
        assertSubmitAddress(response, applicant, CO_APPLICANT_STEP);
    }

    public static void assertIdtAddressValidationTaskIsOpen(String caseKey) throws Exception {
        assertUserTaskIsCreatedCorrectly(ADDRESS_VALIDATION_IDT_TASK_NAME, caseKey, true);
    }

    public static void assertIdtAddressValidationTaskCompleted(String caseKey) throws Exception {
        assertUserTaskIsCreatedCorrectly(ADDRESS_VALIDATION_IDT_TASK_NAME, caseKey, false);
    }

    public static void assertIdtIdvTaskIsOpen(String caseKey) throws Exception {
        assertUserTaskIsCreatedCorrectly(IDV_IDT_TASK_NAME, caseKey, true);
    }

    public static void assertIdtIdvTaskCompleted(String caseKey) throws Exception {
        assertUserTaskIsCreatedCorrectly(IDV_IDT_TASK_NAME, caseKey, false);
    }

    public static void assertIdtKycTaskIsOpen(String caseKey) throws Exception {
        assertUserTaskIsCreatedCorrectly(KYC_IDT_TASK_NAME, caseKey, true);
    }

    public static void assertIdtKycTaskCompleted(String caseKey) throws Exception {
        assertUserTaskIsCreatedCorrectly(KYC_IDT_TASK_NAME, caseKey, false);
    }

    public static void assertIdtCitizenshipTaskIsOpen(String caseKey) throws Exception {
        assertUserTaskIsCreatedCorrectly(CITIZENSHIP_IDT_TASK_NAME, caseKey, true);
    }

    public static void assertIdtCitizenshipTaskCompleted(String caseKey) throws Exception {
        assertUserTaskIsCreatedCorrectly(CITIZENSHIP_IDT_TASK_NAME, caseKey, false);
    }

    public static void assertSubmitAddress_CoApplicant(InteractionResponseDto response, Applicant applicant) {
        assertSubmitAddress(response, applicant, CITIZENSHIP_STEP);
        assertBackStep(response, CITIZENSHIP_STEP, ADDRESS_STEP);
    }

    public static void assertSubmitIndividualAccountType(InteractionResponseDto response, Onboarding caseData) {
        assertSubmitAccountType(response, caseData, CITIZENSHIP_STEP);
        assertBackStep(response, CITIZENSHIP_STEP, CO_APPLICANT_STEP);
    }

    public static void assertSubmitJointAccountType(InteractionResponseDto response, Onboarding caseData) {
        assertSubmitAccountType(response, caseData, CO_APPLICANT_DATA_STEP);
    }

    public static void assertFetchCitizenshipDataBeforeSubmit(InteractionResponseDto response, Applicant applicant) {
        assertFetchCitizenshipDataBeforeSubmit(response, applicant, CITIZENSHIP_STEP);
        assertBackStep(response, CITIZENSHIP_STEP, CO_APPLICANT_STEP);
    }

    public static void assertFetchCitizenshipDataAfterSubmit(InteractionResponseDto response, Applicant applicant) {
        assertFetchCitizenshipDataAfterSubmit(response, applicant, SSN_STEP);
        assertBackStep(response, SSN_STEP, CITIZENSHIP_STEP);
    }

    public static void assertFetchCitizenshipDataAfterSubmitNonResident(InteractionResponseDto response, Applicant applicant) {
        assertFetchCitizenshipDataAfterSubmit(response, applicant, NON_RESIDENT_DATA_STEP);
        assertBackStep(response, SSN_STEP, CITIZENSHIP_STEP);
    }

    public static void assertSubmitResidentCitizenshipType(InteractionResponseDto response, Applicant applicant) {
        assertSubmitCitizenshipType(response, applicant, SSN_STEP);
        assertBackStep(response, SSN_STEP, CITIZENSHIP_STEP);
    }

    public static void assertSubmitNonResidentCitizenshipType(InteractionResponseDto response, Applicant applicant) {
        assertSubmitCitizenshipType(response, applicant, NON_RESIDENT_DATA_STEP);
        assertBackStep(response, NON_RESIDENT_DATA_STEP, CITIZENSHIP_STEP);
    }

    public static void assertSubmitNonResidentData(InteractionResponseDto response, Applicant applicant) {
        assertSubmitNonResidentData(response, applicant, KYC_STEP);
        assertBackStep(response, KYC_STEP, NON_RESIDENT_DATA_STEP);
    }

    public static void assertSubmitSsn(InteractionResponseDto response, Applicant applicant) {
        assertSubmitSsn(response, applicant, KYC_STEP);
        assertBackStep(response, KYC_STEP, SSN_STEP);
    }

    public static void assertRequestKycQuestions(InteractionResponseDto response) {
        assertRequestKycQuestions(response, KYC_STEP);
    }

    public static void assertSubmitKycQuestionsIdentityVerified(InteractionResponseDto response, Applicant applicant,
        String mockedVerificationId) {
        assertSubmitKycQuestionsWithIdv(response, applicant, CREDENTIALS_STEP, mockedVerificationId);
    }

    public static void assertSubmitKycQuestionsInReviewDone(InteractionResponseDto response) {
        assertSubmitKycQuestions(response, IN_REVIEW_DONE_STEP);
    }

    public static void assertSubmitKycQuestionsDeclined(InteractionResponseDto response) {
        assertSubmitKycQuestions(response, DECLINED_STEP);
    }

    public static void assertSetupCredentials(InteractionResponseDto response, Applicant applicant) {
        assertSetupCredentials(response, applicant, SUCCESSFULLY_DONE_STEP);
    }

    public static void assertSetupCredentialsJointAccountMainApplicant(InteractionResponseDto response,
        Applicant applicant) {
        assertSetupCredentials(response, applicant, SUCCESSFULLY_DONE_CO_APPLICANT_STEP);
    }

    public static void assertCaseDataAnchorDataFilled(Applicant applicant) {
        assertApplicantAnchorDataFilled(applicant);
    }

    public static void assertCaseIsArchived(String caseKey) throws Exception {
        BaseAssertions.assertCaseIsArchived(caseKey);
    }

    public static void assertCaseDataIdentityVerificationInitiationFilled(Applicant applicant) {
        assertApplicantIdentityVerificationInitiationFilled(applicant);
    }

    public static void assertCaseDataIdentityVerificationResultFilled(Applicant applicant,
        String mockedVerificationId) {
        assertApplicantIdentityVerificationResultFilled(applicant, mockedVerificationId);
    }

    public static void assertAgreeWithTerms(InteractionResponseDto response, Applicant applicant) {
        assertAgreeWithTerms(response, applicant, ANCHOR_DATA_STEP);
    }

    public static void assertProductSelectionList(InteractionResponseDto response) {
        assertProductSelectionList(response, SELECT_PRODUCTS_STEP);
    }

    public static void assertSelectProducts(InteractionResponseDto response, Onboarding onboarding) {
        assertSelectProducts(response, onboarding, TERMS_AND_CONDITIONS_STEP);
    }

    public static void assertFetchCoApplicant(InteractionResponseDto response) {
        assertNextStep(response, SELECT_PRODUCTS_STEP);
        assertThat(response.getBody()).isNull();
    }

    public static void assertFetchCoApplicant_CoApplicant(InteractionResponseDto response) {
        assertNextStep(response, TERMS_AND_CONDITIONS_STEP);
        assertThat(response.getBody()).isNotNull();
    }

}
