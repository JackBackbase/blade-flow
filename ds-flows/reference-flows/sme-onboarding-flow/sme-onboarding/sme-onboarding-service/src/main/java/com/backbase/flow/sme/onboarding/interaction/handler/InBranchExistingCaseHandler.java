package com.backbase.flow.sme.onboarding.interaction.handler;

import com.backbase.flow.interaction.engine.action.ActionHandler;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import org.springframework.stereotype.Component;

@Component("existing-case")
public class InBranchExistingCaseHandler implements ActionHandler<Void, Void> {

    @Override
    public ActionResult<Void> handle(Void payload, InteractionContext context) {
        return ActionResult.success(null);
    }
}
