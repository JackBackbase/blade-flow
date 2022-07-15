package com.backbase.flow.sme.onboarding.interaction.hook;

import com.backbase.flow.interaction.engine.action.ActionHandlerHook;
import com.backbase.flow.interaction.engine.action.ActionHookResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import com.backbase.flow.sme.onboarding.process.service.SmeBusinessRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("check-business-relation")
@RequiredArgsConstructor
public class CheckBusinessRelationHook implements ActionHandlerHook {

    private final SmeBusinessRelationService smeBusinessRelationService;

    @Override
    @SuppressWarnings("squid:S5738")
    public boolean execute(InteractionContext context) {
        throw new UnsupportedOperationException("Trying to call deprecated method. Call 'executeHook' instead.");
    }

    @Override
    public ActionHookResult executeHook(InteractionContext context) {
        var smeCase = context.getCaseData(SmeCaseDefinition.class);
        var isBusinessRelationRequired = smeBusinessRelationService.isBusinessRelationRequired(smeCase, context);
        smeCase.setBusinessRelationRequired(isBusinessRelationRequired);
        context.saveCaseDataToReadCaseData(smeCase);
        return new ActionHookResult(true, null);
    }
}
