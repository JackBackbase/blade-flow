package com.backbase.flow.sme.onboarding.process.handler;

import static com.backbase.flow.process.ProcessConstants.PROCESS_VARIABLE_CASE_KEY;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backbase.flow.casedata.CaseDataService;
import java.util.UUID;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.Test;

class ArchiveCaseTaskHandlerTest {

    private final CaseDataService caseDataService = mock(CaseDataService.class);
    private final DelegateExecution execution = mock(DelegateExecution.class);
    private final ArchiveCaseTaskHandler handler = new ArchiveCaseTaskHandler(caseDataService);

    @Test
    void execute() {
        var caseKey = UUID.randomUUID();

        when(execution.getVariable(PROCESS_VARIABLE_CASE_KEY)).thenReturn(caseKey.toString());

        handler.execute(execution);

        verify(caseDataService, times(1)).archiveCase(caseKey);
    }
}
