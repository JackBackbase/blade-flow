package com.backbase.flow.onboarding.us.interaction.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.backbase.buildingblocks.presentation.errors.Error;
import com.backbase.flow.application.uso.casedata.AgreementInfo;
import com.backbase.flow.application.uso.casedata.AgreementInfo.PolicyType;
import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.ErrorCodes;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.interaction.dto.OnboardingDto;
import com.backbase.flow.onboarding.us.interaction.dto.PoliciesAgreementDto;
import com.backbase.flow.onboarding.us.service.policyagreement.PolicyAgreementService;
import com.backbase.flow.utils.CaseDataUtils;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import org.assertj.core.api.Condition;
import org.camunda.bpm.engine.runtime.MessageCorrelationBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class PoliciesAgreementsHandlerTest {

    private final MessageCorrelationBuilder messageCorrelationBuilder = mock(MessageCorrelationBuilder.class);
    private final InteractionContext context = mock(InteractionContext.class);
    private final CaseDataService caseDataService = mock(CaseDataService.class);

    private final CaseDataUtils caseDataUtils = new CaseDataUtils();
    private final PolicyAgreementService policyAgreementService = mock(PolicyAgreementService.class);

    private final PoliciesAgreementsHandler policiesAgreementsHandler = new PoliciesAgreementsHandler(caseDataUtils,
        policyAgreementService);

    @BeforeEach
    private void setup() {
        when(caseDataService.updateCase(any(Case.class)))
            .thenAnswer(a -> a.getArgument(0));
    }

    @Test
    void handleWorks() {
        final UUID caseKey = UUID.randomUUID();
        final PoliciesAgreementDto policiesAgreementDto = new PoliciesAgreementDto(true);
        final Onboarding caseData = new Onboarding().withMainApplicant(new Applicant()).withIsMainApplicantFlow(true);
        final Case sampleCase = new Case()
            .setKey(caseKey)
            .setCaseData(caseData);

        when(messageCorrelationBuilder.processInstanceVariableEquals(any(), any()))
            .thenReturn(messageCorrelationBuilder);
        when(context.getCaseKey()).thenReturn(caseKey);
        when(caseDataService.getCaseByKey(caseKey)).thenReturn(sampleCase);

        final ActionResult<OnboardingDto> actionResult = policiesAgreementsHandler
            .handle(policiesAgreementDto, context);

        assertThat(actionResult.isSuccessful()).isTrue();
        assertThat(actionResult.getErrors()).isEmpty();
    }

    @Test
    void handleValidationWorks() {
        final PoliciesAgreementDto policiesAgreementDto = new PoliciesAgreementDto(false);

        final ActionResult<OnboardingDto> actionResult = policiesAgreementsHandler
            .handle(policiesAgreementDto, context);

        final List<Error> errors = actionResult.getErrors();
        assertThat(errors).isNotEmpty();
        assertThat(errors.size()).isEqualTo(1);
        assertThat(errors.get(0).getKey()).isEqualTo(ErrorCodes.FLOW_001.getKey());
        assertThat(errors.get(0).getMessage()).isEqualTo(ErrorCodes.FLOW_001.getMessage());
        assertThat(errors.get(0).getContext()).containsEntry("agreedToTerms", "must be true");
    }

}
