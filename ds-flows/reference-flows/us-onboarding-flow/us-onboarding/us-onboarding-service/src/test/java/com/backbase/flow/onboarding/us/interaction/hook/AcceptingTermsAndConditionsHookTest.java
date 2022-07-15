package com.backbase.flow.onboarding.us.interaction.hook;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.interaction.dto.OnboardingDto;
import com.backbase.flow.onboarding.us.interaction.dto.PoliciesAgreementDto;
import com.backbase.flow.onboarding.us.service.policyagreement.PolicyAgreementService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AcceptingTermsAndConditionsHookTest {

    @Mock
    private InteractionContext context;
    @Mock
    private PolicyAgreementService policyAgreementService;
    @InjectMocks
    private AcceptingTermsAndConditionsHook acceptingTermsAndConditionsHook;

    @Test
    void execute() {
        final UUID caseKey = UUID.randomUUID();
        when(context.getCaseKey()).thenReturn(caseKey);

        final Boolean result = acceptingTermsAndConditionsHook.execute(context);

        assertThat(result).isTrue();
    }
}