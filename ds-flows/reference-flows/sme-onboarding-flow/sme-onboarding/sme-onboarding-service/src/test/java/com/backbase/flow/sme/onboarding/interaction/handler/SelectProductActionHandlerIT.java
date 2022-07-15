package com.backbase.flow.sme.onboarding.interaction.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backbase.flow.iam.util.WithFlowAnonymousUser;
import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.interaction.engine.data.model.InteractionInstance;
import com.backbase.flow.sme.onboarding.BaseIntegrationIT;
import com.backbase.flow.sme.onboarding.TestConstants;
import com.backbase.flow.sme.onboarding.builder.SelectProductsRequestBuilder;
import com.backbase.flow.sme.onboarding.builder.SelectProductsRequestBuilder.ProductItem;
import com.backbase.flow.sme.onboarding.builder.SmeCaseDefBuilder;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class SelectProductActionHandlerIT extends BaseIntegrationIT {

    @Test
    @WithFlowAnonymousUser
    void postInteraction_withValidData_shouldSave() throws Exception {
        var interactionInstance = getInteractionInstance();

        var interactionRequestDto = new InteractionRequestDto();
        interactionRequestDto.setInteractionId(interactionInstance.getId().toString());
        var productItemList = List.of(ProductItem.builder()
            .productName("Business Checking").referenceId("product-1").build());
        interactionRequestDto.setPayload(SelectProductsRequestBuilder.builder().selection(productItemList).build());

        getAnonymousUserId(interactionInstance, interactionRequestDto)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.interactionId").isNotEmpty())
            .andExpect(jsonPath("$.actionErrors").isEmpty());

        var smeCase = getCaseData(interactionInstance.getCaseKey());

        assertThat(smeCase).isNotNull();
        assertThat(smeCase.getProductSelection()).isNotNull();
        assertThat(smeCase.getProductSelection().getSelectedProducts()).isNotEmpty();
        assertThat(smeCase.getProductSelection().getSelectedProducts().get(0).getProductName())
            .isEqualTo("Business Checking");
        assertThat(smeCase.getProductSelection().getSelectedProducts().get(0).getReferenceId()).isEqualTo("product-1");
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

    private ResultActions getAnonymousUserId(InteractionInstance interactionInstance,
                                             InteractionRequestDto interactionRequestDto) throws Exception {
        return mockMvc.perform(post(
                TestConstants.ACTION_URL, TestConstants.SME_ONBOARDING,
                TestConstants.SELECT_PRODUCTS)
                .cookie(new Cookie("anonymousUserId", interactionInstance.getUserId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(interactionRequestDto)))
            .andDo(print());
    }

    private InteractionInstance getInteractionInstance() {
        return createInteractionInstance(
            SmeCaseDefBuilder.getInstance().soleProp().build(),
            TestConstants.SELECT_PRODUCTS, UUID.randomUUID().toString());
    }

}
