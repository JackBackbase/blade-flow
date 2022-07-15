package com.backbase.flow.onboarding.us.helpers;

import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.interaction.api.model.InteractionResponseDto;
import com.backbase.flow.onboarding.us.BaseIT;
import com.backbase.flow.service.identityverification.casedata.IdentityVerificationResult;
import java.util.UUID;

public class IdentityVerificationStepCalls extends BaseIT {

    public static InteractionResponseDto verificationType(final String interactionId) throws Exception {
        final InteractionRequestDto request = new InteractionRequestDto();
        request.setInteractionId(interactionId);
        request.setPayload(null);

        return performAction("verification-type", request);
    }

    public static InteractionResponseDto identityInitiation(final String interactionId) throws Exception {
        final InteractionRequestDto request = new InteractionRequestDto();
        request.setInteractionId(interactionId);
        request.setPayload(null);

        return performAction("identity-verification-initiation", request);
    }

    public static InteractionResponseDto identityResult(final String interactionId, String mockedResult)
        throws Exception {
        final IdentityVerificationResult identityVerificationResult = new IdentityVerificationResult();
        identityVerificationResult.setVerificationId(isIDVMocked ? mockedResult : UUID.randomUUID().toString());

        final InteractionRequestDto request = new InteractionRequestDto();
        request.setInteractionId(interactionId);
        request.setPayload(identityVerificationResult);

        return performAction("identity-verification-result", request);
    }
}