package com.backbase.flow.onboarding.us.interaction.handler;

import static com.backbase.flow.onboarding.us.util.OnboardingMapper.MAPPER;

import com.backbase.buildingblocks.presentation.errors.Error;
import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.KycAnswers;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.interaction.engine.action.ActionHandler;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.Errors;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.interaction.engine.action.validation.HandlerPayloadValidator;
import com.backbase.flow.onboarding.us.interaction.dto.KycDto;
import com.backbase.flow.onboarding.us.interaction.dto.OnboardingDto;
import com.backbase.flow.onboarding.us.kyc.service.KycService;
import com.backbase.flow.onboarding.us.util.OnboardingCaseDataUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("submit-kyc-data")
@Slf4j
public class KycHandler implements ActionHandler<KycDto, OnboardingDto> {

    private final KycService kycService;
    private final JourneyMapper<Onboarding> journeyMapper;

    @Override
    public ActionResult<OnboardingDto> handle(final KycDto kycPage, final InteractionContext context) {
        final Errors violations = new HandlerPayloadValidator<>().validate(kycPage);
        UUID caseKey = context.getCaseKey();
        final Onboarding onboarding = journeyMapper.read(null, null, caseKey.toString());
        final Applicant applicant = OnboardingCaseDataUtils.getApplicant(onboarding);
        final OnboardingDto result;
        if (!violations.isEmpty()) {
            result = MAPPER.mapToDto(onboarding);
            return new ActionResult<>(result, violations);
        }

        String kycName =
            applicant.getAntiMoneyLaunderingInfo().getAmlResult().getMatchStatus().equalsIgnoreCase("no_match")
                ? "kyc/kyc.json"
                : "kyc/kyc2.json";

        ActionResult<KycAnswers> kycResult = kycService.handleKycPage(kycPage, kycName);

        if (!kycResult.getErrors().isEmpty()) {
            Errors errors = new Errors();
            for (Error error : kycResult.getErrors()) {
                errors.add(error);
            }
            result = MAPPER.mapToDto(onboarding);
            return new ActionResult<>(result, errors);
        }

        applicant.setKycInformation(kycResult.getBody());

        final Case updatedCase = journeyMapper.write(onboarding, null, null, caseKey.toString());
        final Onboarding updatedOnboarding = journeyMapper.read(null, null, updatedCase);

        result = MAPPER.mapToDto(updatedOnboarding);
        return new ActionResult<>(result, violations);
    }

}
