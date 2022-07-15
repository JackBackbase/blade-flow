package com.backbase.flow.onboarding.us.process.handler;

import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.status.CaseEngineEventPublisher;
import com.backbase.flow.iam.annotation.RunWithoutAuthorization;
import com.backbase.flow.onboarding.us.process.status.event.CaseClosedEvent;
import com.backbase.flow.onboarding.us.util.CaseKeyUtils;
import java.util.HashMap;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class ArchiveCaseTaskHandler implements JavaDelegate {

    private final CaseDataService caseDataService;
    private final CaseEngineEventPublisher eventPublisher;

    @RunWithoutAuthorization
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        UUID caseKey = CaseKeyUtils.getCaseKey(execution);
        caseDataService.archiveCase(caseKey);
        eventPublisher.publish(caseKey,
            new CaseClosedEvent(caseKey, "Case has been archived and closed.", new HashMap<>()));
    }
}
