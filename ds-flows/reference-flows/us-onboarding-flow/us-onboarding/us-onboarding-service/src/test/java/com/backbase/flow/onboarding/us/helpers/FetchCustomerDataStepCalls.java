package com.backbase.flow.onboarding.us.helpers;

import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.interaction.api.model.InteractionResponseDto;
import com.backbase.flow.onboarding.us.BaseIT;
import com.backbase.flow.onboarding.us.interaction.dto.CoApplicantDataRequestDto;
import java.util.UUID;

public class FetchCustomerDataStepCalls extends BaseIT {

    public static InteractionResponseDto fetchCustomerData(UUID coApplicant) throws Exception {
        return performActionWithJwt("fetch-customer-data", createValidPayload(coApplicant));
    }

    private static InteractionRequestDto createValidPayload(UUID coApplicantId) {
        final CoApplicantDataRequestDto fetchCoApplicantDataRequestDto =
            new CoApplicantDataRequestDto(coApplicantId);

        final InteractionRequestDto request = new InteractionRequestDto();
        request.setPayload(fetchCoApplicantDataRequestDto);
        return request;
    }
}
