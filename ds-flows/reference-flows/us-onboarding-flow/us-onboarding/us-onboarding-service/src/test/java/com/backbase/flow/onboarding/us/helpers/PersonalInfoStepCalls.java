package com.backbase.flow.onboarding.us.helpers;

import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.interaction.api.model.InteractionResponseDto;
import com.backbase.flow.onboarding.us.BaseIT;
import com.backbase.flow.onboarding.us.interaction.dto.PersonalInfoDto;

public class PersonalInfoStepCalls extends BaseIT {

    public static final String EMAIL = "potter@backbase.com";
    public static final String SUBMIT_PERSONAL_INFO_ENDPOINT = "submit-personal-info";

    public static InteractionResponseDto submitPersonalInformation(final String firstName,
                                                                   final String dateOfBirth,
                                                                   String interactionId) throws Exception {
        final var personalInfoDto = PersonalInfoDto.builder()
            .firstName(firstName)
            .lastName("Potter")
            .dateOfBirth(dateOfBirth)
            .phoneNumber(OtpVerificationStepCalls.MAIN_APPLICANT_PHONE_NUMBER)
            .email(EMAIL)
            .build();

        final var request = new InteractionRequestDto();
        request.setPayload(personalInfoDto);
        request.setInteractionId(interactionId);

        return performAction(SUBMIT_PERSONAL_INFO_ENDPOINT, request);
    }

}
