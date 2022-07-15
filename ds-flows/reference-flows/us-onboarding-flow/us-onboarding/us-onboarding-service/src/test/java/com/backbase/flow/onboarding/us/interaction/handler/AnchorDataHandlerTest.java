package com.backbase.flow.onboarding.us.interaction.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.error.OnboardingError;
import com.backbase.flow.onboarding.us.interaction.dto.AnchorDataDto;
import com.backbase.flow.onboarding.us.interaction.dto.OnboardingDto;
import com.backbase.flow.utils.CaseDataUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AnchorDataHandlerTest {

    private final InteractionContext interactionContext = Mockito.mock(InteractionContext.class);
    private final CaseDataUtils caseDataUtils = mock(CaseDataUtils.class);
    private final JourneyMapper<Onboarding> journeyMapper = mock(JourneyMapper.class);

    private final AnchorDataHandler anchorDataHandler = new AnchorDataHandler(caseDataUtils, journeyMapper);

    @BeforeEach
    void mapp() {
        when(journeyMapper.write(any(), any(), any(), any(Case.class)))
            .thenAnswer(a -> a.getArgument(0));
        when(journeyMapper.read(any(), any(), any(Case.class)))
            .thenAnswer(a -> ((Case) a.getArgument(2)).getCaseData(Onboarding.class));
    }

    @Test
    void handleWithChecks() {
        final AnchorDataDto anchorData = sampleAnchorDto();
        when(caseDataUtils.getOrCreateCaseKey(any())).thenReturn(UUID.randomUUID());
        when(journeyMapper.read(any(), any(), anyString()))
            .thenReturn(new Onboarding().withMainApplicant(new Applicant()));
        Case aCase = new Case();
        aCase.setCaseData(Map.of("mainApplicant", anchorData, "isMainApplicantFlow", true));
        when(journeyMapper.write(any(), any(), any(), anyString())).thenReturn(aCase);
        final List<Map<String, Object>> decisionResponse = getDecisionResponse(true);
        when(interactionContext.checkDecisionTable(any(), any(), any())).thenReturn(decisionResponse);

        final ActionResult<OnboardingDto> actionResult = anchorDataHandler.handle(anchorData, interactionContext);
        assertThat(actionResult.getBody().getFirstName()).isEqualTo(anchorData.getFirstName());
        assertThat(actionResult.getBody().getLastName()).isEqualTo(anchorData.getLastName());
        assertThat(actionResult.getBody().getDateOfBirth()).isEqualTo(anchorData.getDateOfBirth());
        assertThat(actionResult.getBody().getEmail()).isEqualTo(anchorData.getEmail());
        assertThat(actionResult.getErrors()).isEmpty();
    }

    @Test
    void ageNotValidShouldReturnError() {
        final AnchorDataDto anchorData = sampleAnchorDto();
        when(caseDataUtils.getOrCreateCaseKey(any())).thenReturn(UUID.randomUUID());
        when(journeyMapper.read(any(), any(), anyString()))
            .thenReturn(new Onboarding().withMainApplicant(new Applicant()).withIsMainApplicantFlow(true));
        Case aCase = new Case();
        aCase.setCaseData(Map.of("mainApplicant", anchorData, "isMainApplicantFlow", true));
        when(journeyMapper.write(any(), any(), any(), anyString())).thenReturn(aCase);
        final List<Map<String, Object>> decisionResponse = getDecisionResponse(false);
        when(interactionContext.checkDecisionTable(any(), any(), any())).thenReturn(decisionResponse);

        final ActionResult<OnboardingDto> actionResult = anchorDataHandler.handle(anchorData, interactionContext);
        assertThat(actionResult.getBody().getFirstName()).isEqualTo(anchorData.getFirstName());
        assertThat(actionResult.getBody().getLastName()).isEqualTo(anchorData.getLastName());
        assertThat(actionResult.getBody().getDateOfBirth()).isEqualTo(anchorData.getDateOfBirth());
        assertThat(actionResult.getErrors().size()).isEqualTo(1);
        assertThat(actionResult.getErrors().get(0).getKey())
            .isEqualTo(OnboardingError.ANCHOR_DATA_FAILED.getError().getKey());
        assertThat(actionResult.getErrors().get(0).getMessage())
            .isEqualTo(OnboardingError.ANCHOR_DATA_FAILED.getError().getMessage());

        assertThat(actionResult.getErrors().get(0).getContext())
            .containsEntry("dateOfBirth", "Customer's age should be between 18 and 100 years old.");
    }

    @Test
    void validationWorks() {
        final AnchorDataDto anchorData = AnchorDataDto.builder()
            .firstName(RandomStringUtils.random(51))
            .lastName(RandomStringUtils.random(51))
            .dateOfBirth("invalidDate")
            .email("john.doe")
            .build();

        final ActionResult<OnboardingDto> actionResult = anchorDataHandler.handle(anchorData, interactionContext);
        assertThat(actionResult.getErrors()).isNotEmpty();
        assertThat(actionResult.getErrors().size()).isEqualTo(4);
        verify(caseDataUtils, times(0)).saveCaseData(any(), any());
    }

    private List<Map<String, Object>> getDecisionResponse(final boolean decisionResult) {
        final Map<String, Object> variables = new HashMap<>();
        variables.put("age-accepted", decisionResult);
        return Collections.singletonList(variables);
    }

    private AnchorDataDto sampleAnchorDto() {
        return AnchorDataDto.builder()
            .firstName("John")
            .lastName("Doe")
            .dateOfBirth("2000-01-01")
            .email("john.doe@backbase.com")
            .build();
    }
}
