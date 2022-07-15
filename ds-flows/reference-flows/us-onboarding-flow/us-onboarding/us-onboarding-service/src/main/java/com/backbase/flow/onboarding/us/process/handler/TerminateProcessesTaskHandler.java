package com.backbase.flow.onboarding.us.process.handler;

import com.backbase.flow.onboarding.us.util.CaseKeyUtils;
import com.backbase.flow.process.ProcessConstants;
import com.backbase.flow.process.annotation.ProcessBean;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@ProcessBean("terminateProcessesTaskHandler")
@Component
public class TerminateProcessesTaskHandler implements JavaDelegate {

    private final RuntimeService runtimeService;

    @Value("${definitions.process.key}")
    private String processDefinitionId;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        UUID caseKey = CaseKeyUtils.getCaseKey(execution);

        runtimeService.createProcessInstanceQuery()
            .variableValueEquals(ProcessConstants.PROCESS_VARIABLE_CASE_KEY, caseKey.toString())
            .list()
            .stream()
            .filter(pi -> !pi.getProcessInstanceId().equals(execution.getProcessInstanceId()) &&
                pi.getProcessDefinitionId().contains(processDefinitionId))
            .forEach(this::terminate);
    }

    private void terminate(ProcessInstance processInstance) {
        log.info("Terminating process {} with id {}", processInstance.getProcessDefinitionId(), processInstance.getId());
        runtimeService.deleteProcessInstance(processInstance.getId(), "Onboarding terminated");
    }
}
