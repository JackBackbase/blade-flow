package com.backbase.flow.onboarding.us.interaction.dto;

import static com.backbase.flow.onboarding.us.validation.ValidationConstants.PSW_LENGTH_8_MESSAGE;
import static com.backbase.flow.onboarding.us.validation.ValidationConstants.PSW_NULL_MESSAGE;
import static com.backbase.flow.onboarding.us.validation.ValidationConstants.PSW_ONE_DIGIT_MESSAGE;
import static com.backbase.flow.onboarding.us.validation.ValidationConstants.PSW_ONE_UPPERCASE_MESSAGE;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CredentialsDto {

    @NotNull(message = PSW_NULL_MESSAGE)
    @Pattern(regexp = ".{8,}", message = PSW_LENGTH_8_MESSAGE)
    @Pattern(regexp = ".*[0-9].*", message = PSW_ONE_DIGIT_MESSAGE)
    @Pattern(regexp = ".*[A-Z].*", message = PSW_ONE_UPPERCASE_MESSAGE)
    private String password;
}
