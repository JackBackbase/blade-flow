package com.backbase.flow.sme.onboarding.interaction.handler;

import static com.backbase.flow.sme.onboarding.TestConstants.TRANSPORTATION_AND_WAREHOUSING_GENERAL;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backbase.flow.casedata.CaseNotFoundException;
import com.backbase.flow.iam.util.WithFlowAnonymousUser;
import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.sme.onboarding.BaseIntegrationIT;
import com.backbase.flow.sme.onboarding.TestConstants;
import com.backbase.flow.sme.onboarding.builder.ApplicationCenterRequestDtoBuilder;
import com.backbase.flow.sme.onboarding.builder.SmeCaseDefBuilder;
import java.util.UUID;
import javax.servlet.http.Cookie;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class ApplicationCenterInitActionHandlerIT extends BaseIntegrationIT {

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withValidData_shouldBindCaseToInteraction() throws Exception {
        var exCase = startCase(SmeCaseDefBuilder.getInstance()
            .businessKnownName("Test").businessOperationState("TX")
            .businessIndustry(TRANSPORTATION_AND_WAREHOUSING_GENERAL).build());

        caseDataService.updateCase(exCase);

        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setPayload(ApplicationCenterRequestDtoBuilder.getInstance().id(exCase.getKey().toString())
            .build());

        mockMvc.perform(post(
            TestConstants.ACTION_URL, TestConstants.SME_APPLICATION_CENTER,
            TestConstants.APPLICATION_CENTER_INIT)
            .cookie(new Cookie("anonymousUserId", UUID.randomUUID().toString()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.step.name", is("otp-verification")))
            .andExpect(jsonPath("$.actionErrors").isEmpty());
    }


    @Test
    @WithFlowAnonymousUser
    void postInteraction_withNoCase_shouldNotBindCaseToInteraction() throws Exception {

        var interactionRequestDto = new InteractionRequestDto();
        var randomCaseId = UUID.randomUUID().toString();
        interactionRequestDto.setPayload(ApplicationCenterRequestDtoBuilder.getInstance().id(randomCaseId)
            .build());

        mockMvc.perform(post(
            TestConstants.ACTION_URL, TestConstants.SME_APPLICATION_CENTER,
            TestConstants.APPLICATION_CENTER_INIT)
            .cookie(new Cookie("anonymousUserId", UUID.randomUUID().toString()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof CaseNotFoundException))
            .andExpect(result -> assertEquals("Could not find case with key " + randomCaseId,
                result.getResolvedException().getMessage()));

    }

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withNullPayload_shouldNotBindCaseToInteraction() throws Exception {

        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setPayload(null);

        mockMvc.perform(post(
            TestConstants.ACTION_URL, TestConstants.SME_APPLICATION_CENTER,
            TestConstants.APPLICATION_CENTER_INIT)
            .cookie(new Cookie("anonymousUserId", UUID.randomUUID().toString()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.step.name", is("application-center-init")))
            .andExpect(jsonPath("$.actionErrors").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors[0].key", Matchers.is("SME_001")))
            .andExpect(jsonPath("$.actionErrors[0].message", Matchers.is("Input payload is invalid.")));

    }
}
