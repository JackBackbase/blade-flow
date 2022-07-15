package com.backbase.flow.onboarding.us.helpers;

import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.interaction.api.model.InteractionResponseDto;
import com.backbase.flow.onboarding.us.BaseIT;

public class IdentityVerifiedStepCalls extends BaseIT {

    public static InteractionResponseDto moveViaEmptyHandler(final String interactionId) throws Exception {
        final InteractionRequestDto request = new InteractionRequestDto();
        request.setInteractionId(interactionId);

        return performAction("empty-handler", request);
    }
}
