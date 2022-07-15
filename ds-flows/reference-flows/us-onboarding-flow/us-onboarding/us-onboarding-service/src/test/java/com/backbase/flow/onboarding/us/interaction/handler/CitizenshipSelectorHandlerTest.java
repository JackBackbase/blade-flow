package com.backbase.flow.onboarding.us.interaction.handler;

import static com.backbase.flow.onboarding.us.validation.ValidationConstants.CITIZENSHIP_NULL_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backbase.buildingblocks.presentation.errors.Error;
import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.CitizenshipInfo;
import com.backbase.flow.application.uso.casedata.CitizenshipInfo.CitizenshipType;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.ErrorCodes;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.interaction.dto.CitizenshipTypeDto;
import com.backbase.flow.onboarding.us.interaction.dto.OnboardingDto;
import com.backbase.flow.utils.CaseDataUtils;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class CitizenshipSelectorHandlerTest {

    private final InteractionContext interactionContext = mock(InteractionContext.class);
    private final CaseDataUtils caseDataUtils = mock(CaseDataUtils.class);
    @SuppressWarnings("unchecked")
    private final JourneyMapper<Onboarding> mapper = mock(JourneyMapper.class);
    private final CitizenshipSelectorHandler handler = new CitizenshipSelectorHandler(caseDataUtils, mapper);
    private final UUID caseKey = UUID.randomUUID();

    @ParameterizedTest
    @EnumSource(CitizenshipType.class)
    void correctRequest(final CitizenshipType type) {
        final CitizenshipTypeDto request = new CitizenshipTypeDto(type);
        final Onboarding onboarding = new Onboarding()
            .withIsMainApplicantFlow(true)
            .withMainApplicant(new Applicant());
        final Onboarding expected = new Onboarding()
            .withIsMainApplicantFlow(true)
            .withMainApplicant(new Applicant()
                .withCitizenship(new CitizenshipInfo()
                    .withCitizenshipType(type)));

        when(caseDataUtils.getOrCreateCaseKey(interactionContext)).thenReturn(caseKey);
        when(mapper.read(null, null, caseKey.toString())).thenReturn(onboarding);
        ActionResult<OnboardingDto> result = handler.handle(request, interactionContext);

        verify(mapper, times(1)).write(expected, null, null, caseKey.toString());
        assertThat(result.getBody().getCitizenshipType()).isEqualTo(type);
    }

    @Test
    void nullRequest_shouldReturnError() {
        final CitizenshipTypeDto request = new CitizenshipTypeDto(null);

        ActionResult<OnboardingDto> result = handler.handle(request, interactionContext);

        final List<Error> errors = result.getErrors();
        assertThat(errors).isNotEmpty();
        assertThat(errors.size()).isEqualTo(1);
        assertThat(errors.get(0).getKey()).isEqualTo(ErrorCodes.FLOW_001.getKey());
        assertThat(errors.get(0).getMessage()).isEqualTo(ErrorCodes.FLOW_001.getMessage());
        assertThat(errors.get(0).getContext().get("type")).contains(CITIZENSHIP_NULL_MESSAGE);
    }
}