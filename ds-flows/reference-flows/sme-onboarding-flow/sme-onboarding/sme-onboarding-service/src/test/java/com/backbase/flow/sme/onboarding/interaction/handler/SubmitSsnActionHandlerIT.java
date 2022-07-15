package com.backbase.flow.sme.onboarding.interaction.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backbase.flow.iam.util.WithFlowAnonymousUser;
import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.interaction.engine.data.model.InteractionInstance;
import com.backbase.flow.sme.onboarding.BaseIntegrationIT;
import com.backbase.flow.sme.onboarding.TestConstants;
import com.backbase.flow.sme.onboarding.builder.SmeCaseDefBuilder;
import com.backbase.flow.sme.onboarding.interaction.model.SsnDto;
import java.time.LocalDate;
import java.util.UUID;
import javax.servlet.http.Cookie;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class SubmitSsnActionHandlerIT extends BaseIntegrationIT {

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withValidData_shouldSave() throws Exception {
        var interactionInstance = getInteractionInstance();

        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());
        var ssnDto = new SsnDto("123456789");
        interactionRequestDto.setPayload(ssnDto);

        getMockMvcResults(interactionInstance, interactionRequestDto)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors").isEmpty());

        var smeCase = getCaseData(interactionInstance.getCaseKey());

        assertThat(smeCase).isNotNull();
        assertThat(smeCase.getApplicants().get(0)).isNotNull();
        assertThat(smeCase.getApplicants().get(0).getSsn()).isEqualTo("123456789");
    }

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withInvalidRegistrar_shouldReturnError() throws Exception {
        var interactionInstance = createInteractionInstance(
            SmeCaseDefBuilder.getInstance().firstName("b").lastName("b").dateOfBirth(LocalDate.now())
                .email("test@bb.com").build(),
            TestConstants.SME_ONBOARDING_SSN, UUID.randomUUID().toString());

        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());
        var ssnDto = new SsnDto("123456789");
        interactionRequestDto.setPayload(ssnDto);

        getMockMvcResults(interactionInstance, interactionRequestDto)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors[0].key", Matchers.is("SME_004")))
            .andExpect(jsonPath("$.actionErrors[0].message", Matchers.is("Registrar is invalid.")));
    }

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withUnknownInteraction_shouldReturnNotFound() throws Exception {
        var interactionInstance = getInteractionInstance();

        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(UUID.randomUUID().toString());
        interactionRequestDto.setPayload(null);

        getMockMvcResults(interactionInstance, interactionRequestDto)
            .andExpect(status().isNotFound());
    }

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withBlankPayload_shouldReturnActionErrors() throws Exception {
        var interactionInstance = getInteractionInstance();

        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());
        interactionRequestDto.setPayload(null);

        getMockMvcResults(interactionInstance, interactionRequestDto)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors[0].message", Matchers.is("Input payload is invalid.")));
    }

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withInvalidPayload_shouldReturnActionErrors() throws Exception {
        var interactionInstance = getInteractionInstance();

        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());
        var ssnDto = new SsnDto("123456");
        interactionRequestDto.setPayload(ssnDto);

        getMockMvcResults(interactionInstance, interactionRequestDto)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors", hasSize(1)))
            .andExpect(jsonPath("$.actionErrors[*].context.ssn")
                .value(contains("Please enter a valid social security number.")));
    }

    private InteractionInstance getInteractionInstance() {
        return createInteractionInstance(
            SmeCaseDefBuilder.getInstance().firstName("b").lastName("b").dateOfBirth(LocalDate.now())
                .email("test@bb.com").isRegistrar(true).build(),
            TestConstants.SME_ONBOARDING_SSN, UUID.randomUUID().toString());
    }

    private ResultActions getMockMvcResults(InteractionInstance interactionInstance,
                                            InteractionRequestDto interactionRequestDto) throws Exception {
        return mockMvc.perform(
            post(TestConstants.ACTION_URL, TestConstants.SME_ONBOARDING, TestConstants.SUBMIT_SSN)
                .cookie(new Cookie("anonymousUserId", interactionInstance.getUserId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print());
    }
}
