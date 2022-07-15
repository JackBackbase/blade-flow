package com.backbase.flow.sme.onboarding.it;

import static com.backbase.flow.service.companylookup.domain.CompanyLookupConstants.COMPANY_LOOKUP_USER_TASK_ID;
import static com.backbase.flow.service.companylookup.infrastructure.BusinessDetailsHandler.BUSINESS_DETAILS;
import static com.backbase.flow.service.companylookup.infrastructure.CompanyLookupHandler.COMPANY_LOOKUP;
import static com.backbase.flow.sme.onboarding.TestConstants.ADDRESS_VALIDATION_IDT;
import static com.backbase.flow.sme.onboarding.TestConstants.APPLICANT_ONBOARDING_PENDING_EVENT;
import static com.backbase.flow.sme.onboarding.TestConstants.APPLICANT_ONBOARDING_PROCESS_ID;
import static com.backbase.flow.sme.onboarding.TestConstants.COMPANY_LOOKUP_COMPLETE_EVENT;
import static com.backbase.flow.sme.onboarding.TestConstants.COMPANY_LOOKUP_PENDING_EVENT;
import static com.backbase.flow.sme.onboarding.TestConstants.DONE;
import static com.backbase.flow.sme.onboarding.TestConstants.IDENTITY_VERIFICATION_IDT;
import static com.backbase.flow.sme.onboarding.TestConstants.SELECT_PRODUCTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.backbase.flow.service.companylookup.domain.CompanyLookupConstants;
import com.backbase.flow.sme.onboarding.TestConstants;
import com.backbase.flow.sme.onboarding.casedata.Applicant;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import com.backbase.flow.sme.onboarding.interaction.model.AnchorRequestDto;
import com.backbase.flow.sme.onboarding.interaction.model.CaseResponseDto;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class SmeInBranchOnboardingSoleProprietorshipIT
    extends
    SmeInBranchOnboardingIT<SmeInBranchOnboardingSoleProprietorshipIT.SmeInBranchOnboardingSoleProprietorshipStep> {

    @Override
    List<StepAction<SmeInBranchOnboardingSoleProprietorshipStep>> happyPath() {
        return List.of(
            new StepAction<>(SmeInBranchOnboardingSoleProprietorshipStep.START, () -> {
                performAnchorDataRequest(APPLICANT);
                checkAnchorData(FIRST_NAME);
            }),
            new StepAction<>(SmeInBranchOnboardingSoleProprietorshipStep.CHECK_CASE, () -> {
                var checkCaseExistsResponse = performCheckCaseRequest();
                checkCheckCaseExists(checkCaseExistsResponse, SELECT_PRODUCTS, FIRST_NAME);
            }),
            new StepAction<>(SmeInBranchOnboardingSoleProprietorshipStep.SELECT_PRODUCTS, () -> {
                performGetProductList();
                performProductSelectionRequest(DONE);
                checkProductSelection();
                checkCasePromoted();
            }),
            new StepAction<>(SmeInBranchOnboardingSoleProprietorshipStep.COMPANY_LOOKUP_IDT, () -> {
                setCurrentInteractionName(TestConstants.COMPANY_LOOKUP_IDT);
                var companyLookupInteractionId = startIdt(caseKey, COMPANY_LOOKUP, COMPANY_LOOKUP_USER_TASK_ID);
                setInteractionId(companyLookupInteractionId);
                checkEventWasRaised(COMPANY_LOOKUP_PENDING_EVENT);
                checkUserTaskIsActive(
                    CompanyLookupConstants.PROCESS_DEFINITION_KEY,
                    CompanyLookupConstants.COMPANY_LOOKUP_USER_TASK_ID);
            }),
            new StepAction<>(SmeInBranchOnboardingSoleProprietorshipStep.BUSINESS_STRUCTURE, () -> {
                performSoleProprietorshipBusinessStructureRequest(BUSINESS_DETAILS);
                checkCompanyLookupWillBeNotExecuted();
            }),
            new StepAction<>(SmeInBranchOnboardingSoleProprietorshipStep.BUSINESS_DETAILS, () -> {
                performBusinessDetailsRequest();
                checkBusinessDetails();
            }),
            new StepAction<>(SmeInBranchOnboardingSoleProprietorshipStep.BUSINESS_ADDRESS, () -> {
                performBusinessAddressRequest();
                checkBusinessAddress();
            }),
            new StepAction<>(SmeInBranchOnboardingSoleProprietorshipStep.BUSINESS_IDENTITY, () -> {
                performBusinessIdentityRequest(BUSINESS_IDENTITY_WITHOUT_LICENSE, DONE);
                checkBusinessIdentity(BUSINESS_IDENTITY_WITHOUT_LICENSE);
                checkEventWasRaised(COMPANY_LOOKUP_COMPLETE_EVENT);
                checkUserTaskIsCompleted(
                    CompanyLookupConstants.PROCESS_DEFINITION_KEY,
                    CompanyLookupConstants.COMPANY_LOOKUP_USER_TASK_ID);
            }),
            new StepAction<>(SmeInBranchOnboardingSoleProprietorshipStep.IDENTITY_VERIFICATION_IDT, () -> {
                setCurrentInteractionName(IDENTITY_VERIFICATION_IDT);
                var idvInteractionId = startIdt(caseKey, APPLICANT_ONBOARDING_PROCESS_ID, IDENTITY_VERIFICATION_IDT);
                setInteractionId(idvInteractionId);
                checkEventWasRaised(APPLICANT_ONBOARDING_PENDING_EVENT);
                checkUserTaskIsActive(APPLICANT_ONBOARDING_PROCESS_ID, IDENTITY_VERIFICATION_IDT);
            }),
            new StepAction(SmeInBranchOnboardingSoleProprietorshipStep.IDENTITY_VERIFICATION, () -> {
                performIdentityVerification(DONE, interactionId);
                checkIdentityVerification();
                checkUserTaskIsCompleted(APPLICANT_ONBOARDING_PROCESS_ID, IDENTITY_VERIFICATION_IDT);
            }),
            new StepAction<>(SmeInBranchOnboardingSoleProprietorshipStep.ADDRESS_IDT, () -> {
                setCurrentInteractionName(ADDRESS_VALIDATION_IDT);
                var addressInteractionId = startIdt(caseKey, APPLICANT_ONBOARDING_PROCESS_ID, ADDRESS_VALIDATION_IDT);
                setInteractionId(addressInteractionId);

            }),
            new StepAction<>(SmeInBranchOnboardingSoleProprietorshipStep.ADDRESS, () -> {
                performPersonalAddressRequest(DONE, interactionId);
                checkPersonalAddress();
                checkTasksForPersonalAddressValidation();
            })
        );
    }

    @Test
    void smeInBranchOnboardingSoleProprietorship_done_fullHappyPath() {
        assertDoesNotThrow(() -> happyPathUntil(SmeInBranchOnboardingSoleProprietorshipStep.DONE));
    }

    @Test
    void smeInBranchOnboardingSoleProprietorship_anchorData_noData() {
        var anchorDataRequest = AnchorRequestDto.builder().build();
        checkIncorrectAnchorData(anchorDataRequest);
    }

    @Test
    void smeInBranchOnboardingSoleProprietorship_anchorData_invalidData() {
        var anchorDataRequest = AnchorRequestDto.builder()
            .firstName(null)
            .lastName("this is a long enough name to trigger validation error")
            .dateOfBirth(LocalDate.now())
            .emailAddress("not@an@email")
            .phoneNumber("not-a-phone-number")
            .build();
        checkIncorrectAnchorData(anchorDataRequest);
    }

    private void checkIncorrectAnchorData(AnchorRequestDto anchorDataRequest) {
        checkIncorrectAnchorData(anchorDataRequest, TestConstants.IN_BRANCH_ONBOARDING_START);
    }

    @Test
    void smeInBranchOnboardingSoleProprietorship_checkCase_done() {
        var existingCaseData = new SmeCaseDefinition()
            .withApplicants(List.of(
                new Applicant()
                    .withIsRegistrar(true)
                    .withFirstName(FIRST_NAME)
                    .withLastName(LAST_NAME)
                    .withDateOfBirth(DATE_OF_BIRTH)
                    .withEmail(EMAIL)
                    .withPhoneNumber(PHONE_NUMBER))
            );
        var existingCase = startCase(existingCaseData);
        happyPathUntil(SmeInBranchOnboardingSoleProprietorshipStep.CHECK_CASE);

        var checkCaseInteractionResponse = performCheckCaseRequest();
        var checkCaseResponse = mapToObject(checkCaseInteractionResponse.getBody(), CaseResponseDto.class);
        assertThat(checkCaseResponse.isCaseExist()).isTrue();
        assertThat(checkCaseResponse.getCaseKey()).isEqualTo(existingCase.getKey().toString());

        var existingCaseInteractionResponse = performExistingCaseRequest();
        checkInteractionResponse(existingCaseInteractionResponse, DONE);
    }

    @Test
    void smeInBranchOnboardingSoleProprietorship_checkCase_noMatch() {
        var existingCaseData = new SmeCaseDefinition()
            .withApplicants(List.of(
                new Applicant()
                    .withIsRegistrar(true)
                    .withFirstName("different name")
                    .withLastName(LAST_NAME)
                    .withDateOfBirth(DATE_OF_BIRTH)
                    .withEmail(EMAIL)
                    .withPhoneNumber(PHONE_NUMBER))
            );
        var existingCase = startCase(existingCaseData);
        happyPathUntil(SmeInBranchOnboardingSoleProprietorshipStep.SELECT_PRODUCTS);
        var archivedCase = getCase(existingCase.getKey());
        assertThat(archivedCase.isArchived()).isTrue();
    }

    enum SmeInBranchOnboardingSoleProprietorshipStep {
        START,
        CHECK_CASE,
        SELECT_PRODUCTS,
        COMPANY_LOOKUP_IDT,
        BUSINESS_STRUCTURE,
        BUSINESS_DETAILS,
        BUSINESS_ADDRESS,
        BUSINESS_IDENTITY,
        IDENTITY_VERIFICATION_IDT,
        IDENTITY_VERIFICATION,
        ADDRESS_IDT,
        ADDRESS,
        DONE
    }
}
