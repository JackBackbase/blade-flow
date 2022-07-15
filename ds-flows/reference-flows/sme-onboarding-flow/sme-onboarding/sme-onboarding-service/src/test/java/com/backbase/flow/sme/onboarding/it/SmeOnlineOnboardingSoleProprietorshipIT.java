package com.backbase.flow.sme.onboarding.it;

import static com.backbase.flow.service.companylookup.infrastructure.BusinessDetailsHandler.BUSINESS_DETAILS;
import static com.backbase.flow.service.companylookup.infrastructure.BusinessStructureHandler.BUSINESS_STRUCTURE;
import static com.backbase.flow.sme.onboarding.TestConstants.APPLICANT_ONBOARDING_PROCESS_ID;
import static com.backbase.flow.sme.onboarding.TestConstants.CHECK_BUSINESS_RELATION_AND_DOCUMENT_REQUEST;
import static com.backbase.flow.sme.onboarding.TestConstants.DOCUMENT_REQUEST_JOURNEY;
import static com.backbase.flow.sme.onboarding.TestConstants.FIRST_NAME_POTENTIAL_MATCH;
import static com.backbase.flow.sme.onboarding.TestConstants.IDENTITY_VERIFICATION_IDT;
import static com.backbase.flow.sme.onboarding.TestConstants.IDENTITY_VERIFICATION_INITIATION;
import static com.backbase.flow.sme.onboarding.TestConstants.SELECT_PRODUCTS;
import static com.backbase.flow.sme.onboarding.TestConstants.SME_ONBOARDING_ANCHOR;
import static com.backbase.flow.sme.onboarding.TestConstants.SME_ONBOARDING_PERSONAL_ADDRESS;
import static com.backbase.flow.sme.onboarding.TestConstants.SME_ONBOARDING_SSN;

import com.backbase.flow.sme.onboarding.TestConstants;
import com.backbase.flow.sme.onboarding.interaction.model.AnchorRequestDto;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class SmeOnlineOnboardingSoleProprietorshipIT
    extends InteractionIT<SmeOnlineOnboardingSoleProprietorshipIT.SmeOnlineOnboardingSoleProprietorshipStep> {

    @Override
    List<StepAction<SmeOnlineOnboardingSoleProprietorshipStep>> happyPath() {
        return List.of(
            new StepAction<>(SmeOnlineOnboardingSoleProprietorshipStep.WALKTHROUGH, () -> {
                var onboardingInitResponse = performWalkthroughRequest(SME_ONBOARDING_ANCHOR);
                checkWalkthrough(onboardingInitResponse);
            }),
            new StepAction<>(SmeOnlineOnboardingSoleProprietorshipStep.ANCHOR_DATA, () -> {
                performAnchorDataRequest(FIRST_NAME_POTENTIAL_MATCH);
                checkAnchorData(FIRST_NAME_POTENTIAL_MATCH);
            }),
            new StepAction<>(SmeOnlineOnboardingSoleProprietorshipStep.OTP, () -> {
                performAvailableOtpChannelsRequest();
                performFetchOtpDataRequest();
                performRequestOtpRequest();
                performVerifyOtpRequest();
                checkOtp();
            }),
            new StepAction<>(SmeOnlineOnboardingSoleProprietorshipStep.CHECK_CASE, () -> {
                var checkCaseExistsResponse = performCheckCaseRequest();
                checkCheckCaseExists(checkCaseExistsResponse, SELECT_PRODUCTS, FIRST_NAME_POTENTIAL_MATCH);
            }),
            new StepAction<>(SmeOnlineOnboardingSoleProprietorshipStep.PRODUCT_SELECTION, () -> {
                performGetProductList();
                performProductSelectionRequest(BUSINESS_STRUCTURE);
                checkProductSelection();
            }),
            new StepAction<>(SmeOnlineOnboardingSoleProprietorshipStep.BUSINESS_STRUCTURE, () -> {
                performGetBusinessStructuresList();
                performSoleProprietorshipBusinessStructureRequest(BUSINESS_DETAILS);
                checkCompanyLookupWillBeNotExecuted();
            }),
            new StepAction<>(SmeOnlineOnboardingSoleProprietorshipStep.BUSINESS_DETAILS, () -> {
                performBusinessDetailsRequest();
                checkBusinessDetails();
            }),
            new StepAction<>(SmeOnlineOnboardingSoleProprietorshipStep.BUSINESS_ADDRESS, () -> {
                performBusinessAddressRequest();
                checkBusinessAddress();
            }),
            new StepAction<>(SmeOnlineOnboardingSoleProprietorshipStep.BUSINESS_IDENTITY, () -> {
                performBusinessIdentityRequest(BUSINESS_IDENTITY_WITHOUT_LICENSE,
                    CHECK_BUSINESS_RELATION_AND_DOCUMENT_REQUEST
                );
                checkBusinessIdentity(BUSINESS_IDENTITY_WITHOUT_LICENSE);
            }),
            new StepAction<>(SmeOnlineOnboardingSoleProprietorshipStep.CHECK_BRJ_AND_DR, () -> {
                performCheckBusinessRelationsAndDocumentRequest(IDENTITY_VERIFICATION_INITIATION);
                checkBusinessRelationsAndDocumentRequest(false, false);
            }),
            new StepAction(SmeOnlineOnboardingSoleProprietorshipStep.IDENTITY_VERIFICATION, () -> {
                performIdentityVerification(SME_ONBOARDING_PERSONAL_ADDRESS, interactionId);
                checkIdentityVerification();
                checkUserTaskIsCompleted(APPLICANT_ONBOARDING_PROCESS_ID, IDENTITY_VERIFICATION_IDT);
            }),
            new StepAction<>(SmeOnlineOnboardingSoleProprietorshipStep.PERSONAL_ADDRESS, () -> {
                performPersonalAddressRequest(SME_ONBOARDING_SSN, interactionId);
                checkPersonalAddress();
                checkTasksForPersonalAddressValidation();
            }),
            new StepAction<>(SmeOnlineOnboardingSoleProprietorshipStep.SSN, () -> {
                performSsnRequest();
                checkSsn();
            }),
            new StepAction<>(SmeOnlineOnboardingSoleProprietorshipStep.LANDING, () -> {
                performLandingRequest();
                checkLanding();
            }),
            new StepAction<>(SmeOnlineOnboardingSoleProprietorshipStep.COMPLETE_AML_KYC_TASK, () -> {
                performCompleteAmlTask();
                checkAmlUserData();
            }),
            new StepAction<>(SmeOnlineOnboardingSoleProprietorshipStep.APPROVE_RISK_ASSESSMENT_REVIEW, () -> {
                performCompleteRiskAssessmentTask();
                checkRiskAssessmentData(BUSINESS_INDUSTRIES_WITHOUT_LICENSE);
            })
        );
    }

    @Override
    String interactionName() {
        return TestConstants.SME_ONBOARDING;
    }

    @Test
    void smeOnlineOnboarding_soleProprietorship_fullHappyPath() {
        happyPathUntil(SmeOnlineOnboardingSoleProprietorshipStep.INTERACTION_END);
        checkProcessIsFinishedSuccessfully();
    }

    @Test
    void smeOnlineOnboarding_anchorData_noData() {
        var anchorDataRequest = AnchorRequestDto.builder().build();
        checkIncorrectAnchorData(anchorDataRequest);
    }

    @Test
    void smeOnlineOnboarding_anchorData_invalidData() {
        var anchorDataRequest = AnchorRequestDto.builder()
            .firstName(null)
            .lastName("this is a long enough name to trigger validation error")
            .dateOfBirth(LocalDate.now())
            .emailAddress("not@an@email")
            .build();
        checkIncorrectAnchorData(anchorDataRequest);
    }

    private void checkIncorrectAnchorData(AnchorRequestDto anchorDataRequest) {
        happyPathUntil(SmeOnlineOnboardingSoleProprietorshipStep.ANCHOR_DATA);
        checkIncorrectAnchorData(anchorDataRequest, SME_ONBOARDING_ANCHOR);
    }

    @Test
    void smeOnlineOnboarding_soleProprietorship_businessWithLicense() {
        happyPathUntil(SmeOnlineOnboardingSoleProprietorshipStep.BUSINESS_IDENTITY);
        performBusinessIdentityRequest(BUSINESS_IDENTITY_WITH_LICENSE, CHECK_BUSINESS_RELATION_AND_DOCUMENT_REQUEST);
        checkBusinessIdentity(BUSINESS_IDENTITY_WITH_LICENSE);
        performCheckBusinessRelationsAndDocumentRequest(DOCUMENT_REQUEST_JOURNEY);
        checkBusinessRelationsAndDocumentRequest(false, true);
        performDocumentUploadRequests();
        happyPathBetween(SmeOnlineOnboardingSoleProprietorshipStep.CHECK_BRJ_AND_DR,
            SmeOnlineOnboardingSoleProprietorshipStep.APPROVE_RISK_ASSESSMENT_REVIEW);
        performCompleteRiskAssessmentTask();
        checkRiskAssessmentData(BUSINESS_INDUSTRIES_WITH_LICENSE);
        checkProcessIsFinishedSuccessfully();
    }

    enum SmeOnlineOnboardingSoleProprietorshipStep {
        WALKTHROUGH,
        ANCHOR_DATA,
        OTP,
        CHECK_CASE,
        PRODUCT_SELECTION,
        BUSINESS_STRUCTURE,
        BUSINESS_DETAILS,
        BUSINESS_ADDRESS,
        BUSINESS_IDENTITY,
        CHECK_BRJ_AND_DR,
        IDENTITY_VERIFICATION,
        PERSONAL_ADDRESS,
        SSN,
        LANDING,
        COMPLETE_AML_KYC_TASK,
        APPROVE_RISK_ASSESSMENT_REVIEW,
        INTERACTION_END
    }
}
