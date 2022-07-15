package com.backbase.flow.sme.onboarding.interaction.hook;

import com.backbase.flow.iam.FlowSecurityContext;
import com.backbase.flow.interaction.engine.action.ActionHandlerHook;
import com.backbase.flow.interaction.engine.action.ActionHookResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import com.backbase.flow.sme.onboarding.interaction.model.TaskDto;
import com.backbase.flow.sme.onboarding.interaction.service.TaskCompletionService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class CompleteApplicantTaskHook implements ActionHandlerHook {

    private final FlowSecurityContext flowSecurityContext;
    private final TaskCompletionService taskCompletionService;

    abstract String getTaskDefinitionKey();

    abstract boolean isTaskReadyToClose(TaskDto taskDto, SmeCaseDefinition smeCase);

    @Override
    public ActionHookResult executeHook(InteractionContext context) {
        var smeCase = context.getCaseData(SmeCaseDefinition.class);
        flowSecurityContext.runWithoutAuthorization(
            () -> taskCompletionService.completeTaskAssociatedToApplicant(
                getTaskDefinitionKey(),
                context.getCaseKey(),
                task -> isTaskReadyToClose(task, smeCase))
        );
        return new ActionHookResult(true, null);
    }

    @Override
    @SuppressWarnings("squid:S5738")
    public boolean execute(InteractionContext context) {
        throw new UnsupportedOperationException("Trying to call deprecated method. Call 'executeHook' instead.");
    }
}
