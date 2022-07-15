package com.backbase.flow.onboarding.us.interaction.dto;

import static com.backbase.flow.onboarding.us.validation.ValidationConstants.CITIZENSHIP_COUNTRY_CODE_NULL_MESSAGE;
import static com.backbase.flow.onboarding.us.validation.ValidationConstants.FIELD_LENGTH_MESSAGE;
import static com.backbase.flow.onboarding.us.validation.ValidationConstants.RESIDENCY_ADDRESS_NULL_MESSAGE;
import static com.backbase.flow.onboarding.us.validation.ValidationConstants.W8BEN_ACCEPTED_NULL_MESSAGE;
import static com.backbase.flow.onboarding.us.validation.ValidationConstants.WITHHOLDING_TAX_ACCEPTED_NULL_MESSAGE;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NonResidentDataDto {

    @NotNull(message = CITIZENSHIP_COUNTRY_CODE_NULL_MESSAGE)
    private String citizenshipCountryCode;

    @Valid
    @NotNull(message = RESIDENCY_ADDRESS_NULL_MESSAGE)
    private ResidencyAddressDto residencyAddress;

    @Size(max = 50, message = FIELD_LENGTH_MESSAGE)
    private String nationalTin;

    @Size(max = 100, message = FIELD_LENGTH_MESSAGE)
    private String foreignTin;

    @NotNull(message = WITHHOLDING_TAX_ACCEPTED_NULL_MESSAGE)
    private Boolean withholdingTaxAccepted;

    @NotNull(message = W8BEN_ACCEPTED_NULL_MESSAGE)
    @AssertTrue
    private Boolean w8benAccepted;

}
