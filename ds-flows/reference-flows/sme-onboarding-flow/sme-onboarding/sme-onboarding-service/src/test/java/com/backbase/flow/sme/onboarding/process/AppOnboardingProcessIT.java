package com.backbase.flow.sme.onboarding.process;

import static com.backbase.flow.sme.onboarding.TestConstants.AML_PENDING_EVENT;
import static com.backbase.flow.sme.onboarding.TestConstants.AML_SUCCEED_EVENT;
import static com.backbase.flow.sme.onboarding.TestConstants.APPLICANT_ONBOARDING_FINISHED_EVENT;
import static com.backbase.flow.sme.onboarding.TestConstants.APPLICANT_ONBOARDING_PENDING_EVENT;
import static com.backbase.flow.sme.onboarding.constants.CaseDefinitionConstants.APPLICANT_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.backbase.flow.iam.util.WithFlowAnonymousUser;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.sme.onboarding.BaseIntegrationIT;
import com.backbase.flow.sme.onboarding.casedata.BusinessStructureInfo;
import com.backbase.flow.sme.onboarding.casedata.CompanyLookupInfo;
import com.backbase.flow.sme.onboarding.casedata.IdentityVerificationResult;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import com.backbase.flow.sme.onboarding.casedata.TermsAndConditions;
import com.backbase.flow.sme.onboarding.constants.ProcessConstants;
import com.backbase.flow.test.interaction.FakeInteractionContext;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AppOnboardingProcessIT extends BaseIntegrationIT {

    @Test
    @WithFlowAnonymousUser
    void startProcess_happyFlow_shouldCompleteApplicantOnboardingProcess() {
        //given
        final var mainApplicantId = UUID.randomUUID();
        var caseDef = createCase(mainApplicantId, "approved");
        var exCase = startCase(caseDef);
        var fakeInteractionContext = new FakeInteractionContext(exCase);

        //when
        runProcess(fakeInteractionContext, mainApplicantId);

        //then
        assertThat(
            containsAllEvents(fakeInteractionContext.getCaseKey(),
                List.of(APPLICANT_ONBOARDING_PENDING_EVENT,
                    AML_PENDING_EVENT,
                    AML_SUCCEED_EVENT,
                    APPLICANT_ONBOARDING_FINISHED_EVENT)))
            .isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"failed", "no_result", "review"})
    @WithFlowAnonymousUser
    void startProcess_idvNegativeResults_shouldNotCompleteApplicantOnboardingProcess(String verificationIdResult) {
        //given
        final var mainApplicantId = UUID.randomUUID();
        var caseDef = createCase(mainApplicantId, verificationIdResult);
        var exCase = startCase(caseDef);
        var fakeInteractionContext = new FakeInteractionContext(exCase);

        //when
        runProcess(fakeInteractionContext, mainApplicantId);

        //then
        assertThat(
            containsAllEvents(fakeInteractionContext.getCaseKey(),
                List.of(APPLICANT_ONBOARDING_PENDING_EVENT,
                    AML_PENDING_EVENT,
                    AML_SUCCEED_EVENT)))
            .isTrue();
        assertThat(
            containsEvent(fakeInteractionContext.getCaseKey(), APPLICANT_ONBOARDING_FINISHED_EVENT))
            .isFalse();
    }

    private SmeCaseDefinition createCase(UUID mainApplicantId, String verificationResultId) {
        return new SmeCaseDefinition()
            .withApplicants(List.of(getMainApplicant(mainApplicantId)))
            .withCompanyLookupInfo(
                new CompanyLookupInfo().withBusinessStructureInfo(
                    new BusinessStructureInfo().withType(BusinessStructureInfo.Type.SOLE_PROPRIETORSHIP)))
            .withTermsAndConditions(createTermsAndConditions())
            .withIdentityVerificationResult(createIdentityVerificationResult(verificationResultId));
    }

    private IdentityVerificationResult createIdentityVerificationResult(String verificationIdResult) {
        return new IdentityVerificationResult()
            .withAdditionalProperty("verificationId", verificationIdResult)
            .withAdditionalProperty("documentStatus", "documentStatus")
            .withAdditionalProperty("filesetNameSuffix", "filesetNameSuffix");
    }

    private TermsAndConditions createTermsAndConditions() {
        return new TermsAndConditions()
            .withAccepted(true)
            .withVersion("1")
            .withAcceptanceDate(OffsetDateTime.now());
    }

    private void runProcess(InteractionContext interactionContext, UUID mainApplicantId) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(APPLICANT_ID, mainApplicantId);
        flowProcessService.startProcess(interactionContext.getCaseKey(), ProcessConstants.BPM_APP_ONBOARDING,
            variables);
        completeAddressValidationIdtHook.executeHook(interactionContext);
        completeIdvIdtHook.executeHook(interactionContext);
    }
}
