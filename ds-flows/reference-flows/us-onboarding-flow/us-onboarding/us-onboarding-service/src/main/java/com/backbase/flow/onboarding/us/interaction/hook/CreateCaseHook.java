package com.backbase.flow.onboarding.us.interaction.hook;

import com.backbase.flow.interaction.engine.action.ActionHandlerHook;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.utils.CaseDataUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("create-case")
public class CreateCaseHook implements ActionHandlerHook {

    private final CaseDataUtils caseDataUtils;

    @Override
    public boolean execute(InteractionContext context) {

        return caseDataUtils.getOrCreateCaseKey(context) != null;
    }
}
