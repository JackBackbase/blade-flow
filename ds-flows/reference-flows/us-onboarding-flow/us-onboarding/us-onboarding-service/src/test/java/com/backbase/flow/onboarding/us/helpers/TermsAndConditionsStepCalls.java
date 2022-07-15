package com.backbase.flow.onboarding.us.helpers;

import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.interaction.api.model.InteractionResponseDto;
import com.backbase.flow.onboarding.us.BaseIT;
import com.backbase.flow.onboarding.us.interaction.dto.PoliciesAgreementDto;

public class TermsAndConditionsStepCalls extends BaseIT {


    public static InteractionResponseDto agreeToTermsUsingJwt(final String interactionId)
        throws Exception {
        return performActionWithJwt("agree-to-terms", createPayload(interactionId));
    }

    public static InteractionResponseDto agreeToTerms() throws Exception {
        return performAction("agree-to-terms", createPayload());
    }

    public static InteractionResponseDto agreeToTerms(final String interactionId) throws Exception {
        return performAction("agree-to-terms", createPayload(interactionId));
    }

    private static InteractionRequestDto createPayload() {
        return createPayload(null);
    }

    private static InteractionRequestDto createPayload(String interactionId) {
        final PoliciesAgreementDto policiesAgreementDto = new PoliciesAgreementDto(true);

        final InteractionRequestDto request = new InteractionRequestDto();
        request.setInteractionId(interactionId);
        request.setPayload(policiesAgreementDto);
        return request;
    }
}
