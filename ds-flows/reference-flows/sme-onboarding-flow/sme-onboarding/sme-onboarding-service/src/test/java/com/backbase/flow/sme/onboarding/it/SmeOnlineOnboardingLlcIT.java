package com.backbase.flow.sme.onboarding.it;

import static com.backbase.flow.service.companylookup.infrastructure.BusinessStructureHandler.BUSINESS_STRUCTURE;
import static com.backbase.flow.sme.onboarding.TestConstants.APPLICANT_ONBOARDING_PROCESS_ID;
import static com.backbase.flow.sme.onboarding.TestConstants.BUSINESS_OWNERS;
import static com.backbase.flow.sme.onboarding.TestConstants.CHECK_BUSINESS_RELATION_AND_DOCUMENT_REQUEST;
import static com.backbase.flow.sme.onboarding.TestConstants.DOCUMENT_REQUEST_JOURNEY;
import static com.backbase.flow.sme.onboarding.TestConstants.IDENTITY_VERIFICATION_IDT;
import static com.backbase.flow.sme.onboarding.TestConstants.RELATION_TYPE;
import static com.backbase.flow.sme.onboarding.TestConstants.SELECT_PRODUCTS;
import static com.backbase.flow.sme.onboarding.TestConstants.SME_ONBOARDING_ANCHOR;
import static com.backbase.flow.sme.onboarding.TestConstants.SME_ONBOARDING_PERSONAL_ADDRESS;
import static com.backbase.flow.sme.onboarding.TestConstants.SME_ONBOARDING_SSN;
import static com.backbase.flow.sme.onboarding.TestConstants.UPDATE_CURRENT_USER_OWNER_STEP;

import com.backbase.flow.sme.onboarding.TestConstants;
import com.backbase.flow.sme.onboarding.interaction.utils.SmeUtils;
import java.util.List;
import org.junit.jupiter.api.Test;

class SmeOnlineOnboardingLlcIT extends InteractionIT<SmeOnlineOnboardingLlcIT.SmeOnlineOnboardingLlcStep> {

    @Override
    List<StepAction<SmeOnlineOnboardingLlcStep>> happyPath() {
        return List.of(
            new StepAction<>(SmeOnlineOnboardingLlcStep.WALKTHROUGH, () -> {
                var onboardingInitResponse = performWalkthroughRequest(SME_ONBOARDING_ANCHOR);
                checkWalkthrough(onboardingInitResponse);
            }),
            new StepAction<>(SmeOnlineOnboardingLlcStep.ANCHOR_DATA, () -> {
                performAnchorDataRequest(FIRST_NAME);
                checkAnchorData(FIRST_NAME);
            }),
            new StepAction<>(SmeOnlineOnboardingLlcStep.OTP, () -> {
                performAvailableOtpChannelsRequest();
                performFetchOtpDataRequest();
                performRequestOtpRequest();
                performVerifyOtpRequest();
                checkOtp();
            }),
            new StepAction<>(SmeOnlineOnboardingLlcStep.CHECK_CASE, () -> {
                var checkCaseExistsResponse = performCheckCaseRequest();
                checkCheckCaseExists(checkCaseExistsResponse, SELECT_PRODUCTS, FIRST_NAME);
            }),
            new StepAction<>(SmeOnlineOnboardingLlcStep.PRODUCT_SELECTION, () -> {
                performGetProductList();
                performProductSelectionRequest(BUSINESS_STRUCTURE);
                checkProductSelection();
            }),
            new StepAction<>(SmeOnlineOnboardingLlcStep.BUSINESS_STRUCTURE, () -> {
                performGetBusinessStructuresList();
                performLlcMultipleMemberBusinessStructureRequest();
                checkCompanyLookupWillBeExecuted();
            }),
            new StepAction<>(SmeOnlineOnboardingLlcStep.COMPANY_LOOKUP, () -> {
                var companyNumber = performCompanyLookupRequest();
                performCompanyDetailsRequest(companyNumber);
                checkCompanyDetails(companyNumber);
            }),
            new StepAction<>(SmeOnlineOnboardingLlcStep.BUSINESS_DETAILS, () -> {
                performBusinessDetailsRequest();
                checkBusinessDetails();
            }),
            new StepAction<>(SmeOnlineOnboardingLlcStep.BUSINESS_ADDRESS, () -> {
                performBusinessAddressRequest();
                checkBusinessAddress();
            }),
            new StepAction<>(SmeOnlineOnboardingLlcStep.BUSINESS_IDENTITY, () -> {
                performBusinessIdentityRequest(BUSINESS_IDENTITY_WITHOUT_LICENSE,
                    CHECK_BUSINESS_RELATION_AND_DOCUMENT_REQUEST
                );
                checkBusinessIdentity(BUSINESS_IDENTITY_WITHOUT_LICENSE);
            }),
            new StepAction<>(SmeOnlineOnboardingLlcStep.CHECK_BRJ_AND_DR, () -> {
                performCheckBusinessRelationsAndDocumentRequest(RELATION_TYPE);
                checkBusinessRelationsAndDocumentRequest(true, true);
            }),
            new StepAction<>(SmeOnlineOnboardingLlcStep.RELATION_TYPE, () -> {
                var relationTypeResponse = performRelationType();
                checkRelationType(relationTypeResponse);
            }),
            new StepAction(SmeOnlineOnboardingLlcStep.UPDATE_CURRENT_USER_OWNER, () -> {
                var businessRoles = performGetBusinessRoles(UPDATE_CURRENT_USER_OWNER_STEP);
                checkBusinessRoles(businessRoles);
                var businessPersons = performGetBusinessPersons();
                var currentUser = checkBusinessPersons(businessPersons);
                var modifiedCurrentUserData = new CurrentUserData(
                    currentUser, getRandomRole(businessRoles), getRandomOwnershipPercentage());
                performUpdateCurrentUserOwner(currentUser, modifiedCurrentUserData);
                checkCurrentUserOwner(currentUser, modifiedCurrentUserData);
            }),
            new StepAction<>(SmeOnlineOnboardingLlcStep.BUSINESS_OWNERS, () -> {
                var businessRoles = performGetBusinessRoles(BUSINESS_OWNERS);
                var additionalOwner = performAddAdditionalBusinessOwner(businessRoles);
                checkAddAdditionalBusinessOwner(additionalOwner);
                var relationType = performCompleteBusinessOwner();
                checkCompleteBusinessOwner(relationType);
            }),
            new StepAction<>(SmeOnlineOnboardingLlcStep.CONTROL_PERSONS, () -> {
                var registrarId = SmeUtils.getRegistrar(getCaseData(caseKey)).getId();
                performSelectControlPerson(registrarId);
                checkSelectControlPerson();
            }),
            new StepAction<>(SmeOnlineOnboardingLlcStep.BUSINESS_SUMMARY, () -> {
                performCompleteSummary(false, DOCUMENT_REQUEST_JOURNEY);
                checkCompleteSummary();
            }),
            new StepAction<>(SmeOnlineOnboardingLlcStep.COMPLETE_AML_KYB_TASK, () -> {
                performCompleteAmlTask();
                checkAmlBusinessData();
            }),
            new StepAction<>(SmeOnlineOnboardingLlcStep.COMPLETE_BUSINESS_RELATION_REVIEW_TASK, () -> {
                performCompleteBusinessRelationReviewTask();
            }),
            //TODO extend with DocumentRequest during https://backbase.atlassian.net/browse/XEN-1365 implementation
            new StepAction(SmeOnlineOnboardingLlcStep.IDENTITY_VERIFICATION, () -> {
                performIdentityVerification(SME_ONBOARDING_PERSONAL_ADDRESS, interactionId);
                checkIdentityVerification();
                checkUserTaskIsCompleted(APPLICANT_ONBOARDING_PROCESS_ID, IDENTITY_VERIFICATION_IDT);
            }),
            new StepAction<>(SmeOnlineOnboardingLlcStep.PERSONAL_ADDRESS, () -> {
                performPersonalAddressRequest(SME_ONBOARDING_SSN, interactionId);
                checkPersonalAddress();
                checkTasksForPersonalAddressValidation();
            }),
            new StepAction<>(SmeOnlineOnboardingLlcStep.SSN, () -> {
                performSsnRequest();
                checkSsn();
            }),
            new StepAction<>(SmeOnlineOnboardingLlcStep.LANDING, () -> {
                performLandingRequest();
                checkLanding();
            })
        );
    }

    @Override
    String interactionName() {
        return TestConstants.SME_ONBOARDING;
    }

    @Test
    void smeOnlineOnboarding_llc_fullHappyPath() {
        //TODO change during https://backbase.atlassian.net/browse/XEN-1365 implementation
        happyPathUntil(SmeOnlineOnboardingLlcStep.IDENTITY_VERIFICATION);
        //checkProcessIsFinished();
    }

    @Test
    void smeOnlineOnboarding_llc_amlFailedFinishesProcessWithNoTasksLeft() {
        happyPathUntil(SmeOnlineOnboardingLlcStep.COMPLETE_AML_KYB_TASK);
        checkUserTaskIsActive(TestConstants.AML_PROCESS_ID, TestConstants.AML_REVIEW_ACTIVITY);
        checkUserTaskIsActive(TestConstants.MANUAL_REVIEW, TestConstants.UPLOAD_DOCUMENTS_TASK);
        performFailedAmlTask();
        checkProcessIsFinished();
    }

    enum SmeOnlineOnboardingLlcStep {
        WALKTHROUGH,
        ANCHOR_DATA,
        OTP,
        CHECK_CASE,
        PRODUCT_SELECTION,
        BUSINESS_STRUCTURE,
        COMPANY_LOOKUP,
        BUSINESS_DETAILS,
        BUSINESS_ADDRESS,
        BUSINESS_IDENTITY,
        CHECK_BRJ_AND_DR,
        RELATION_TYPE,
        UPDATE_CURRENT_USER_OWNER,
        UPDATE_CURRENT_USER_CONTROL_PERSON,
        BUSINESS_OWNERS,
        CONTROL_PERSONS,
        BUSINESS_SUMMARY,
        COMPLETE_AML_KYB_TASK,
        COMPLETE_BUSINESS_RELATION_REVIEW_TASK,
        IDENTITY_VERIFICATION,
        PERSONAL_ADDRESS,
        SSN,
        LANDING,
        INTERACTION_END
    }
}
