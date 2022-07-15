package com.backbase.flow.onboarding.us.interaction.dto;

import static com.backbase.flow.onboarding.us.validation.ValidationConstants.EMAIL_NULL_MESSAGE;
import static com.backbase.flow.onboarding.us.validation.ValidationConstants.EMAIL_VALIDATION_MESSAGE;
import static com.backbase.flow.onboarding.us.validation.ValidationConstants.FIELD_LENGTH_MESSAGE;

import com.backbase.flow.onboarding.us.validation.annotation.CheckDateFormat;
import com.backbase.flow.onboarding.us.validation.annotation.EmailFormatIsValid;
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
public class AnchorDataDto {

    @NotBlank
    @Size(max = 50, message = FIELD_LENGTH_MESSAGE)
    private String firstName;

    @NotBlank
    @Size(max = 50, message = FIELD_LENGTH_MESSAGE)
    private String lastName;

    @NotBlank
    @CheckDateFormat
    private String dateOfBirth;

    @NotNull(message = EMAIL_NULL_MESSAGE)
    @EmailFormatIsValid(message = EMAIL_VALIDATION_MESSAGE)
    private String email;
    
}
