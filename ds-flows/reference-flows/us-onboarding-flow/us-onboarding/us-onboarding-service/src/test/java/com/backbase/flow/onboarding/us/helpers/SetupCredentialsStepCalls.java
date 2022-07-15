package com.backbase.flow.onboarding.us.helpers;

import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.interaction.api.model.InteractionResponseDto;
import com.backbase.flow.onboarding.us.BaseIT;
import com.backbase.flow.onboarding.us.interaction.dto.CredentialsDto;

public class SetupCredentialsStepCalls extends BaseIT {

    public static InteractionResponseDto setupCredentials(final String interactionId) throws Exception {
        final CredentialsDto credentialsDto = CredentialsDto.builder()
            .password("somePassword1")
            .build();

        final InteractionRequestDto request = new InteractionRequestDto();
        request.setInteractionId(interactionId);
        request.setPayload(credentialsDto);

        return performAction("setup-credentials", request);
    }

    public static InteractionResponseDto setupInvalidPassword(final String interactionId) throws Exception {
        final CredentialsDto credentialsDto = CredentialsDto.builder()
            .password("_1")
            .build();

        final InteractionRequestDto request = new InteractionRequestDto();
        request.setInteractionId(interactionId);
        request.setPayload(credentialsDto);

        return performAction("setup-credentials", request);
    }

    public static InteractionResponseDto moveViaEmptyHandler(final String interactionId) throws Exception {
        final InteractionRequestDto request = new InteractionRequestDto();
        request.setInteractionId(interactionId);

        return performAction("empty-handler", request);
    }
}