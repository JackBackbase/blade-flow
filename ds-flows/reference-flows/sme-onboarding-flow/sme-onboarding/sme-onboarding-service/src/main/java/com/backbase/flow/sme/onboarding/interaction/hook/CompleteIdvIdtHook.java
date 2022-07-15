package com.backbase.flow.sme.onboarding.interaction.hook;

import com.backbase.flow.iam.FlowSecurityContext;
import com.backbase.flow.interaction.engine.action.ActionHandlerHook;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import com.backbase.flow.sme.onboarding.interaction.model.TaskDto;
import com.backbase.flow.sme.onboarding.interaction.service.TaskCompletionService;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component("complete-idv-idt")
public class CompleteIdvIdtHook extends CompleteApplicantTaskHook {

    @Getter
    @SuppressWarnings("squid:S1170")
    private final String taskDefinitionKey = "idv-idt";

    public CompleteIdvIdtHook(FlowSecurityContext flowSecurityContext,
                              TaskCompletionService taskCompletionService) {
        super(flowSecurityContext, taskCompletionService);
    }

    @Override
    boolean isTaskReadyToClose(TaskDto taskDto, SmeCaseDefinition smeCase) {
        return true;
    }
}
