package com.backbase.flow.onboarding.us.interaction.hook;

import com.backbase.flow.iam.FlowSecurityContext;
import com.backbase.flow.interaction.engine.action.ActionHandlerHook;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.service.TaskCompletionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("complete-address-validation-idt")
public class CompleteAddressValidationIdtHook implements ActionHandlerHook {

    private final FlowSecurityContext flowSecurityContext;
    private final TaskCompletionService taskCompletionService;
    private final IdtDataCollectionService idtDataCollectionService;

    @Override
    public boolean execute(InteractionContext context) {
        if (idtDataCollectionService.addressIdtDataCollected(context.getCaseKey())) {
            flowSecurityContext.runWithoutAuthorization(() -> {
                taskCompletionService.completeTaskByKeyForCaseKey("address-validation-idt", context.getCaseKey());
                return true;
            });
            return true;
        }
        return false;
    }
}
