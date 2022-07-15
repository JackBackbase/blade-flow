package com.backbase.flow.sme.onboarding.error;

import static com.backbase.flow.interaction.engine.action.Errors.buildError;

import com.backbase.buildingblocks.presentation.errors.Error;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SmeErrors {

    public static final Error INPUT_DATA_INVALID = buildError("SME_001", "Input payload is invalid.");
    public static final Error INPUT_AGE_INVALID = buildError("SME_003", "Anchor data submission failed.");
    public static final Error INPUT_REGISTRAR_INVALID = buildError("SME_004", "Registrar is invalid.");
}
