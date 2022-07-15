package com.backbase.flow.onboarding.us.process.handler;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backbase.flow.process.ProcessConstants;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class TerminateProcessesTaskHandlerTest {

    private static final String TERMINATION_REASON = "Onboarding terminated";
    private final RuntimeService runtimeService = mock(RuntimeService.class);
    private final TerminateProcessesTaskHandler handler = new TerminateProcessesTaskHandler(runtimeService);

    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(handler, "processDefinitionId", "def1");
    }

    @Test
    void execute_terminatesProcesses() throws Exception {
        final var caseKey = UUID.randomUUID();
        final var processDefinitionId = "def1";
        var execution = execution(caseKey, caseKey.toString(), processDefinitionId);
        var processList = List.of(mockedProcessInstance(UUID.randomUUID(), processDefinitionId),
            mockedProcessInstance(UUID.randomUUID(), processDefinitionId), mockedProcessInstance(UUID.randomUUID(), processDefinitionId));
        setupMockedQueryResults(caseKey.toString(), processList);

        handler.execute(execution);

        processList.forEach(
            p -> verify(runtimeService).deleteProcessInstance(eq(p.getId()), eq(TERMINATION_REASON)));
    }

    @Test
    void execute_doesNotTerminateSelf() throws Exception {
        final var caseKey = UUID.randomUUID();
        final var ownProcessInstanceId = UUID.randomUUID();
        final var processDefinitionId = "def1";
        final var execution = execution(caseKey, ownProcessInstanceId.toString(), processDefinitionId);
        final var processList = List.of(mockedProcessInstance(UUID.randomUUID(), processDefinitionId),
            mockedProcessInstance(ownProcessInstanceId, processDefinitionId), mockedProcessInstance(UUID.randomUUID(), processDefinitionId));
        setupMockedQueryResults(caseKey.toString(), processList);

        handler.execute(execution);

        verify(runtimeService, times(2)).deleteProcessInstance(anyString(), eq(TERMINATION_REASON));
        verify(runtimeService, times(0)).deleteProcessInstance(eq(ownProcessInstanceId.toString()), anyString());

    }

    @Test
    void execute_doNotTerminateOtherProcessDefinition() throws Exception {
        UUID processInstanceId = UUID.randomUUID();
        var caseKey = UUID.randomUUID();
        var firstProcessDefinitionId = String.format("def1-%s", UUID.randomUUID());
        var secondProcessDefinitionId = String.format("def2-%s", UUID.randomUUID());
        final var execution = execution(caseKey, processInstanceId.toString(), firstProcessDefinitionId);

        var processToBeTerminated = UUID.randomUUID();
        var processToNotBeTerminated = UUID.randomUUID();
        var processList = List.of(mockedProcessInstance(processInstanceId, firstProcessDefinitionId),
            mockedProcessInstance(processToBeTerminated, firstProcessDefinitionId),
            mockedProcessInstance(processToNotBeTerminated, secondProcessDefinitionId));
        setupMockedQueryResults(caseKey.toString(), processList);

        handler.execute(execution);

        verify(runtimeService, times(1)).deleteProcessInstance(processToBeTerminated.toString(), TERMINATION_REASON);
        verify(runtimeService, times(0)).deleteProcessInstance(eq(processInstanceId.toString()), anyString());
        verify(runtimeService, times(0)).deleteProcessInstance(eq(processToNotBeTerminated.toString()), anyString());
    }

    private ProcessInstance mockedProcessInstance(UUID id, String processDefinitionId) {
        return FakeProcessInstance.builder()
            .id(id.toString())
            .processInstanceId(id.toString())
            .processDefinitionId(processDefinitionId)
            .build();
    }

    private void setupMockedQueryResults(final String forCaseKey, List<ProcessInstance> processInstances) {
        var processInstanceQuery = mock(ProcessInstanceQuery.class);
        when(processInstanceQuery.variableValueEquals(eq(ProcessConstants.PROCESS_VARIABLE_CASE_KEY), eq(forCaseKey)))
            .thenReturn(processInstanceQuery);

        when(processInstanceQuery.list())
            .thenReturn(processInstances);

        when(runtimeService.createProcessInstanceQuery())
            .thenReturn(processInstanceQuery);

    }

    private DelegateExecution execution(UUID caseKey, String processInstanceId, String processDefinitionId) {
        var execution = mock(DelegateExecution.class);

        when(execution.getId()).thenReturn(caseKey.toString());
        when(execution.getProcessInstanceId()).thenReturn(processInstanceId);
        when(execution.getVariable(ProcessConstants.PROCESS_VARIABLE_CASE_KEY)).thenReturn(caseKey.toString());
        when(execution.getProcessDefinitionId()).thenReturn(processDefinitionId);

        return execution;
    }

    @Value
    @Builder
    static class FakeProcessInstance implements ProcessInstance {

        String processDefinitionId;
        String businessKey;
        String rootProcessInstanceId;
        String caseInstanceId;
        String id;
        boolean suspended;
        boolean ended;
        String processInstanceId;
        String tenantId;

    }

}
