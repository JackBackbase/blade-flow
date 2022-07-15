package com.backbase.flow.onboarding.us.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.flow.process.ProcessConstants;
import java.util.UUID;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.Test;

class CaseKeyUtilsTest {

    private final DelegateExecution execution = mock(DelegateExecution.class);

    @Test
    void getCaseKey_variableExists_keyIsReturned() {
        final UUID expectedCaseKey = UUID.randomUUID();
        when(execution.getVariable(ProcessConstants.PROCESS_VARIABLE_CASE_KEY)).thenReturn(expectedCaseKey.toString());

        UUID actualCaseKey = CaseKeyUtils.getCaseKey(execution);

        assertThat(actualCaseKey).isEqualTo(expectedCaseKey);
    }

    @Test
    void getCaseKey_variableDoesNotExists_exceptionIsThrown() {
        assertThrows(NotFoundException.class, () -> CaseKeyUtils.getCaseKey(execution));
    }
}
