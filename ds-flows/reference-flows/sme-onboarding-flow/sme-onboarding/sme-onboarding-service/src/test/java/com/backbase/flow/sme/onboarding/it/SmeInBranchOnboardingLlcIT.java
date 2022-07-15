package com.backbase.flow.sme.onboarding.it;

import static com.backbase.flow.service.companylookup.domain.CompanyLookupConstants.COMPANY_LOOKUP_USER_TASK_ID;
import static com.backbase.flow.service.companylookup.infrastructure.CompanyLookupHandler.COMPANY_LOOKUP;
import static com.backbase.flow.sme.onboarding.TestConstants.ADDRESS_VALIDATION_IDT;
import static com.backbase.flow.sme.onboarding.TestConstants.APPLICANT_ONBOARDING_PENDING_EVENT;
import static com.backbase.flow.sme.onboarding.TestConstants.APPLICANT_ONBOARDING_PROCESS_ID;
import static com.backbase.flow.sme.onboarding.TestConstants.BUSINESS_OWNERS;
import static com.backbase.flow.sme.onboarding.TestConstants.BUSINESS_RELATION_COMPLETE_EVENT;
import static com.backbase.flow.sme.onboarding.TestConstants.BUSINESS_RELATION_INPUT_OWNER_INFORMATION_TASK_ID;
import static com.backbase.flow.sme.onboarding.TestConstants.BUSINESS_RELATION_IN_REVIEW_EVENT;
import static com.backbase.flow.sme.onboarding.TestConstants.BUSINESS_RELATION_PENDING_EVENT;
import static com.backbase.flow.sme.onboarding.TestConstants.BUSINESS_RELATION_PROCESS_ID;
import static com.backbase.flow.sme.onboarding.TestConstants.BUSINESS_RELATION_REVIEW_INFORMATION_TASK_ID;
import static com.backbase.flow.sme.onboarding.TestConstants.COMPANY_LOOKUP_COMPLETE_EVENT;
import static com.backbase.flow.sme.onboarding.TestConstants.COMPANY_LOOKUP_PENDING_EVENT;
import static com.backbase.flow.sme.onboarding.TestConstants.DONE;
import static com.backbase.flow.sme.onboarding.TestConstants.IDENTITY_VERIFICATION_IDT;
import static com.backbase.flow.sme.onboarding.TestConstants.SELECT_PRODUCTS;
import static com.backbase.flow.sme.onboarding.TestConstants.UPDATE_CURRENT_USER_OWNER_STEP;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.backbase.flow.service.companylookup.domain.CompanyLookupConstants;
import com.backbase.flow.sme.onboarding.TestConstants;
import com.backbase.flow.sme.onboarding.interaction.utils.SmeUtils;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class SmeInBranchOnboardingLlcIT
    extends SmeInBranchOnboardingIT<SmeInBranchOnboardingLlcIT.SmeInBranchOnboardingLlcStep> {

    @Override
    List<StepAction<SmeInBranchOnboardingLlcStep>> happyPath() {

        final var interactions = new ArrayList<String>();
        return List.of(
            new StepAction<>(SmeInBranchOnboardingLlcStep.START, () -> {
                performAnchorDataRequest(APPLICANT);
                checkAnchorData(FIRST_NAME);
            }),
            new StepAction<>(SmeInBranchOnboardingLlcStep.CHECK_CASE, () -> {
                var checkCaseExistsResponse = performCheckCaseRequest();
                checkCheckCaseExists(checkCaseExistsResponse, SELECT_PRODUCTS, FIRST_NAME);
            }),
            new StepAction<>(SmeInBranchOnboardingLlcStep.SELECT_PRODUCTS, () -> {
                performGetProductList();
                performProductSelectionRequest(DONE);
                checkProductSelection();
                checkCasePromoted();
            }),
            new StepAction<>(SmeInBranchOnboardingLlcStep.COMPANY_LOOKUP_IDT, () -> {
                setCurrentInteractionName(TestConstants.COMPANY_LOOKUP_IDT);
                var companyLookupInteractionId = startIdt(caseKey, COMPANY_LOOKUP, COMPANY_LOOKUP_USER_TASK_ID);
                setInteractionId(companyLookupInteractionId);
                checkEventWasRaised(COMPANY_LOOKUP_PENDING_EVENT);
                checkUserTaskIsActive(
                    CompanyLookupConstants.PROCESS_DEFINITION_KEY,
                    CompanyLookupConstants.COMPANY_LOOKUP_USER_TASK_ID);
            }),
            new StepAction<>(SmeInBranchOnboardingLlcStep.BUSINESS_STRUCTURE, () -> {
                performGetBusinessStructuresList();
                performLlcMultipleMemberBusinessStructureRequest();
                checkCompanyLookupWillBeExecuted();
            }),
            new StepAction<>(SmeInBranchOnboardingLlcStep.COMPANY_LOOKUP, () -> {
                var companyNumber = performCompanyLookupRequest();
                performCompanyDetailsRequest(companyNumber);
                checkCompanyDetails(companyNumber);
            }),
            new StepAction<>(SmeInBranchOnboardingLlcStep.BUSINESS_DETAILS, () -> {
                performBusinessDetailsRequest();
                checkBusinessDetails();
            }),
            new StepAction<>(SmeInBranchOnboardingLlcStep.BUSINESS_ADDRESS, () -> {
                performBusinessAddressRequest();
                checkBusinessAddress();
            }),
            new StepAction<>(SmeInBranchOnboardingLlcStep.BUSINESS_IDENTITY, () -> {
                performBusinessIdentityRequest(BUSINESS_IDENTITY_WITHOUT_LICENSE, DONE);
                checkBusinessIdentity(BUSINESS_IDENTITY_WITHOUT_LICENSE);
                checkEventWasRaised(COMPANY_LOOKUP_COMPLETE_EVENT);
                checkUserTaskIsCompleted(
                    CompanyLookupConstants.PROCESS_DEFINITION_KEY,
                    CompanyLookupConstants.COMPANY_LOOKUP_USER_TASK_ID);
            }),
            new StepAction<>(SmeInBranchOnboardingLlcStep.BUSINESS_RELATIONS_IDT, () -> {
                setCurrentInteractionName(TestConstants.BUSINESS_RELATIONS_IDT);
                var businessRelationsInteractionId =
                    startIdt(caseKey, BUSINESS_RELATION_PROCESS_ID, BUSINESS_RELATION_INPUT_OWNER_INFORMATION_TASK_ID);
                setInteractionId(businessRelationsInteractionId);
                checkEventWasRaised(BUSINESS_RELATION_PENDING_EVENT);
                checkUserTaskIsActive(BUSINESS_RELATION_PROCESS_ID, BUSINESS_RELATION_INPUT_OWNER_INFORMATION_TASK_ID);
            }),
            new StepAction<>(SmeInBranchOnboardingLlcStep.RELATION_TYPE, () -> {
                var relationTypeResponse = performRelationType();
                checkRelationType(relationTypeResponse);
            }),
            new StepAction(SmeInBranchOnboardingLlcStep.UPDATE_CURRENT_USER_OWNER, () -> {
                var businessRoles = performGetBusinessRoles(UPDATE_CURRENT_USER_OWNER_STEP);
                checkBusinessRoles(businessRoles);
                var businessPersons = performGetBusinessPersons();
                var currentUser = checkBusinessPersons(businessPersons);
                var amendedCurrentUserData = new CurrentUserData(
                    currentUser, getRandomRole(businessRoles), getRandomOwnershipPercentage());
                performUpdateCurrentUserOwner(currentUser, amendedCurrentUserData);
                checkCurrentUserOwner(currentUser, amendedCurrentUserData);
            }),
            new StepAction<>(SmeInBranchOnboardingLlcStep.BUSINESS_OWNERS, () -> {
                var businessRoles = performGetBusinessRoles(BUSINESS_OWNERS);
                var additionalOwner = performAddAdditionalBusinessOwner(businessRoles);
                checkAddAdditionalBusinessOwner(additionalOwner);
                var relationType = performCompleteBusinessOwner();
                checkCompleteBusinessOwner(relationType);
            }),
            new StepAction<>(SmeInBranchOnboardingLlcStep.CONTROL_PERSONS, () -> {
                var registrarId = SmeUtils.getRegistrar(getCaseData(caseKey)).getId();
                performSelectControlPerson(registrarId);
                checkSelectControlPerson();
            }),
            new StepAction<>(SmeInBranchOnboardingLlcStep.BUSINESS_SUMMARY, () -> {
                performCompleteSummary(true, DONE);
                checkCompleteSummary();
                checkEventWasRaised(BUSINESS_RELATION_IN_REVIEW_EVENT);
                checkUserTaskIsCompleted(
                    BUSINESS_RELATION_PROCESS_ID,
                    BUSINESS_RELATION_INPUT_OWNER_INFORMATION_TASK_ID);
                checkUserTaskIsActive(BUSINESS_RELATION_PROCESS_ID, BUSINESS_RELATION_REVIEW_INFORMATION_TASK_ID);
            }),
            new StepAction<>(SmeInBranchOnboardingLlcStep.COMPLETE_BUSINESS_RELATION_REVIEW_TASK, () -> {
                performCompleteBusinessRelationReviewTask();
                checkEventWasRaised(BUSINESS_RELATION_COMPLETE_EVENT);
                checkUserTaskIsCompleted(
                    BUSINESS_RELATION_PROCESS_ID,
                    BUSINESS_RELATION_REVIEW_INFORMATION_TASK_ID);
            }),
            new StepAction<>(SmeInBranchOnboardingLlcStep.IDENTITY_VERIFICATION_IDT, () -> {
                setCurrentInteractionName(TestConstants.IDENTITY_VERIFICATION_IDT);
                interactions.addAll(startIdts(caseKey, APPLICANT_ONBOARDING_PROCESS_ID, IDENTITY_VERIFICATION_IDT));
                checkEventWasRaised(APPLICANT_ONBOARDING_PENDING_EVENT, interactions.size());
                checkUserTasksAreActive(
                    APPLICANT_ONBOARDING_PROCESS_ID, IDENTITY_VERIFICATION_IDT, interactions.size());
            }),
            new StepAction<>(SmeInBranchOnboardingLlcStep.IDENTITY_VERIFICATION, () -> {
                interactions.forEach(interaction -> performIdentityVerification(DONE, interaction));
                checkIdentityVerification();
                checkUserTasksAreCompleted(
                    APPLICANT_ONBOARDING_PROCESS_ID, IDENTITY_VERIFICATION_IDT, interactions.size());
                interactions.clear();
            }),
            new StepAction<>(SmeInBranchOnboardingLlcStep.ADDRESS_IDT, () -> {
                setCurrentInteractionName(ADDRESS_VALIDATION_IDT);
                interactions.addAll(startIdts(caseKey, APPLICANT_ONBOARDING_PROCESS_ID, ADDRESS_VALIDATION_IDT));
                checkUserTasksAreActive(APPLICANT_ONBOARDING_PROCESS_ID, ADDRESS_VALIDATION_IDT, interactions.size());
            }),
            new StepAction<>(SmeInBranchOnboardingLlcStep.ADDRESS, () -> {
                interactions.forEach(interaction -> performPersonalAddressRequest(DONE, interaction));
                checkPersonalAddress();
                checkTasksForPersonalAddressValidation();
                interactions.clear();
            })
        );
    }

    @Test
    void smeInBranchOnboardingLlc_done_fullHappyPath() {
        assertDoesNotThrow(() -> happyPathUntil(SmeInBranchOnboardingLlcStep.DONE));
        //TODO extend with DocumentRequest during https://backbase.atlassian.net/browse/XEN-1365 implementation
        //checkProcessIsFinished()
    }

    enum SmeInBranchOnboardingLlcStep {
        START,
        CHECK_CASE,
        SELECT_PRODUCTS,
        COMPANY_LOOKUP_IDT,
        BUSINESS_STRUCTURE,
        COMPANY_LOOKUP,
        BUSINESS_DETAILS,
        BUSINESS_ADDRESS,
        BUSINESS_IDENTITY,
        BUSINESS_RELATIONS_IDT,
        RELATION_TYPE,
        UPDATE_CURRENT_USER_OWNER,
        UPDATE_CURRENT_USER_CONTROL_PERSON,
        BUSINESS_OWNERS,
        CONTROL_PERSONS,
        BUSINESS_SUMMARY,
        COMPLETE_BUSINESS_RELATION_REVIEW_TASK,
        IDENTITY_VERIFICATION_IDT,
        IDENTITY_VERIFICATION,
        ADDRESS_IDT,
        ADDRESS,
        DONE
    }
}
