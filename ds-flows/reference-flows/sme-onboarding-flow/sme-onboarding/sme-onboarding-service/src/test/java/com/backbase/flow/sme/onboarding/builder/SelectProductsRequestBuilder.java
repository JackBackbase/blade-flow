package com.backbase.flow.sme.onboarding.builder;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SelectProductsRequestBuilder {

    private List<ProductItem> selection;

    @Getter
    @Setter
    @Builder
    public static class ProductItem {

        private String referenceId;
        private String productName;
    }

}
