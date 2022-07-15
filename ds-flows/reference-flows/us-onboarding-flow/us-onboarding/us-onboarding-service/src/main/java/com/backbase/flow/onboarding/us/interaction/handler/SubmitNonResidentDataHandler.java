package com.backbase.flow.onboarding.us.interaction.handler;

import static com.backbase.flow.onboarding.us.util.OnboardingMapper.MAPPER;

import com.backbase.buildingblocks.presentation.errors.Error;
import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.interaction.dto.NonResidentDataDto;
import com.backbase.flow.onboarding.us.interaction.dto.OnboardingDto;
import com.backbase.flow.onboarding.us.mapper.citizenship.NonResidentDataMapper;
import com.backbase.flow.onboarding.us.service.CountryService;
import com.backbase.flow.onboarding.us.util.OnboardingCaseDataUtils;
import com.backbase.flow.utils.CaseDataUtils;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("submit-non-resident-data")
@RequiredArgsConstructor
public class SubmitNonResidentDataHandler extends ActionHandlerWithValidation<NonResidentDataDto, OnboardingDto> {

    private final JourneyMapper<Onboarding> mapper;
    private final CaseDataUtils caseDataUtils;
    private final CountryService countryService;
    private final NonResidentDataMapper nonResidentDataMapper;

    @Override
    protected ActionResult<OnboardingDto> handleWithValidation(NonResidentDataDto payload,
        InteractionContext context) {
        if (!validateCountryCodes(payload)) {
            return new ActionResult<>(null, new Error("Error", "Invalid country code.", Map.of()));
        }

        final String caseKey = caseDataUtils.getOrCreateCaseKey(context).toString();
        final Onboarding onboarding = mapper.read(null, null, caseKey);
        final Applicant applicant = OnboardingCaseDataUtils.getApplicant(onboarding);

        var citizenship = applicant.getCitizenship();
        if (citizenship == null) {
            return new ActionResult<>(null,
                new Error("Error", "No citizenship info exists for given applicant.", Map.of()));
        }

        nonResidentDataMapper.mapNonResidentDataToExistingInfo(citizenship, payload, OffsetDateTime.now());

        mapper.write(onboarding, null, null, caseKey);
        final OnboardingDto onboardingDto = MAPPER.mapToDto(onboarding);
        return new ActionResult<>(onboardingDto);
    }

    private boolean validateCountryCodes(NonResidentDataDto payload) {
        Set<String> countryCodes = ImmutableSet.of(
            payload.getCitizenshipCountryCode(),
            payload.getResidencyAddress().getCountryCode()
        );
        Set<String> validCountryCodes = countryService.getCountryListMap().keySet();

        return validCountryCodes.containsAll(countryCodes);
    }
}
