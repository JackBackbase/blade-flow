package com.backbase.flow.onboarding.us.validation.annotation;

import static com.backbase.flow.onboarding.us.validation.ValidationConstants.EMAIL_NULL_MESSAGE;
import static com.backbase.flow.onboarding.us.validation.ValidationConstants.EMAIL_VALIDATION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;

import com.backbase.buildingblocks.presentation.errors.Error;
import com.backbase.flow.interaction.engine.action.ErrorCodes;
import com.backbase.flow.interaction.engine.action.Errors;
import com.backbase.flow.interaction.engine.action.validation.HandlerPayloadValidator;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Value;
import org.junit.jupiter.api.Test;

class EmailFormatIsValidTest {

    @Test
    void emailValidationWorks() {
        final String emailFormat = EMAIL_VALIDATION_MESSAGE;

        assertEmailIsNotValid(null, EMAIL_NULL_MESSAGE);
        assertEmailIsNotValid("asasas", emailFormat);
        assertEmailIsNotValid("hello@", emailFormat);
        assertEmailIsNotValid(".com", emailFormat);
        assertEmailIsNotValid("aaa@cccc.fake", emailFormat);
    }

    private void assertEmailIsNotValid(final String email, final String... expectedMessages) {
        final EmailDto emailDto = new EmailDto(email);

        final HandlerPayloadValidator<EmailDto> validator = new HandlerPayloadValidator<>();
        final Errors violations = validator.validate(emailDto);

        final List<Error> errors = violations.getErrors();
        assertThat(errors).isNotEmpty();
        assertThat(errors.size()).isEqualTo(expectedMessages.length);
        for (Error error : errors) {
            assertThat(error.getKey()).isEqualTo(ErrorCodes.FLOW_001.getKey());
            assertThat(error.getMessage()).isEqualTo(ErrorCodes.FLOW_001.getMessage());
            assertThat(expectedMessages).contains(error.getContext().get("email"));
        }
    }

    @Value
    static class EmailDto {

        @NotNull(message = EMAIL_NULL_MESSAGE)
        @EmailFormatIsValid(message = EMAIL_VALIDATION_MESSAGE)
        String email;
    }
}
