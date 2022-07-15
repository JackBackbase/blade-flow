package com.backbase.flow.onboarding.us.interaction.hook;

import com.backbase.flow.iam.annotation.RunWithoutAuthorization;
import com.backbase.flow.interaction.engine.action.ActionHandlerHook;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.interaction.dto.PoliciesAgreementDto;
import com.backbase.flow.onboarding.us.service.policyagreement.PolicyAgreementService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("accept-terms-and-conditions")
public class AcceptingTermsAndConditionsHook implements ActionHandlerHook {

    private final PolicyAgreementService policyAgreementService;

    @RunWithoutAuthorization
    @Override
    public boolean execute(InteractionContext context) {
        final UUID caseKey = context.getCaseKey();

        policyAgreementService.processPolicies(new PoliciesAgreementDto(Boolean.TRUE), caseKey);

        return true;
    }

}
