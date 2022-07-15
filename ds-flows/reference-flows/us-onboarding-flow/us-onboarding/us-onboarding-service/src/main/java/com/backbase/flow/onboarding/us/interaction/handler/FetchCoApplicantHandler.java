package com.backbase.flow.onboarding.us.interaction.handler;

import static com.backbase.flow.onboarding.us.util.OnboardingMapper.MAPPER;

import com.backbase.buildingblocks.presentation.errors.Error;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.interaction.engine.action.ActionHandler;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.coapplicant.CoApplicantService;
import com.backbase.flow.onboarding.us.interaction.dto.CoApplicantDataRequestDto;
import com.backbase.flow.onboarding.us.interaction.dto.OnboardingDto;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("fetch-co-applicant")
@RequiredArgsConstructor
public class FetchCoApplicantHandler implements ActionHandler<CoApplicantDataRequestDto, OnboardingDto> {

    private final CoApplicantService coApplicantService;
    private final JourneyMapper<Onboarding> journeyMapper;

    @Override
    public ActionResult<OnboardingDto> handle(CoApplicantDataRequestDto coApplicantDataRequestDto, InteractionContext context) {

        if (coApplicantDataRequestDto.getCoApplicantId() == null) {
            return new ActionResult<>(null);
        }

        Optional<Case> optionalCase = coApplicantService
            .resumeCoApplicantCaseAndInteractionStep(context, coApplicantDataRequestDto.getCoApplicantId());

        if (optionalCase.isEmpty()) {
            return new ActionResult<>(null, new Error(
                "Error",
                "No co-applicant data found",
                Collections.emptyMap()));
        }

        return new ActionResult<>(MAPPER.mapToDto(journeyMapper.read(null, null, optionalCase.get())));
    }
}
