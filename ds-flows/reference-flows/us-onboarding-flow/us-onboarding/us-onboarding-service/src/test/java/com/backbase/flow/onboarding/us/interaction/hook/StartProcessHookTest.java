package com.backbase.flow.onboarding.us.interaction.hook;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.backbase.flow.interaction.engine.action.InteractionContext;
import org.junit.jupiter.api.Test;

class StartProcessHookTest {

    private final InteractionContext interactionContext = mock(InteractionContext.class);
    private final StartProcessHook startProcessHook = new StartProcessHook();

    @Test
    void execute_ProcessIsStarted() {
        startProcessHook.execute(interactionContext);

        verify(interactionContext, times(1))
            .startProcess(eq(interactionContext.getCaseKey()), eq("us-onboarding"));
    }
}
