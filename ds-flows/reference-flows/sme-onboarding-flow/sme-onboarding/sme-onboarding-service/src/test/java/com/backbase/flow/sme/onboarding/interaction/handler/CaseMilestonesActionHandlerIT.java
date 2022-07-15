package com.backbase.flow.sme.onboarding.interaction.handler;

import static com.backbase.flow.sme.onboarding.TestConstants.GET_MILESTONES;
import static com.backbase.flow.sme.onboarding.TestConstants.SME_APPLICATION_CENTER;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backbase.flow.iam.util.WithFlowAnonymousUser;
import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.sme.onboarding.BaseIntegrationIT;
import com.backbase.flow.sme.onboarding.TestConstants;
import com.backbase.flow.sme.onboarding.builder.SmeCaseDefBuilder;
import com.backbase.flow.sme.onboarding.interaction.model.CaseMilestoneRequestDto;
import java.time.LocalDate;
import java.util.UUID;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class CaseMilestonesActionHandlerIT extends BaseIntegrationIT {

    @Test
    @WithFlowAnonymousUser
    void postInteraction_epicExists_shouldReturnListOfMilestones() throws Exception {

        var interactionInstance = createApplicationCenterInteractionInstanceInCaseStore(
            SmeCaseDefBuilder.getInstance().firstName("ab").lastName("bc").email("abc@gmail.com")
                .dateOfBirth(LocalDate.parse("1970-01-01")).soleProp().build(),
            SME_APPLICATION_CENTER, UUID.randomUUID().toString());
        var interactionRequestDto = new InteractionRequestDto();
        var payload = new CaseMilestoneRequestDto();
        payload.setEpic("Main");
        interactionRequestDto.setPayload(payload);
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());

        mockMvc.perform(post(
                TestConstants.ACTION_URL, SME_APPLICATION_CENTER,
                GET_MILESTONES)
                .cookie(new Cookie("anonymousUserId", interactionInstance.getUserId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body.milestones", hasSize(4)))
            .andExpect(jsonPath("$.body.milestones.[0].epic").value("Main"))
            .andExpect(jsonPath("$.body.milestones.[0].name", anyOf(
                equalTo("Application submitted"),
                equalTo("Documents Uploaded"),
                equalTo("Documents reviewed"),
                equalTo("Application completed"))))
            .andExpect(jsonPath("$.body.milestones.[0].context").doesNotExist())
            .andExpect(jsonPath("$.body.milestones.[0].passed").value(false))
            .andExpect(jsonPath("$.body.milestones.[0].createdAt").exists())
            .andExpect(jsonPath("$.body.milestones.[0].modifiedAt").exists())
            .andExpect(jsonPath("$.body.milestones.[1].context").doesNotExist())
            .andExpect(jsonPath("$.body.milestones.[1].passed").value(false))
            .andExpect(jsonPath("$.body.milestones.[1].createdAt").exists())
            .andExpect(jsonPath("$.body.milestones.[1].modifiedAt").exists())
            .andExpect(jsonPath("$.interactionId").isNotEmpty());
    }


    @Test
    @WithFlowAnonymousUser
    void postInteraction_epicDoesNotExist_shouldNotReturnListOfMilestones() throws Exception {

        var interactionInstance = createApplicationCenterInteractionInstanceInCaseStore(
            SmeCaseDefBuilder.getInstance().firstName("ab").lastName("bc").email("abc@gmail.com")
                .dateOfBirth(LocalDate.parse("1970-01-01")).build(),
            SME_APPLICATION_CENTER, UUID.randomUUID().toString());
        var interactionRequestDto = new InteractionRequestDto();
        var payload = new CaseMilestoneRequestDto();
        payload.setEpic("InvalidEpic");
        interactionRequestDto.setPayload(payload);
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());

        mockMvc.perform(post(
                TestConstants.ACTION_URL, SME_APPLICATION_CENTER,
                GET_MILESTONES)
                .cookie(new Cookie("anonymousUserId", interactionInstance.getUserId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body.milestones", hasSize(0)))
            .andExpect(jsonPath("$.interactionId").isNotEmpty());
    }

}
