package com.backbase.flow.onboarding.us.helpers;

import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.interaction.api.model.InteractionResponseDto;
import com.backbase.flow.onboarding.us.BaseIT;
import com.backbase.flow.onboarding.us.interaction.dto.NonResidentDataDto;
import com.backbase.flow.onboarding.us.interaction.dto.ResidencyAddressDto;

public class NonResidentDataStepCalls extends BaseIT {

    public static final String VALID_NATIONAL_TIN = "bbb321321BBB!";
    public static final String VALID_FOREIGN_TIN = "aaa123123AAA!";
    public static final String VALID_CODE_COUNTRY_1 = "CT1";
    public static final String VALID_CODE_COUNTRY_2 = "CT2";
    public static final String VALID_CITY = "Some-City Example";
    public static final String VALID_STREET = "12 Street-somewhere";
    public static final String VALID_ZIP_CODE = "12-Example Zip";

    public static InteractionResponseDto submitNonResidentData(final String interactionId) throws Exception {
        final InteractionRequestDto request = new InteractionRequestDto();
        request.setInteractionId(interactionId);
        request.setPayload(validPayload());

        return performAction("submit-non-resident-data", request);
    }

    private static NonResidentDataDto validPayload() {
        return NonResidentDataDto.builder()
            .foreignTin(VALID_FOREIGN_TIN)
            .nationalTin(VALID_NATIONAL_TIN)
            .citizenshipCountryCode(VALID_CODE_COUNTRY_1)
            .residencyAddress(ResidencyAddressDto.builder()
                .city(VALID_CITY)
                .countryCode(VALID_CODE_COUNTRY_2)
                .numberAndStreet(VALID_STREET)
                .zipCode(VALID_ZIP_CODE)
                .build())
            .w8benAccepted(true)
            .withholdingTaxAccepted(true)
            .build();
    }
}