package com.backbase.flow.onboarding.us.interaction.handler;

import static com.backbase.flow.onboarding.us.util.OnboardingMapper.MAPPER;

import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.interaction.dto.OnboardingDto;
import com.backbase.flow.onboarding.us.interaction.dto.PoliciesAgreementDto;
import com.backbase.flow.onboarding.us.service.policyagreement.PolicyAgreementService;
import com.backbase.flow.utils.CaseDataUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component("agree-to-terms")
public class PoliciesAgreementsHandler extends ActionHandlerWithValidation<PoliciesAgreementDto, OnboardingDto> {

    private final CaseDataUtils caseDataUtils;
    private final PolicyAgreementService policyAgreementService;

    @Override
    protected ActionResult<OnboardingDto> handleWithValidation(final PoliciesAgreementDto policiesAgreement,
        final InteractionContext context) {

        final UUID caseKey = caseDataUtils.getOrCreateCaseKey(context);

        Onboarding onboarding = policyAgreementService.processPolicies(policiesAgreement, caseKey);

        return new ActionResult<>(MAPPER.mapToDto(onboarding));
    }
}
