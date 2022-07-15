package com.backbase.flow.onboarding.us.helpers;


import com.backbase.flow.application.uso.casedata.CitizenshipInfo.CitizenshipType;
import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.interaction.api.model.InteractionResponseDto;
import com.backbase.flow.onboarding.us.BaseIT;
import com.backbase.flow.onboarding.us.interaction.dto.CitizenshipTypeDto;

public class CitizenshipStepCalls extends BaseIT {

    public static InteractionResponseDto submitCitizenshipType(final String interactionId, CitizenshipType type)
        throws Exception {
        final CitizenshipTypeDto citizenshipTypeDto = new CitizenshipTypeDto(type);
        final InteractionRequestDto request = new InteractionRequestDto();
        request.setInteractionId(interactionId);
        request.setPayload(citizenshipTypeDto);

        return performAction("citizenship-selector", request);
    }

    public static InteractionResponseDto fetchCitizenshipInfo(final String interactionId) throws Exception {
        final InteractionRequestDto request = new InteractionRequestDto();
        request.setInteractionId(interactionId);

        return performAction("fetch-citizenship-data", request);
    }
}

