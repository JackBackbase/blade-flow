package com.backbase.flow.onboarding.us.interaction.handler;

import static com.backbase.flow.onboarding.us.util.OnboardingMapper.MAPPER;

import com.backbase.buildingblocks.presentation.errors.Error;
import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.interaction.dto.OnboardingDto;
import com.backbase.flow.onboarding.us.interaction.dto.SsnDto;
import com.backbase.flow.onboarding.us.util.OnboardingCaseDataUtils;
import com.backbase.flow.utils.CaseDataUtils;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("submit-ssn")
@RequiredArgsConstructor
public class SubmitSsnHandler extends ActionHandlerWithValidation<SsnDto, OnboardingDto> {

    private final JourneyMapper<Onboarding> mapper;
    private final CaseDataUtils caseDataUtils;

    @Override
    protected ActionResult<OnboardingDto> handleWithValidation(SsnDto ssnDto, InteractionContext context) {
        final String caseKey = caseDataUtils.getOrCreateCaseKey(context).toString();
        final Onboarding onboarding = mapper.read(null, null, caseKey);
        final Applicant applicant = OnboardingCaseDataUtils.getApplicant(onboarding);

        if (applicant.getCitizenship() == null ||
            applicant.getCitizenship().getCitizenshipType() == null) {
            return buildError();
        }

        applicant.getCitizenship().setSsn(ssnDto.getSsn());

        mapper.write(onboarding, null, null, caseKey);
        final OnboardingDto onboardingDto = MAPPER.mapToDto(onboarding);
        return new ActionResult<>(onboardingDto);
    }

    private ActionResult<OnboardingDto> buildError() {
        return new ActionResult<>(null, new Error(
            "Error",
            "Citizenship type must be defined before submit SSN",
            Collections.emptyMap()));
    }

}
