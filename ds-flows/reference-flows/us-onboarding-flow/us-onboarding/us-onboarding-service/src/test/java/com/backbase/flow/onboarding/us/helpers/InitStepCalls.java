package com.backbase.flow.onboarding.us.helpers;

import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.interaction.api.model.InteractionResponseDto;
import com.backbase.flow.onboarding.us.BaseIT;
import com.backbase.flow.onboarding.us.interaction.dto.CoApplicantDataRequestDto;
import java.util.UUID;

public class InitStepCalls extends BaseIT {

    private static final String FETCH_CO_APPLICANT_ENDPOINT = "fetch-co-applicant";
    private static final String FETCH_CO_APPLICANT_DATA_ENDPOINT = "fetch-co-applicant-data";

    public static InteractionResponseDto fetchCoApplicantData(String coApplicantId) throws Exception {
        final var fetchCoApplicantDataRequestDto = new CoApplicantDataRequestDto(UUID.fromString(coApplicantId));
        final var request = new InteractionRequestDto();
        request.setPayload(fetchCoApplicantDataRequestDto);

        return performAction(FETCH_CO_APPLICANT_DATA_ENDPOINT, request);
    }

    public static InteractionResponseDto fetchCoApplicant(UUID coApplicantId) throws Exception {
        final var coApplicantData = new CoApplicantDataRequestDto(coApplicantId);
        final var request = new InteractionRequestDto();
        request.setPayload(coApplicantData);

        return performAction(FETCH_CO_APPLICANT_ENDPOINT, request);
    }

}
