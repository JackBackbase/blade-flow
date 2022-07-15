package com.backbase.flow.sme.onboarding.process.handler;

import static com.backbase.flow.process.ProcessConstants.PROCESS_VARIABLE_CASE_KEY;

import com.backbase.flow.iam.annotation.RunWithoutAuthorization;
import com.backbase.flow.process.annotation.ProcessBean;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@ProcessBean("accountService")
public class AccountServiceHandler {

    @RunWithoutAuthorization
    public void createAccount(DelegateExecution execution) {
        var caseKey = UUID.fromString(execution.getVariable(PROCESS_VARIABLE_CASE_KEY).toString());
        log.info("here the event is thrown for the process {}...", caseKey);
    }
}
