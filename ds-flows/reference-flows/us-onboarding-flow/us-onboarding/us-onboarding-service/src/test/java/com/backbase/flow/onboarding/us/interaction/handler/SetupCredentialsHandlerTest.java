package com.backbase.flow.onboarding.us.interaction.handler;

import static com.backbase.flow.onboarding.us.validation.ValidationConstants.PSW_LENGTH_8_MESSAGE;
import static com.backbase.flow.onboarding.us.validation.ValidationConstants.PSW_NULL_MESSAGE;
import static com.backbase.flow.onboarding.us.validation.ValidationConstants.PSW_ONE_DIGIT_MESSAGE;
import static com.backbase.flow.onboarding.us.validation.ValidationConstants.PSW_ONE_UPPERCASE_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backbase.buildingblocks.presentation.errors.Error;
import com.backbase.flow.application.uso.casedata.Address;
import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.ErrorCodes;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.exception.UserIdentityException;
import com.backbase.flow.onboarding.us.interaction.dto.CredentialsDto;
import com.backbase.flow.onboarding.us.interaction.dto.response.CredentialsResponseDto;
import com.backbase.flow.onboarding.us.process.OnboardingProcessMessagingService;
import com.backbase.flow.utils.CaseDataUtils;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

class SetupCredentialsHandlerTest {

    private final InteractionContext interactionContext = mock(InteractionContext.class);
    private final CaseDataService caseDataService = mock(CaseDataService.class);
    private final OnboardingProcessMessagingService onboardingProcessMessagingService = mock(
        OnboardingProcessMessagingService.class);

    private final CaseDataUtils caseDataUtils = new CaseDataUtils();
    private final JourneyMapper<Onboarding> journeyMapper = mock(JourneyMapper.class);
    private final SetupCredentialsHandler credentialsHandler = new SetupCredentialsHandler(journeyMapper,
        caseDataUtils, onboardingProcessMessagingService);

    @BeforeEach
    private void setup() {
        when(caseDataService.updateCase(any(Case.class)))
            .thenAnswer(a -> a.getArgument(0));
    }

    @ParameterizedTest
    @CsvSource({
        "qwerty1A, true",
        "qwerty1_A!^dfg, true",
        "Z asd asd asd @@$$2, false",
        "!ThisIs_Quite-A-s3cur3::pa$$w0rd;, false"})
    void testPasswordIsValid(final String password, boolean clientAcceptedMock) throws UserIdentityException {

        Mockito.reset(interactionContext);

        final CredentialsDto credentials = new CredentialsDto(password);
        Onboarding onboarding = new Onboarding().withMainApplicant(new Applicant());
        onboarding.getMainApplicant().setFirstName(clientAcceptedMock ? "AnyName" : "correlation");
        onboarding.getMainApplicant().setEmail("email@com.invalid");
        onboarding.getMainApplicant().setAddress(new Address());
        onboarding.setIsMainApplicantFlow(true);
        setupCase(onboarding);

        final ActionResult<CredentialsResponseDto> actionResult = credentialsHandler
            .handle(credentials, interactionContext);

        assertThat(actionResult.getErrors()).isEmpty();
        assertThat(actionResult.getBody().getFirstName()).isEqualTo(onboarding.getMainApplicant().getFirstName());
        verify(journeyMapper, times(1)).read(isNull(), isNull(), anyString());
        verify(onboardingProcessMessagingService, times(1))
            .setCustomerInputCompleted(interactionContext.getCaseKey().toString());
    }

    @Test
    void passwordValidationWorks() {
        final String pwd8 = PSW_LENGTH_8_MESSAGE;
        final String pwd1 = PSW_ONE_DIGIT_MESSAGE;
        final String pwdA = PSW_ONE_UPPERCASE_MESSAGE;

        assertPasswordIsNotValid(null, PSW_NULL_MESSAGE);
        assertPasswordIsNotValid("", pwd8, pwd1, pwdA);
        assertPasswordIsNotValid("1qeQ", pwd8);
        assertPasswordIsNotValid("qeQ#_sdfs", pwd1);
        assertPasswordIsNotValid("1qeasd_+=asdasd", pwdA);
        assertPasswordIsNotValid("qeasdA", pwd8, pwd1);
        assertPasswordIsNotValid("qeasd1", pwd8, pwdA);
        assertPasswordIsNotValid("_%sd=====", pwd1, pwdA);
    }

    private void setupCase(Onboarding caseData) {
        final var caseKey = UUID.randomUUID();
        when(interactionContext.getCaseKey()).thenReturn(caseKey);
        when(journeyMapper.read(any(), any(), anyString())).thenReturn(caseData);
    }

    private void assertPasswordIsNotValid(final String password, final String... expectedMessages) {
        Mockito.reset(interactionContext);

        final CredentialsDto credentials = new CredentialsDto(password);
        final Onboarding caseData = new Onboarding();
        caseData.setIsMainApplicantFlow(true);
        setupCase(caseData);

        final ActionResult<CredentialsResponseDto> actionResult = credentialsHandler
            .handle(credentials, interactionContext);

        final List<Error> errors = actionResult.getErrors();
        assertThat(errors).isNotEmpty();
        assertThat(errors.size()).isEqualTo(expectedMessages.length);
        for (Error error : errors) {
            assertThat(error.getKey()).isEqualTo(ErrorCodes.FLOW_001.getKey());
            assertThat(error.getMessage()).isEqualTo(ErrorCodes.FLOW_001.getMessage());
            assertThat(expectedMessages).contains(error.getContext().get("password"));
        }

        verify(interactionContext, times(0)).getCaseData(any());
    }
}