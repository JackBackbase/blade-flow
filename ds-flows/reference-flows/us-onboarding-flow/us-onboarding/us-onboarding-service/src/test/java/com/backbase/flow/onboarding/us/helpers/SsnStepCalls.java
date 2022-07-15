package com.backbase.flow.onboarding.us.helpers;

import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.interaction.api.model.InteractionResponseDto;
import com.backbase.flow.onboarding.us.BaseIT;
import com.backbase.flow.onboarding.us.interaction.dto.SsnDto;

public class SsnStepCalls extends BaseIT {

    public static InteractionResponseDto submitSsn(final String interactionId) throws Exception {
        final SsnDto ssnDto = new SsnDto("123456789");
        final InteractionRequestDto request = new InteractionRequestDto();
        request.setInteractionId(interactionId);
        request.setPayload(ssnDto);

        return performAction("submit-ssn", request);
    }

    public static InteractionResponseDto submitIncorrectSsn(final String interactionId) throws Exception {
        final SsnDto ssnDto = new SsnDto("123-45-6789");
        final InteractionRequestDto request = new InteractionRequestDto();
        request.setInteractionId(interactionId);
        request.setPayload(ssnDto);

        return performAction("submit-ssn", request);
    }
}