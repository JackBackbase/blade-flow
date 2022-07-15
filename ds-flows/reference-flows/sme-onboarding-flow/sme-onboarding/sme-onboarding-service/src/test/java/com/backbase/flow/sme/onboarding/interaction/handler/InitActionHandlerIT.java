package com.backbase.flow.sme.onboarding.interaction.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backbase.flow.iam.util.WithFlowAnonymousUser;
import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.sme.onboarding.BaseIntegrationIT;
import com.backbase.flow.sme.onboarding.TestConstants;
import com.backbase.flow.sme.onboarding.builder.SmeCaseDefBuilder;
import com.backbase.flow.sme.onboarding.builder.TermsConditionsRequestBuilder;
import com.backbase.flow.sme.onboarding.event.TermsAndConditionsApprovedEvent;
import java.time.LocalDate;
import java.util.UUID;
import javax.servlet.http.Cookie;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class InitActionHandlerIT extends BaseIntegrationIT {

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withValidData_shouldCreateCase() throws Exception {
        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setPayload(TermsConditionsRequestBuilder.builder().accepted(true)
            .build());

        mockMvc.perform(post(
            TestConstants.ACTION_URL, TestConstants.SME_ONBOARDING,
            TestConstants.SME_ONBOARDING_INIT)
            .cookie(new Cookie("anonymousUserId", UUID.randomUUID().toString()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.step.name", is("sme-onboarding-anchor")))
            .andExpect(jsonPath("$.body.anchorDataReceived", is(false)))
            .andExpect(jsonPath("$.actionErrors").isEmpty());
    }

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withValidData_shouldCreateCase_accepted_false() throws Exception {
        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setPayload(TermsConditionsRequestBuilder.builder().accepted(false)
            .build());

        mockMvc.perform(post(
            TestConstants.ACTION_URL, TestConstants.SME_ONBOARDING,
            TestConstants.SME_ONBOARDING_INIT)
            .cookie(new Cookie("anonymousUserId", UUID.randomUUID().toString()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.step.name", is("sme-onboarding-walkthrough")))
            .andExpect(jsonPath("$.actionErrors").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors[0].key", Matchers.is("FLOW-001")))
            .andExpect(jsonPath("$.actionErrors[0].message", Matchers.is("Invalid request body.")));
    }

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withValidData_shouldSaveTermsConditions() throws Exception {
        var interactionInstance = createInteractionInstance(
            SmeCaseDefBuilder.getInstance().build(),
            "sme-onboarding-walkthrough",
            UUID.randomUUID().toString());

        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());
        interactionRequestDto.setPayload(TermsConditionsRequestBuilder.builder().accepted(true).build());

        mockMvc.perform(post(
            TestConstants.ACTION_URL, TestConstants.SME_ONBOARDING,
            TestConstants.SME_ONBOARDING_INIT)
            .cookie(new Cookie("anonymousUserId", interactionInstance.getUserId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors").isEmpty())
            .andExpect(jsonPath("$.step.name", is("sme-onboarding-anchor")))
            .andExpect(jsonPath("$.body.anchorDataReceived", is(false)))
            .andExpect(jsonPath("$.body.accepted", is(true)));

        var smeCase = getCaseData(interactionInstance.getCaseKey());
        var events = getEventsByClass(interactionInstance.getCaseKey(),
            TermsAndConditionsApprovedEvent.class);

        assertThat(smeCase).isNotNull();
        assertThat(smeCase.getCompanyLookupInfo()).isNotNull();
        assertThat(smeCase.getCompanyLookupInfo().getBusinessDetailsInfo()).isNotNull();
        assertThat(smeCase.getCompanyLookupInfo().getBusinessStructureInfo()).isNotNull();
        assertThat(smeCase.getTermsAndConditions()).isNotNull();
        assertTrue(smeCase.getTermsAndConditions().getAccepted());

        assertThat(events).isNotEmpty();
        assertThat(events.get(0).getEventDescription()).isEqualTo("Term and conditions are accepted");
    }


    @Test
    @WithFlowAnonymousUser
    void postInteraction_withNullPayload_shouldReturnInvalidDataError() throws Exception {
        var interactionInstance = createInteractionInstance(
            SmeCaseDefBuilder.getInstance().soleProp().build(),
            "sme-onboarding-walkthrough",
            UUID.randomUUID().toString());

        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());
        interactionRequestDto.setPayload(null);

        mockMvc.perform(post(
            TestConstants.ACTION_URL, TestConstants.SME_ONBOARDING,
            TestConstants.SME_ONBOARDING_INIT)
            .cookie(new Cookie("anonymousUserId", interactionInstance.getUserId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors[0].message", is("Input payload is invalid.")));
    }

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withRedundantRequest_shouldReturnCase() throws Exception {
        var interactionInstance = createInteractionInstance(
            SmeCaseDefBuilder.getInstance().firstName("b").lastName("b").dateOfBirth(LocalDate.now())
                .email("test@bb.com").build(),
            "sme-onboarding-walkthrough",
            UUID.randomUUID().toString());

        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());
        interactionRequestDto.setPayload(TermsConditionsRequestBuilder.builder().accepted(true)
            .build());

        mockMvc.perform(post(
            TestConstants.ACTION_URL, TestConstants.SME_ONBOARDING,
            TestConstants.SME_ONBOARDING_INIT)
            .cookie(new Cookie("anonymousUserId", interactionInstance.getUserId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.step.name", is("otp-verification")))
            .andExpect(jsonPath("$.body.anchorDataReceived", is(true)))
            .andExpect(jsonPath("$.body.accepted", is(true)))
            .andExpect(jsonPath("$.body.acceptanceDate").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors").isEmpty());
    }
}
