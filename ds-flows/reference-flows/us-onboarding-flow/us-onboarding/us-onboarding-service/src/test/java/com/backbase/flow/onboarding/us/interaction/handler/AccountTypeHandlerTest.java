package com.backbase.flow.onboarding.us.interaction.handler;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.interaction.dto.AccountTypeDto;
import com.backbase.flow.onboarding.us.interaction.dto.OnboardingDto;
import com.backbase.flow.utils.CaseDataUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

class AccountTypeHandlerTest {

    private final InteractionContext interactionContext = Mockito.mock(InteractionContext.class);
    private final CaseDataUtils caseDataUtils = mock(CaseDataUtils.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final AccountTypeHandler accountTypeHandler = new AccountTypeHandler(caseDataUtils, objectMapper);

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void handle(boolean isJointAccount) {
        when(interactionContext.getCaseData(Onboarding.class))
            .thenReturn(new Onboarding()
                .withIsMainApplicantFlow(true)
                .withMainApplicant(new Applicant()));
        final AccountTypeDto emptyAccountTypeDto = new AccountTypeDto(isJointAccount);
        ActionResult<OnboardingDto> result = accountTypeHandler.handle(emptyAccountTypeDto, interactionContext);

        assertThat(result.getBody().getIsJointAccount()).isEqualTo(isJointAccount);
    }

    @Test
    void handleWithValidationError() {
        final AccountTypeDto emptyAccountTypeDto = new AccountTypeDto();
        ActionResult<OnboardingDto> result = accountTypeHandler.handle(emptyAccountTypeDto, interactionContext);

        assertThat(result.getErrors().size()).isNotZero();
    }
}