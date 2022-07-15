package com.backbase.flow.onboarding.us.util;

import com.backbase.buildingblocks.presentation.errors.Error;
import com.backbase.flow.integration.domain.IntegrationError;

public class ErrorMapper {

    private ErrorMapper() {

    }

    public static Error toError(IntegrationError error) {
        return new Error(error.getKey(), error.getMessage(), null);
    }
}
