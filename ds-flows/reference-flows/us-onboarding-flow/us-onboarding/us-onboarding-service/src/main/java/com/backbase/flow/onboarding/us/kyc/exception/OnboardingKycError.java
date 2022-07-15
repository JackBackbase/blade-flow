package com.backbase.flow.onboarding.us.kyc.exception;

import com.backbase.buildingblocks.presentation.errors.Error;
import com.backbase.flow.interaction.engine.action.Errors;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class OnboardingKycError {
    public static final Error OUS_001 =
            Errors.buildError("OUS-001", "Number of answers do no match number of questions");
    public static final Error OUS_002 =
            Errors.buildError("OUS-002", "Single answer question must have one answer only");
    public static final Error OUS_003 =
            Errors.buildError("OUS-003", "Answers don't match the question");
}
