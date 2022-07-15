package com.backbase.flow.onboarding.us.interaction.handler;

import static com.backbase.flow.onboarding.us.util.OnboardingMapper.MAPPER;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.CitizenshipInfo;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.interaction.dto.CitizenshipTypeDto;
import com.backbase.flow.onboarding.us.interaction.dto.OnboardingDto;
import com.backbase.flow.onboarding.us.util.OnboardingCaseDataUtils;
import com.backbase.flow.utils.CaseDataUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("citizenship-selector")
public class CitizenshipSelectorHandler extends ActionHandlerWithValidation<CitizenshipTypeDto, OnboardingDto> {

    private final CaseDataUtils caseDataUtils;
    private final JourneyMapper<Onboarding> journeyMapper;

    @Override
    public ActionResult<OnboardingDto> handleWithValidation(CitizenshipTypeDto citizenshipTypeRequest,
        InteractionContext context) {
        final String caseKey = caseDataUtils.getOrCreateCaseKey(context).toString();
        final Onboarding onboarding = journeyMapper.read(null, null, caseKey);
        final Applicant applicant = OnboardingCaseDataUtils.getApplicant(onboarding);

        CitizenshipInfo citizenshipInfo = new CitizenshipInfo()
            .withCitizenshipType(citizenshipTypeRequest.getType());
        applicant.setCitizenship(citizenshipInfo);

        journeyMapper.write(onboarding, null, null, caseKey);
        return new ActionResult<>(MAPPER.mapToDto(onboarding));
    }
}
