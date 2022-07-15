package com.backbase.flow.onboarding.us.process.handler;

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
@ProcessBean("messageService")
public class MessageServiceHandler {

    @RunWithoutAuthorization
    public void emit(DelegateExecution execution, String type, String message) {
        UUID caseKey = UUID.fromString(execution.getVariable(PROCESS_VARIABLE_CASE_KEY).toString());
        log.info("here the message {} of the type {} is thrown for the process {}...",
            message, type, caseKey.toString());
    }
}
