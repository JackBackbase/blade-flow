package com.backbase.flow.onboarding.us.helpers;

import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.interaction.api.model.InteractionResponseDto;
import com.backbase.flow.onboarding.us.BaseIT;
import com.backbase.flow.onboarding.us.interaction.dto.AccountTypeDto;

public class AccountTypeStepCalls extends BaseIT {

    private static final String ACTION = "account-type-selector";

    public static InteractionResponseDto submitJointAccountType(final String interactionId) throws Exception {
        return performAction(ACTION, createValidPayload(interactionId, true));
    }

    public static InteractionResponseDto submitIndividualAccountType(final String interactionId) throws Exception {
        return performAction(ACTION, createValidPayload(interactionId, false));
    }

    private static InteractionRequestDto createValidPayload(String interactionId, boolean isJointAccount) {
        final AccountTypeDto accountTypeDto =
            new AccountTypeDto(isJointAccount);

        final InteractionRequestDto request = new InteractionRequestDto();
        request.setInteractionId(interactionId);
        request.setPayload(accountTypeDto);
        return request;
    }
}
