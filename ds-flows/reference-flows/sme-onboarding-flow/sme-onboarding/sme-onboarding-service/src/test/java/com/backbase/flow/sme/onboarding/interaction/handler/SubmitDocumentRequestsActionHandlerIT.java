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
import java.util.UUID;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class SubmitDocumentRequestsActionHandlerIT extends BaseIntegrationIT {

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withValidRequest_returnSuccess() throws Exception {

        var interactionInstance = createInteractionInstanceInCaseStore(
            SmeCaseDefBuilder.getInstance().build(),
            TestConstants.DOCUMENT_REQUEST_JOURNEY,
            UUID.randomUUID().toString());

        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());

        mockMvc.perform(post(
            TestConstants.ACTION_URL, TestConstants.SME_ONBOARDING,
            TestConstants.SUBMIT_DOCUMENT_REQUESTS)
            .cookie(new Cookie("anonymousUserId", interactionInstance.getUserId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors").isEmpty());
    }

}
