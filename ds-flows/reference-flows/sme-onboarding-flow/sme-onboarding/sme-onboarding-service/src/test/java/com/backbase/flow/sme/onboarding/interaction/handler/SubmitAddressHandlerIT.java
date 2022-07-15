package com.backbase.flow.sme.onboarding.interaction.handler;

import static com.backbase.flow.sme.onboarding.TestConstants.SUBMIT_ADDRESS;
import static com.backbase.flow.sme.onboarding.TestConstants.ACTION_URL;
import static com.backbase.flow.sme.onboarding.TestConstants.SME_ONBOARDING_PERSONAL_ADDRESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backbase.flow.iam.util.WithFlowAnonymousUser;
import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.sme.onboarding.BaseIntegrationIT;
import com.backbase.flow.sme.onboarding.TestConstants;
import com.backbase.flow.sme.onboarding.builder.AddressRequestBuilder;
import com.backbase.flow.sme.onboarding.builder.SmeCaseDefBuilder;
import java.time.LocalDate;
import java.util.UUID;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class SubmitAddressHandlerIT extends BaseIntegrationIT {

    @Test
    @WithFlowAnonymousUser
    void postPersonalAddressInteraction_withValidData_shouldSave() throws Exception {
        var interactionInstance = createInteractionInstance(
            SmeCaseDefBuilder.getInstance().firstName("test").lastName("test").dateOfBirth(LocalDate.now())
                .email("test@email.invalid").isRegistrar(true).build(),
            SME_ONBOARDING_PERSONAL_ADDRESS,
            UUID.randomUUID().toString());

        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());
        interactionRequestDto.setPayload(AddressRequestBuilder.builder().state("AZ").city("Phoenix")
            .numberAndStreet("17 E Jefferson St").apt("41").zipCode("22222").addressName("personal").build());

        mockMvc.perform(post(
            ACTION_URL, TestConstants.SME_ONBOARDING,
                SUBMIT_ADDRESS)
            .cookie(new Cookie("anonymousUserId", interactionInstance.getUserId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors").isEmpty());

        var smeCase = getCaseData(interactionInstance.getCaseKey());

        assertThat(smeCase).isNotNull();
        assertThat(smeCase.getApplicants().get(0).getPersonalAddress()).isNotNull();
        assertThat(smeCase.getApplicants().get(0).getPersonalAddress()).isNotNull();
        assertThat(smeCase.getApplicants().get(0).getPersonalAddress()).isNotNull();
        assertThat(smeCase.getApplicants().get(0).getPersonalAddress().getState()).isEqualTo("AZ");
        assertThat(smeCase.getApplicants().get(0).getPersonalAddress().getCity()).isEqualTo("Phoenix");
        assertThat(smeCase.getApplicants().get(0).getPersonalAddress().getNumberAndStreet())
            .isEqualTo("17 E Jefferson St");
        assertThat(smeCase.getApplicants().get(0).getPersonalAddress().getApt()).isEqualTo("41");
        assertThat(smeCase.getApplicants().get(0).getPersonalAddress().getZipCode()).isEqualTo("22222");
    }

    @Test
    @WithFlowAnonymousUser
    void postPersonalAddressInteraction_withBlankPayload_shouldReturnActionErrors() throws Exception {
        var interactionInstance = createInteractionInstance(
            SmeCaseDefBuilder.getInstance().soleProp().build(),
            SME_ONBOARDING_PERSONAL_ADDRESS, UUID.randomUUID().toString());

        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());
        interactionRequestDto.setPayload(null);

        mockMvc.perform(post(
            ACTION_URL, TestConstants.SME_ONBOARDING,
                SUBMIT_ADDRESS)
            .cookie(new Cookie("anonymousUserId", interactionInstance.getUserId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print())
            .andExpect(status().is5xxServerError());
    }

    @Test
    @WithFlowAnonymousUser
    void postPersonalAddressInteraction_withUnknownInteraction_shouldReturnNotFound() throws Exception {
        var interactionInstance = createInteractionInstance(
            SmeCaseDefBuilder.getInstance().build(),
            SME_ONBOARDING_PERSONAL_ADDRESS, UUID.randomUUID().toString());

        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(UUID.randomUUID().toString());
        interactionRequestDto.setPayload(null);

        mockMvc.perform(post(
            ACTION_URL, TestConstants.SME_ONBOARDING,
                SUBMIT_ADDRESS)
            .cookie(new Cookie("anonymousUserId", interactionInstance.getUserId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    @WithFlowAnonymousUser
    void postPersonalAddressInteraction_withInvalidPayload_shouldReturnActionErrors() throws Exception {
        var interactionInstance = createInteractionInstance(
            SmeCaseDefBuilder.getInstance().build(),
            SME_ONBOARDING_PERSONAL_ADDRESS, UUID.randomUUID().toString());

        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());
        interactionRequestDto.setPayload(AddressRequestBuilder.builder().addressName("personal").build());

        mockMvc.perform(post(
            ACTION_URL, TestConstants.SME_ONBOARDING,
                SUBMIT_ADDRESS)
            .cookie(new Cookie("anonymousUserId", interactionInstance.getUserId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors", hasSize(4)))
            .andExpect(jsonPath("$.actionErrors[*].context.state")
                .value(contains("must not be blank")))
            .andExpect(jsonPath("$.actionErrors[*].context.city")
                .value(contains("must not be blank")))
            .andExpect(jsonPath("$.actionErrors[*].context.numberAndStreet")
                .value(contains("must not be blank")))
            .andExpect(jsonPath("$.actionErrors[*].context.zipCode")
                .value(contains("must not be blank")));
    }
}
