package com.backbase.flow.onboarding.us.process;

import com.backbase.flow.iam.FlowSecurityContext;
import com.backbase.flow.process.ProcessConstants;
import java.sql.Timestamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OnboardingProcessMessagingService {

    private final FlowSecurityContext flowSecurityContext;
    private final RuntimeService runtimeService;
    private static final String CUSTOMER_INPUT_COMPLETED = "CUSTOMER_INPUT_COMPLETED";

    public void setCustomerInputCompleted(String caseKey) {
        flowSecurityContext.runWithoutAuthorization(() -> {
            runtimeService.createProcessInstanceQuery()
                .variableValueEquals(ProcessConstants.PROCESS_VARIABLE_CASE_KEY, caseKey)
                .rootProcessInstances()
                .list()
                .forEach(process -> {
                    log.info("Setting {} for process {}", CUSTOMER_INPUT_COMPLETED, process.getId());
                    runtimeService
                        .setVariable(process.getId(), CUSTOMER_INPUT_COMPLETED, new Timestamp(System.currentTimeMillis()));
                });
        });
    }
}
