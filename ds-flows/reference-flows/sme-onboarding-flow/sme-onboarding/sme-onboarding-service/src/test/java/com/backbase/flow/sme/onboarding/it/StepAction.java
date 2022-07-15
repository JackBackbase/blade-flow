package com.backbase.flow.sme.onboarding.it;

import lombok.Value;

@Value
class StepAction<S extends Enum<S>> {

    S step;
    Runnable action;
}
