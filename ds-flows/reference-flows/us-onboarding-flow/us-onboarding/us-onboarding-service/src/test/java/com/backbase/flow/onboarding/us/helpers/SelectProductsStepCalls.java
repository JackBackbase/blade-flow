package com.backbase.flow.onboarding.us.helpers;

import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.interaction.api.model.InteractionResponseDto;
import com.backbase.flow.onboarding.us.BaseIT;
import com.backbase.flow.service.productselection.interaction.dto.SelectProductRequest;
import com.backbase.flow.service.productselection.interaction.dto.SelectProductRequest.ProductItem;
import java.util.Arrays;

public class SelectProductsStepCalls extends BaseIT {

    private static final String SELECT_PRODUCTS_ENDPOINT = "select-products";

    public static InteractionResponseDto selectProducts(String interactionId) throws Exception {
        return performAction(SELECT_PRODUCTS_ENDPOINT, createPayload(interactionId));
    }

    public static InteractionResponseDto selectProductsUsingJwt(String interactionId) throws Exception {
        return performActionWithJwt(SELECT_PRODUCTS_ENDPOINT, createPayload(interactionId));
    }

    private static InteractionRequestDto createPayload(String interactionId) {
        SelectProductRequest selectProductRequest = new SelectProductRequest(Arrays.asList(
            new ProductItem("premium-savings", "High-Rate Savings", 1),
            new ProductItem("online-saving", "Online Savings Account", 2)));

        final InteractionRequestDto request = new InteractionRequestDto();
        request.setInteractionId(interactionId);
        request.setPayload(selectProductRequest);
        return request;
    }

}
