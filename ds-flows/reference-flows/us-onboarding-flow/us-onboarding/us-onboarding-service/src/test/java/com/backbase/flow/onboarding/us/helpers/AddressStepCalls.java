package com.backbase.flow.onboarding.us.helpers;

import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.interaction.api.model.InteractionResponseDto;
import com.backbase.flow.onboarding.us.BaseIT;
import com.backbase.flow.service.dto.AddressDto;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class AddressStepCalls extends BaseIT {

    public static InteractionResponseDto submitAddressUsingJwt(final String interactionId) throws Exception {
        return performActionWithJwt("submit-address", createValidPayload(interactionId));
    }

    public static InteractionResponseDto submitAddress(final String interactionId) throws Exception {
        return performAction("submit-address", createValidPayload(interactionId));
    }

    public static InteractionResponseDto submitInvalidAddress(final String interactionId) throws Exception {
        final AddressDto validatedAddressDto =
            new AddressDto("my PO BOX", "2A", "10471", "Bronx", "NY", null);

        final InteractionRequestDto request = new InteractionRequestDto();
        request.setInteractionId(interactionId);
        request.setPayload(validatedAddressDto);

        return performAction("submit-address", request);
    }

    private static InteractionRequestDto createValidPayload(String interactionId) {
        final AddressDto validatedAddressDto =
            new AddressDto("12 Liebig Ave", "2A", "10471", "Bronx", "NY", null);

        final InteractionRequestDto request = new InteractionRequestDto();
        request.setInteractionId(interactionId);
        request.setPayload(validatedAddressDto);
        return request;
    }
}
