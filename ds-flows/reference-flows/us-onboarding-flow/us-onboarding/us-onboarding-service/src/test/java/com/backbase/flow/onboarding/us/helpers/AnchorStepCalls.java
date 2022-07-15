package com.backbase.flow.onboarding.us.helpers;

import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.interaction.api.model.InteractionResponseDto;
import com.backbase.flow.onboarding.us.BaseIT;
import com.backbase.flow.onboarding.us.interaction.dto.AnchorDataDto;
import com.backbase.flow.onboarding.us.interaction.dto.CoApplicantDataRequestDto;
import java.util.UUID;

public class AnchorStepCalls extends BaseIT {

    public static final String ANCHOR_STEP_EMAIL = "potter@backbase.com";
    public static final String ANCHOR_STEP_EMAIL_CO_APPLICANT = "jane.potter@backbase.com";
    public static final String SUBMIT_ANCHOR_DATA_ENDPOINT = "submit-anchor-data";
    public static final String PREFILL_ANCHOR_DATA_ENDPOINT = "prefill-anchor-data";

    public static InteractionResponseDto prefillAnchorData(final UUID coApplicantId, String interactionId)
        throws Exception {
        var coApplicantData = new CoApplicantDataRequestDto(coApplicantId);

        final var request = new InteractionRequestDto();
        request.setPayload(coApplicantData);
        request.setInteractionId(interactionId);

        return performAction(PREFILL_ANCHOR_DATA_ENDPOINT, request);
    }

    public static InteractionResponseDto submitAnchorData(final String firstName, final String dateOfBirth,
        final String interactionId) throws Exception {
        final var anchorDataDto = AnchorDataDto.builder()
            .firstName(firstName)
            .lastName("Potter")
            .dateOfBirth(dateOfBirth)
            .email(ANCHOR_STEP_EMAIL)
            .build();

        final var request = new InteractionRequestDto();
        request.setPayload(anchorDataDto);
        request.setInteractionId(interactionId);

        return performAction(SUBMIT_ANCHOR_DATA_ENDPOINT, request);
    }

    public static InteractionResponseDto submitCoApplicantAnchorData(final String firstName,
        final String dateOfBirth, final String interactionId) throws Exception {

        final var request = createValidPayloadForCoApplicant(firstName, dateOfBirth, interactionId);

        return performAction(SUBMIT_ANCHOR_DATA_ENDPOINT, request);
    }

    public static InteractionResponseDto submitCoApplicantAnchorDataUsingJwt(final String firstName,
        final String dateOfBirth, final String interactionId) throws Exception {

        final var request = createValidPayloadForCoApplicant(firstName, dateOfBirth, interactionId);

        return performActionWithJwt(SUBMIT_ANCHOR_DATA_ENDPOINT, request);
    }

    private static InteractionRequestDto createValidPayloadForCoApplicant(final String firstName,
        final String dateOfBirth, final String interactionId) {
        final AnchorDataDto anchorDataDto = AnchorDataDto.builder()
            .firstName(firstName)
            .lastName("Potter")
            .dateOfBirth(dateOfBirth)
            .email(ANCHOR_STEP_EMAIL_CO_APPLICANT)
            .build();

        final InteractionRequestDto request = new InteractionRequestDto();
        request.setInteractionId(interactionId);
        request.setPayload(anchorDataDto);
        return request;
    }
}
