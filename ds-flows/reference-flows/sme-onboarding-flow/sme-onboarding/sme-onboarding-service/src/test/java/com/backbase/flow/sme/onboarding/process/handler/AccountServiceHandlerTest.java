package com.backbase.flow.sme.onboarding.process.handler;

import static com.backbase.flow.process.ProcessConstants.PROCESS_VARIABLE_CASE_KEY;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.Test;

class AccountServiceHandlerTest {

    private final DelegateExecution execution = mock(DelegateExecution.class);
    private final AccountServiceHandler handler = new AccountServiceHandler();

    @Test
    void createAccount() {
        when(execution.getVariable(PROCESS_VARIABLE_CASE_KEY)).thenReturn(UUID.randomUUID().toString());
        handler.createAccount(execution);
        assertTrue(true);
    }

}
