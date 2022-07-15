package com.backbase.flow.sme.onboarding.interaction.handler;

import static com.backbase.flow.sme.onboarding.TestConstants.FINANCE_AND_INSURANCE;
import static com.backbase.flow.sme.onboarding.TestConstants.MINING_QUARRYING_OIL_AND_GAS_EXTRACTION;
import static org.assertj.core.api.Assertions.assertThat;
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
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class CheckBusinessRelationAndDocumentRequestConditionsActionHandlerIT extends BaseIntegrationIT {

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withSoleProprietorship_shouldSkipBusinessRelation() throws Exception {
        var interactionInstance = createInteractionInstance(
            SmeCaseDefBuilder.getInstance()
                .firstName("John")
                .lastName("Doe")
                .email("john@email.invalid")
                .isRegistrar(true)
                .businessLegalName("legal")
                .businessKnownName("potential_match")
                .businessOperationState("AR")
                .businessIndustry(MINING_QUARRYING_OIL_AND_GAS_EXTRACTION)
                .soleProp().build(),
            "check-business-relation-and-document-request",
            UUID.randomUUID().toString());

        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());

        mockMvc.perform(post(
            TestConstants.ACTION_URL, TestConstants.SME_ONBOARDING,
            TestConstants.CHECK_BUSINESS_RELATION_AND_DOCUMENT_REQUESTS_CONDITIONS)
            .cookie(new Cookie("anonymousUserId", interactionInstance.getUserId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors").isEmpty())
            .andExpect(jsonPath("$.step.name", Matchers.is("document-request-journey")))
            .andExpect(jsonPath("$.body.documentRequired").isBoolean())
            .andExpect(jsonPath("$.body.documentRequired", Matchers.is(true)));

        var smeCase = getCaseData(interactionInstance.getCaseKey());

        assertThat(smeCase).isNotNull();
        assertThat(smeCase.getBusinessRelations()).isNull();
        assertThat(smeCase.getBusinessRelationRequired()).isFalse();
    }

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withSoleProprietorship_shouldSkipBusinessRelationAndDocumentRequest() throws Exception {
        var interactionInstance = createInteractionInstance(
            SmeCaseDefBuilder.getInstance()
                .firstName("John")
                .lastName("Doe")
                .email("john@email.invalid")
                .isRegistrar(true)
                .businessLegalName("legal")
                .businessOperationState("AR")
                .businessIndustry(FINANCE_AND_INSURANCE)
                .soleProp().build(),
            "check-business-relation-and-document-request",
            UUID.randomUUID().toString());

        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());

        mockMvc.perform(post(
            TestConstants.ACTION_URL, TestConstants.SME_ONBOARDING,
            TestConstants.CHECK_BUSINESS_RELATION_AND_DOCUMENT_REQUESTS_CONDITIONS)
            .cookie(new Cookie("anonymousUserId", interactionInstance.getUserId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors").isEmpty())
            .andExpect(jsonPath("$.step.name", Matchers.is("identity-verification-initiation")))
            .andExpect(jsonPath("$.body.documentRequired").isBoolean())
            .andExpect(jsonPath("$.body.documentRequired", Matchers.is(false)));

        var smeCase = getCaseData(interactionInstance.getCaseKey());

        assertThat(smeCase).isNotNull();
        assertThat(smeCase.getBusinessRelations()).isNull();
        assertThat(smeCase.getBusinessRelationRequired()).isFalse();
    }

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withPartnership_shouldRunBusinessRelation() throws Exception {
        var interactionInstance = createInteractionInstance(
            SmeCaseDefBuilder.getInstance()
                .firstName("John")
                .lastName("Doe")
                .email("john@email.invalid")
                .isRegistrar(true)
                .businessLegalName("legal")
                .businessKnownName("potential_match")
                .businessOperationState("AR")
                .businessIndustry(MINING_QUARRYING_OIL_AND_GAS_EXTRACTION)
                .partnership()
                .jointVentureSubtype()
                .build(),
            "check-business-relation-and-document-request",
            UUID.randomUUID().toString());

        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());

        mockMvc.perform(post(
            TestConstants.ACTION_URL, TestConstants.SME_ONBOARDING,
            TestConstants.CHECK_BUSINESS_RELATION_AND_DOCUMENT_REQUESTS_CONDITIONS)
            .cookie(new Cookie("anonymousUserId", interactionInstance.getUserId()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors").isEmpty())
            .andExpect(jsonPath("$.step.name", Matchers.is("relation-type")));
    }
}
