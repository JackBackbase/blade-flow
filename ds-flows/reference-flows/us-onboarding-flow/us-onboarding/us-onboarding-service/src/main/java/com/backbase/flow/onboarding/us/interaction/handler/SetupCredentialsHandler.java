package com.backbase.flow.onboarding.us.interaction.handler;

import static com.backbase.flow.onboarding.us.util.OnboardingMapper.MAPPER;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.interaction.dto.CredentialsDto;
import com.backbase.flow.onboarding.us.interaction.dto.response.CredentialsResponseDto;
import com.backbase.flow.onboarding.us.process.OnboardingProcessMessagingService;
import com.backbase.flow.onboarding.us.util.OnboardingCaseDataUtils;
import com.backbase.flow.utils.CaseDataUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("setup-credentials")
@Slf4j
@RequiredArgsConstructor
public class SetupCredentialsHandler extends ActionHandlerWithValidation<CredentialsDto, CredentialsResponseDto> {

    private final JourneyMapper<Onboarding> journeyMapper;
    private final CaseDataUtils caseDataUtils;
    private final OnboardingProcessMessagingService onboardingProcessMessagingService;

    @Override
    protected ActionResult<CredentialsResponseDto> handleWithValidation(final CredentialsDto credentials,
        final InteractionContext context) {

        final UUID caseKey = caseDataUtils.getOrCreateCaseKey(context);

        log.info("Handling Setup Credentials for caseKey: {}", caseKey);

        final Onboarding onboarding = journeyMapper.read(null, null, caseKey.toString());

        final Applicant applicant = OnboardingCaseDataUtils.getApplicant(onboarding);

        onboardingProcessMessagingService.setCustomerInputCompleted(caseKey.toString());

        return new ActionResult<>(MAPPER.mapToCredentialsResponseDto(applicant, onboarding.getIsJointAccount(), onboarding.getIsMainApplicantFlow()));
    }
}
