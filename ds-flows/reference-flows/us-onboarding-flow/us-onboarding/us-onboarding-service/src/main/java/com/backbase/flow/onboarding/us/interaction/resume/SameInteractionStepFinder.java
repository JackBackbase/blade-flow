package com.backbase.flow.onboarding.us.interaction.resume;

import com.backbase.flow.interaction.engine.data.model.InteractionInstance;
import com.backbase.flow.interaction.engine.data.model.StepInstance;
import com.backbase.flow.service.resume.stepfinder.StepFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SameInteractionStepFinder implements StepFinder {

    @Override
    public StepInstance getResumeToStep(InteractionInstance currentInteraction,
        InteractionInstance previousInteraction) {
        if (isSameInteraction(currentInteraction, previousInteraction)) {
            return previousInteraction.getCurrentStep();
        }
        //Return null to not set a step. This allows for resuming "into" the same case data from different interactions.
        return null;
    }

    boolean isSameInteraction(InteractionInstance one, InteractionInstance two) {
        return one.getInteractionDefinition().equals(two.getInteractionDefinition());
    }
}

