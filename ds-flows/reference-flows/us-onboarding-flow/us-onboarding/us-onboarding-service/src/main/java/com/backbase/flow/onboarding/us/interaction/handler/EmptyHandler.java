package com.backbase.flow.onboarding.us.interaction.handler;

import com.backbase.flow.interaction.engine.action.ActionHandler;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.application.uso.casedata.Onboarding;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("empty-handler")
public class EmptyHandler implements ActionHandler<Object, Onboarding> {

    @Override
    public ActionResult<Onboarding> handle(Object o, InteractionContext context) {
        return new ActionResult<>(null);
    }
}
