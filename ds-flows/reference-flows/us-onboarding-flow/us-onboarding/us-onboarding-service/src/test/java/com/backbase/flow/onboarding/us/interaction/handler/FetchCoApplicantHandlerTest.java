package com.backbase.flow.onboarding.us.interaction.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.coapplicant.CoApplicantService;
import com.backbase.flow.onboarding.us.interaction.dto.CoApplicantDataRequestDto;
import com.backbase.flow.onboarding.us.interaction.dto.OnboardingDto;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FetchCoApplicantHandlerTest {

    @Mock
    private CoApplicantService coApplicantService;
    @Mock
    private JourneyMapper<Onboarding> journeyMapper;
    @Mock
    private InteractionContext interactionContext;
    @InjectMocks
    private FetchCoApplicantHandler fetchCoApplicantHandler;

    @Test
    void handle_noCoApplicant() {
        CoApplicantDataRequestDto coApplicantDataRequestDto = new CoApplicantDataRequestDto(null);
        ActionResult<OnboardingDto> result = fetchCoApplicantHandler
            .handle(coApplicantDataRequestDto, interactionContext);
        assertThat(result.isSuccessful()).isTrue();
        assertThat(result.getBody()).isNull();
    }

    @Test
    void handle_coApplicant_notFound() {
        when(coApplicantService.resumeCoApplicantCaseAndInteractionStep(any(), any())).thenReturn(Optional.empty());

        CoApplicantDataRequestDto coApplicantDataRequestDto = new CoApplicantDataRequestDto(UUID.randomUUID());
        ActionResult<OnboardingDto> result = fetchCoApplicantHandler
            .handle(coApplicantDataRequestDto, interactionContext);

        assertThat(result.isSuccessful()).isFalse();
    }

    @Test
    void handle_coApplicant_found() {
        when(coApplicantService.resumeCoApplicantCaseAndInteractionStep(any(), any()))
            .thenReturn(Optional.of(new Case()));
        when(journeyMapper.read(any(), any(), any(Case.class)))
            .thenReturn(new Onboarding()
                .withIsMainApplicantFlow(false)
                .withCoApplicant(new Applicant()));

        CoApplicantDataRequestDto coApplicantDataRequestDto = new CoApplicantDataRequestDto(UUID.randomUUID());
        ActionResult<OnboardingDto> result = fetchCoApplicantHandler
            .handle(coApplicantDataRequestDto, interactionContext);

        assertThat(result.isSuccessful()).isTrue();
        assertThat(result.getBody()).isNotNull();
    }
}