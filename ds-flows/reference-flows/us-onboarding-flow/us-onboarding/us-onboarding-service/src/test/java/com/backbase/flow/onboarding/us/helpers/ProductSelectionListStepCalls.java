package com.backbase.flow.onboarding.us.helpers;

import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.interaction.api.model.InteractionResponseDto;
import com.backbase.flow.onboarding.us.BaseIT;
import com.backbase.flow.service.productselection.interaction.dto.SelectProductRequest;

public class ProductSelectionListStepCalls extends BaseIT {

    private static final String GET_PRODUCT_LIST_ENDPOINT = "get-product-list";

    public static InteractionResponseDto getProductList(String interactionId) throws Exception {
        return performAction(GET_PRODUCT_LIST_ENDPOINT, createPayload(interactionId));
    }

    public static InteractionResponseDto getProductListUsingJwt(String interactionId) throws Exception {
        return performActionWithJwt(GET_PRODUCT_LIST_ENDPOINT, createPayload(interactionId));
    }

    private static InteractionRequestDto createPayload(String interactionId) {
        final SelectProductRequest productResponse = new SelectProductRequest();

        final InteractionRequestDto request = new InteractionRequestDto();
        request.setInteractionId(interactionId);
        request.setPayload(productResponse);
        return request;
    }
}
