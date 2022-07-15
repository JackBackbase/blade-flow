package com.backbase.flow.sme.onboarding.interaction.handler;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backbase.flow.iam.util.WithFlowAnonymousUser;
import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.sme.onboarding.BaseIntegrationIT;
import com.backbase.flow.sme.onboarding.TestConstants;
import com.backbase.flow.sme.onboarding.builder.SmeCaseDefBuilder;
import java.time.LocalDate;
import java.util.UUID;
import javax.servlet.http.Cookie;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class FetchOtpEmailActionHandlerIT extends BaseIntegrationIT {

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withValidRequest_shouldReturnEmail() throws Exception {
        var sampleEmail = "registrar@email.invalid";

        var interactionInstance = createInteractionInstance(SmeCaseDefBuilder.getInstance()
                .firstName("Boy").lastName("Sena").dateOfBirth(LocalDate.parse("1970-01-01"))
                .email(sampleEmail).isRegistrar(true).build(),
            "otp-verification",
            UUID.randomUUID().toString());

        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());

        mockMvc.perform(post(
            TestConstants.ACTION_URL, TestConstants.SME_ONBOARDING,
            TestConstants.FETCH_OTP_EMAIL)
            .cookie(new Cookie("anonymousUserId", interactionInstance.getUserId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors").isEmpty())
            .andExpect(jsonPath("$.step.name", Matchers.is("otp-verification")))
            .andExpect(jsonPath("$.body.email", Matchers.is(sampleEmail)));
    }

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withInvalidRegistrar_shouldReturnError() throws Exception {
        var sampleEmail = "registrar@email.invalid";

        var interactionInstance = createInteractionInstance(SmeCaseDefBuilder.getInstance()
                .firstName("Boy").lastName("Sena").dateOfBirth(LocalDate.parse("1970-01-01"))
                .email(sampleEmail).build(),
            "otp-verification",
            UUID.randomUUID().toString());

        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());

        mockMvc.perform(post(
            TestConstants.ACTION_URL, TestConstants.SME_ONBOARDING,
            TestConstants.FETCH_OTP_EMAIL)
            .cookie(new Cookie("anonymousUserId", interactionInstance.getUserId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors[0].key", Matchers.is("SME_004")))
            .andExpect(jsonPath("$.actionErrors[0].message", Matchers.is("Registrar is invalid.")));
    }
}
