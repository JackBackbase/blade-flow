package com.backbase.flow.onboarding.us.interaction.dto;

import static com.backbase.flow.onboarding.us.validation.ValidationConstants.FIELD_LENGTH_MESSAGE;
import static com.backbase.flow.onboarding.us.validation.ValidationConstants.RESIDENCY_CITY_BLANK_MESSAGE;
import static com.backbase.flow.onboarding.us.validation.ValidationConstants.RESIDENCY_COUNTRY_CODE_BLANK_MESSAGE;
import static com.backbase.flow.onboarding.us.validation.ValidationConstants.RESIDENCY_NUMBER_AND_STREET_BLANK_MESSAGE;
import static com.backbase.flow.onboarding.us.validation.ValidationConstants.RESIDENCY_ZIP_CODE_BLANK_MESSAGE;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResidencyAddressDto {

    @NotBlank(message = RESIDENCY_COUNTRY_CODE_BLANK_MESSAGE)
    private String countryCode;

    @NotBlank(message = RESIDENCY_NUMBER_AND_STREET_BLANK_MESSAGE)
    @Size(max = 100, message = FIELD_LENGTH_MESSAGE)
    private String numberAndStreet;

    @NotBlank(message = RESIDENCY_CITY_BLANK_MESSAGE)
    @Size(max = 100, message = FIELD_LENGTH_MESSAGE)
    private String city;

    @NotBlank(message = RESIDENCY_ZIP_CODE_BLANK_MESSAGE)
    @Size(max = 100, message = FIELD_LENGTH_MESSAGE)
    private String zipCode;
}
