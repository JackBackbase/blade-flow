package com.backbase.flow.sme.onboarding.interaction.handler;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backbase.flow.iam.util.WithFlowAnonymousUser;
import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.interaction.engine.data.model.InteractionInstance;
import com.backbase.flow.sme.onboarding.BaseIntegrationIT;
import com.backbase.flow.sme.onboarding.TestConstants;
import com.backbase.flow.sme.onboarding.builder.ProductListRequestBuilder;
import com.backbase.flow.sme.onboarding.builder.SmeCaseDefBuilder;
import java.util.UUID;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class ProductListActionHandlerIT extends BaseIntegrationIT {

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withValidData_shouldSave() throws Exception {
        var interactionInstance = getInteractionInstance();

        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());
        interactionRequestDto.setPayload(ProductListRequestBuilder.builder().productType("Business Checking").build());

        getAnonymousUserId(interactionInstance, interactionRequestDto)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors").isEmpty())
            .andExpect(jsonPath("$.body").isNotEmpty())
            .andExpect(jsonPath("$.body[0].referenceId").value("product-1"))
            .andExpect(jsonPath("$.body[0].name").value("Business Checking"))
            .andExpect(jsonPath("$.body[0].productType").value("Business Checking"));

    }

    private ResultActions getAnonymousUserId(InteractionInstance interactionInstance,
                                             InteractionRequestDto interactionRequestDto) throws Exception {
        return mockMvc.perform(post(
                TestConstants.ACTION_URL, TestConstants.SME_ONBOARDING,
                TestConstants.GET_PRODUCT_LIST)
                .cookie(new Cookie("anonymousUserId", interactionInstance.getUserId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print());
    }

    private InteractionInstance getInteractionInstance() {
        return createInteractionInstance(
            SmeCaseDefBuilder.getInstance().build(),
            TestConstants.SELECT_PRODUCTS, UUID.randomUUID().toString());
    }

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withBlankPayload_shouldReturnAllProducts() throws Exception {
        var interactionInstance = getInteractionInstance();

        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());
        interactionRequestDto.setPayload(null);

        getAnonymousUserId(interactionInstance, interactionRequestDto)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors").isEmpty())
            .andExpect(jsonPath("$.body").isNotEmpty())
            .andExpect(jsonPath("$.body[0].referenceId").value("product-1"))
            .andExpect(jsonPath("$.body[0].name").value("Business Checking"))
            .andExpect(jsonPath("$.body[0].productType").value("Business Checking"))
            .andExpect(jsonPath("$.body[1].referenceId").value("product-2"))
            .andExpect(jsonPath("$.body[1].name").value("Business Checking Plus"))
            .andExpect(jsonPath("$.body[1].productType").value("Business Checking Plus"));
    }

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withUnknownInteraction_shouldReturnNotFound() throws Exception {
        var interactionInstance = getInteractionInstance();

        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(UUID.randomUUID().toString());
        interactionRequestDto.setPayload(null);

        getAnonymousUserId(interactionInstance, interactionRequestDto)
            .andExpect(status().isNotFound());
    }

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withInvalidPayload_shouldReturnEmptyBody() throws Exception {
        var interactionInstance = getInteractionInstance();

        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());
        interactionRequestDto.setPayload(ProductListRequestBuilder.builder().productType("Business Checking1").build());

        getAnonymousUserId(interactionInstance, interactionRequestDto)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors").isEmpty())
            .andExpect(jsonPath("$.body").isEmpty());
    }
}
