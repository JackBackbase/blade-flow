package com.backbase.flow.onboarding.us.interaction.dto;

import static com.backbase.flow.onboarding.us.validation.ValidationConstants.SSN_REGEX_PATTERN;
import static com.backbase.flow.onboarding.us.validation.ValidationConstants.SSN_VALIDATION_MESSAGE;

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
public class SsnDto {

    @NotNull(message = SSN_VALIDATION_MESSAGE)
    @Pattern(regexp = SSN_REGEX_PATTERN, message = SSN_VALIDATION_MESSAGE)
    private String ssn;

}