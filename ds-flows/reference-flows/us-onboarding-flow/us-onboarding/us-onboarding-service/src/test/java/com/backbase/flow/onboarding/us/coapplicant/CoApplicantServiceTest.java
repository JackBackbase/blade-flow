package com.backbase.flow.onboarding.us.coapplicant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.iam.FlowSecurityContext;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.interaction.engine.data.model.InteractionInstance;
import com.backbase.flow.interaction.engine.data.model.StepInstance;
import com.backbase.flow.service.resume.stepfinder.StepFinder;
import com.backbase.flow.service.resume.util.ResumeUtils;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CoApplicantServiceTest {

    private static final String SUCCESSFULLY_DONE_CO_APPLICANT_STEP_NAME = "successfully-done-co-applicant";
    private static final String TEST_STEP_NAME = "test-step-name";

    @Mock
    private ResumeUtils resumeUtils;
    @Mock
    private FlowSecurityContext flowSecurityContext;
    @Mock
    private StepFinder stepFinder;

    @InjectMocks
    private CoApplicantService coApplicantService;

    @Mock
    private InteractionContext interactionContext;
    private final Case previousCase = new Case();

    @Test
    void resumeCoApplicantCaseAndInteractionStep_caseNotFound() {
        when(resumeUtils.getExistingCaseBySpecifiedField(any())).thenReturn(Optional.empty());

        Optional<Case> optionalCase = coApplicantService
            .resumeCoApplicantCaseAndInteractionStep(interactionContext, UUID.randomUUID());

        assertThat(optionalCase).isNotPresent();
    }

    @Test
    void resumeCoApplicantCaseAndInteractionStep_caseFound_interactionNotFound() {
        when(resumeUtils.getExistingCaseBySpecifiedField(any())).thenReturn(Optional.of(previousCase));
        when(flowSecurityContext.runWithoutAuthorization(any(Callable.class))).thenReturn(Optional.empty());

        Optional<Case> optionalCase = coApplicantService
            .resumeCoApplicantCaseAndInteractionStep(interactionContext, UUID.randomUUID());

        assertThat(optionalCase).isNotPresent();
    }

    @Test
    void resumeCoApplicantCaseAndInteractionStep_caseFound_interactionFound_lastStepIsExitPoint() {
        InteractionInstance interactionInstance = new InteractionInstance();
        interactionInstance.setCurrentStep(new StepInstance(SUCCESSFULLY_DONE_CO_APPLICANT_STEP_NAME));

        when(resumeUtils.getExistingCaseBySpecifiedField(any())).thenReturn(Optional.of(previousCase));
        when(flowSecurityContext.runWithoutAuthorization(any(Callable.class)))
            .thenReturn(Optional.of(interactionInstance));
        when(interactionContext.getInteractionId()).thenReturn(UUID.randomUUID());
        doNothing().when(resumeUtils).swapUnderlyingCase(any(UUID.class), any(UUID.class), anyLong());

        Optional<Case> optionalCase = coApplicantService
            .resumeCoApplicantCaseAndInteractionStep(interactionContext, UUID.randomUUID());

        verify(resumeUtils).swapUnderlyingCase(any(UUID.class), any(UUID.class), anyLong());
        verify(resumeUtils, times(0)).setStepForInteraction(any(), any());
        assertThat(optionalCase.orElseThrow().getKey()).isEqualTo(previousCase.getKey());
    }

    @Test
    void resumeCoApplicantCaseAndInteractionStep_case_found_interaction_found() {
        InteractionInstance interactionInstance = new InteractionInstance();
        interactionInstance.setCurrentStep(new StepInstance(TEST_STEP_NAME));

        when(resumeUtils.getExistingCaseBySpecifiedField(any())).thenReturn(Optional.of(previousCase));
        when(flowSecurityContext.runWithoutAuthorization(any(Callable.class)))
            .thenReturn(Optional.of(interactionInstance));
        when(interactionContext.getInteractionId()).thenReturn(UUID.randomUUID());
        doNothing().when(resumeUtils).swapUnderlyingCase(any(UUID.class), any(UUID.class), anyLong());
        when(resumeUtils.getLatestInteractionInstance(any())).thenReturn(Optional.of(interactionInstance));
        when(stepFinder.getResumeToStep(any(), any())).thenReturn(new StepInstance());
        doNothing().when(resumeUtils).setStepForInteraction(any(), any());

        Optional<Case> optionalCase = coApplicantService
            .resumeCoApplicantCaseAndInteractionStep(interactionContext, UUID.randomUUID());

        verify(resumeUtils).swapUnderlyingCase(any(UUID.class), any(UUID.class), anyLong());
        verify(resumeUtils).setStepForInteraction(any(), any());
        assertThat(optionalCase.orElseThrow().getKey()).isEqualTo(previousCase.getKey());
    }

}