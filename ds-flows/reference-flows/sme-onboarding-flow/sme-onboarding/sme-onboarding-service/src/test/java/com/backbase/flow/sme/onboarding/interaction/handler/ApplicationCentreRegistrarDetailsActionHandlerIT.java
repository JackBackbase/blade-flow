package com.backbase.flow.sme.onboarding.interaction.handler;

import static com.backbase.flow.sme.onboarding.TestConstants.SEND_REGISTRAR_DETAILS;
import static com.backbase.flow.sme.onboarding.TestConstants.SME_APPLICATION_CENTER;
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

class ApplicationCentreRegistrarDetailsActionHandlerIT extends BaseIntegrationIT {

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withValidData_returnFullName() throws Exception {

        var caseDefinition = SmeCaseDefBuilder.getInstance().firstName("ab").lastName("bc")
            .email("abc@gmail.com").dateOfBirth(LocalDate.parse("1970-01-01")).isRegistrar(true).soleProp().build();
        var interactionInstance = createApplicationCenterInteractionInstanceInCaseStore(
            caseDefinition, SME_APPLICATION_CENTER, UUID.randomUUID().toString());
        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());

        mockMvc.perform(post(
                TestConstants.ACTION_URL, SME_APPLICATION_CENTER,
                SEND_REGISTRAR_DETAILS)
                .cookie(new Cookie("anonymousUserId", interactionInstance.getUserId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.body.fullName", Matchers.is("ab bc")));
    }

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withEmptyRegistrar_returnError() throws Exception {

        var caseDefinition = SmeCaseDefBuilder.getInstance().firstName("ab").lastName("bc")
            .email("abc@gmail.com").dateOfBirth(LocalDate.parse("1970-01-01")).soleProp().build();
        var interactionInstance = createApplicationCenterInteractionInstanceInCaseStore(
            caseDefinition, SME_APPLICATION_CENTER, UUID.randomUUID().toString());
        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());

        mockMvc.perform(post(
                TestConstants.ACTION_URL, SME_APPLICATION_CENTER,
                SEND_REGISTRAR_DETAILS)
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
