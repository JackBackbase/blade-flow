package com.backbase.flow.onboarding.us.interaction.handler;

import com.backbase.flow.interaction.engine.action.ActionHandler;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.interaction.dto.SubmitInBranchDto;
import com.backbase.flow.utils.CaseDataUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("submit-in-branch")
public class SubmitInBranchHandler implements ActionHandler<Void, SubmitInBranchDto> {

    private final CaseDataUtils caseDataUtils;

    @Override
    public ActionResult<SubmitInBranchDto> handle(Void o, InteractionContext context) {
        final UUID caseKey = caseDataUtils.getOrCreateCaseKey(context);
        return new ActionResult<>(new SubmitInBranchDto(caseKey));
    }
}
