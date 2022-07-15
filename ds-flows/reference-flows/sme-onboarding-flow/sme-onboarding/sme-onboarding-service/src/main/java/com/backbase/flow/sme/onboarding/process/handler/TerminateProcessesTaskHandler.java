package com.backbase.flow.sme.onboarding.process.handler;

import static com.backbase.flow.process.ProcessConstants.PROCESS_VARIABLE_CASE_KEY;
import static com.backbase.flow.sme.onboarding.constants.ProcessConstants.BPM_DATA_GATHERING;

import com.backbase.flow.process.annotation.ProcessBean;
import com.backbase.flow.service.bpmnprocess.CaseKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@ProcessBean("terminateProcessesTaskHandler")
public class TerminateProcessesTaskHandler implements JavaDelegate {

    private final RuntimeService runtimeService;

    @Override
    public void execute(DelegateExecution execution) {
        var caseKey = CaseKey.from(execution).getKey();

        runtimeService.createProcessInstanceQuery()
            .variableValueEquals(PROCESS_VARIABLE_CASE_KEY, caseKey.toString())
            .list()
            .stream()
            .filter(pi -> !pi.getProcessInstanceId().equals(execution.getProcessInstanceId())
                && pi.getProcessDefinitionId().contains(BPM_DATA_GATHERING))
            .forEach(this::terminate);
    }

    private void terminate(ProcessInstance processInstance) {
        log.info("Terminating process with id {}", processInstance.getId());
        runtimeService.deleteProcessInstance(processInstance.getId(), "Data-gathering terminated");
    }
}
