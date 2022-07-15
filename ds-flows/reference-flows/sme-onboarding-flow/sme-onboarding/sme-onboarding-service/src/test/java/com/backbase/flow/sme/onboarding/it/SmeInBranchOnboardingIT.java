package com.backbase.flow.sme.onboarding.it;

import static com.backbase.flow.sme.onboarding.TestConstants.EXISTING_CASE;
import static org.assertj.core.api.Assertions.assertThat;

import com.backbase.flow.interaction.api.model.InteractionResponseDto;
import com.backbase.flow.sme.onboarding.TestConstants;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;

abstract class SmeInBranchOnboardingIT<S extends Enum<S>> extends InteractionIT<S> {

    @Setter
    protected String currentInteractionName;

    @Override
    String interactionName() {
        return this.currentInteractionName;
    }

    @BeforeEach
    protected void setCurrentInteractionName() {
        setCurrentInteractionName(TestConstants.IN_BRANCH_ONBOARDING_START);
    }

    protected void checkCasePromoted() {
        var aCase = getCase(caseKey);
        assertThat(aCase.isPreliminary()).isFalse();
    }

    protected InteractionResponseDto performExistingCaseRequest() {
        return action(EXISTING_CASE, interactionId, null);
    }
}
