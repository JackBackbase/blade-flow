package com.backbase.flow.onboarding.us.interaction.hook;

import com.backbase.flow.interaction.engine.action.ActionHandlerHook;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("start-onboarding-process")
public class StartProcessHook implements ActionHandlerHook {

    @Value("${definitions.process.key}")
    private String processId;

    @Override
    public boolean execute(InteractionContext context) {
        context.startProcess(context.getCaseKey(), processId);
        return true;
    }
}
