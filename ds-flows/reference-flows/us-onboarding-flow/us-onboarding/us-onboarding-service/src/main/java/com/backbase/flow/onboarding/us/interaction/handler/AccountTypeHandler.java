package com.backbase.flow.onboarding.us.interaction.handler;

import static com.backbase.flow.onboarding.us.util.OnboardingMapper.MAPPER;

import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.interaction.dto.AccountTypeDto;
import com.backbase.flow.onboarding.us.interaction.dto.OnboardingDto;
import com.backbase.flow.utils.CaseDataUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("account-type-selector")
public class AccountTypeHandler extends ActionHandlerWithValidation<AccountTypeDto, OnboardingDto> {

    private final CaseDataUtils caseDataUtils;
    private final ObjectMapper objectMapper;

    @Override
    public ActionResult<OnboardingDto> handleWithValidation(AccountTypeDto accountTypeDto,
        InteractionContext interactionContext) {

        final Onboarding onboarding = interactionContext.getCaseData(Onboarding.class);
        onboarding.setIsJointAccount(accountTypeDto.getIsJointAccount());
        //noinspection unchecked
        caseDataUtils.saveCaseData(interactionContext, objectMapper.convertValue(onboarding, Map.class));

        return new ActionResult<>(MAPPER.mapToDto(onboarding));
    }
}
