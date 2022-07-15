package com.backbase.flow.onboarding.us.process;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backbase.flow.iam.FlowSecurityContext;
import com.backbase.flow.iam.auth.AuthorizationProperties;
import com.backbase.flow.iam.local.LocalUserIdResolver;
import com.backbase.flow.process.ProcessConstants;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.junit.jupiter.api.Test;

class OnboardingProcessMessagingServiceTest {

    private final FlowSecurityContext flowSecurityContext = new FlowSecurityContext(new AuthorizationProperties(),
        new LocalUserIdResolver());
    private final RuntimeService runtimeService = mock(RuntimeService.class);

    private final OnboardingProcessMessagingService onboardingProcessMessagingService = new OnboardingProcessMessagingService(flowSecurityContext, runtimeService);

    @Test
    void setInputCompleted_setsProcessVariable() {
        var caseKey = UUID.randomUUID().toString();
        var mockProcessInstance = mock(ProcessInstance.class);
        var mockQuery = mock(ProcessInstanceQuery.class);

        when(runtimeService.createProcessInstanceQuery()).thenReturn(mockQuery);
        when(mockQuery.variableValueEquals(ProcessConstants.PROCESS_VARIABLE_CASE_KEY, caseKey))
            .thenReturn(mockQuery);
        when(mockQuery.rootProcessInstances())
            .thenReturn(mockQuery);
        when(mockQuery.list())
            .thenReturn(List.of(mockProcessInstance));
        when(mockProcessInstance.getId()).thenReturn("process-instance-id");

        onboardingProcessMessagingService.setCustomerInputCompleted(caseKey);

        verify(runtimeService).setVariable(eq("process-instance-id"), eq("CUSTOMER_INPUT_COMPLETED"), any(Timestamp.class));
    }
}
