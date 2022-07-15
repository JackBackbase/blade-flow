package com.backbase.flow.sme.onboarding.process.handler;

import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.service.bpmnprocess.CaseKey;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArchiveCaseTaskHandler implements JavaDelegate {

    private final CaseDataService caseDataService;

    @Override
    public void execute(DelegateExecution execution) {
        var caseKey = CaseKey.from(execution);
        caseDataService.archiveCase(caseKey.getKey());
    }
}
