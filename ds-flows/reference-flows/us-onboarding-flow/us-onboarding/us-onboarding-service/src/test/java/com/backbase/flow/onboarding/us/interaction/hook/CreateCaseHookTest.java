package com.backbase.flow.onboarding.us.interaction.hook;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.utils.CaseDataUtils;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateCaseHookTest {

    @Mock
    private CaseDataUtils caseDataUtils;
    @Mock
    private InteractionContext context;
    @InjectMocks
    private CreateCaseHook createCaseHook;

    @Test
    void execute_caseCreated_returnsTrue() {
        when(caseDataUtils.getOrCreateCaseKey(context)).thenReturn(UUID.randomUUID());

        var result = createCaseHook.execute(context);

        assertThat(result).isTrue();
        verify(caseDataUtils).getOrCreateCaseKey(context);
    }

    @Test
    void execute_caseNotCreated_returnsFalse() {
        when(caseDataUtils.getOrCreateCaseKey(context)).thenReturn(null);

        var result = createCaseHook.execute(context);

        assertThat(result).isFalse();
        verify(caseDataUtils).getOrCreateCaseKey(context);
    }

}