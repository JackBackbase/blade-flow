package com.backbase.flow.onboarding.us.process;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.backbase.flow.application.uso.casedata.Address;
import com.backbase.flow.application.uso.casedata.AgreementInfo;
import com.backbase.flow.application.uso.casedata.AgreementInfo.PolicyType;
import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.application.uso.casedata.OnboardingAntiMoneyLaunderingInfo;
import com.backbase.flow.application.uso.casedata.OnboardingAntiMoneyLaunderingInfo.Status;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.process.ProcessConstants;
import java.util.Arrays;
import java.util.UUID;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConditionalEventHandlerTest {

    private final JourneyMapper<Onboarding> mapper = mock(JourneyMapper.class);
    private final DelegateExecution execution = mock(DelegateExecution.class);
    private final ConditionalEventHandler conditionalEventHandler = new ConditionalEventHandler(mapper);

    @BeforeEach
    void setup() {
        when(execution.getVariable(ProcessConstants.PROCESS_VARIABLE_CASE_KEY))
            .thenReturn(UUID.randomUUID());
    }

    void setReturnedCase(final Onboarding onboarding) {
        when(mapper.read(eq(null), eq(null), anyString()))
            .thenReturn(onboarding);
    }

    @Test
    void amlWasSuccessful_amlWasNotSuccessful_falseIsReturned() {
        final Onboarding onboarding = new Onboarding()
            .withMainApplicant(new Applicant()
                .withAntiMoneyLaunderingInfo(new OnboardingAntiMoneyLaunderingInfo()
                    .withStatus(Status.FAILED)))
            .withIsMainApplicantFlow(true);
        setReturnedCase(onboarding);

        final boolean result = conditionalEventHandler.amlWasSuccessful(execution);

        assertThat(result).isFalse();
    }

    @Test
    void amlWasSuccessful_amlWasSuccessful_trueIsReturned() {
        final Onboarding onboarding = new Onboarding()
            .withMainApplicant(new Applicant()
                .withAntiMoneyLaunderingInfo(new OnboardingAntiMoneyLaunderingInfo()
                    .withStatus(Status.SUCCESS)))
            .withIsMainApplicantFlow(true);
        setReturnedCase(onboarding);

        final boolean result = conditionalEventHandler.amlWasSuccessful(execution);

        assertThat(result).isTrue();
    }

    @Test
    void amlFailed_amlInReview_falseIsReturned() {
        final Onboarding onboarding = new Onboarding()
            .withMainApplicant(new Applicant()
                .withAntiMoneyLaunderingInfo(new OnboardingAntiMoneyLaunderingInfo()
                    .withStatus(Status.IN_REVIEW)))
            .withIsMainApplicantFlow(true);
        setReturnedCase(onboarding);

        final boolean result = conditionalEventHandler.amlFailed(execution);

        assertThat(result).isFalse();
    }

    @Test
    void amlFailed_amlFailed_trueIsReturned() {
        final Onboarding onboarding = new Onboarding()
            .withMainApplicant(new Applicant()
                .withAntiMoneyLaunderingInfo(new OnboardingAntiMoneyLaunderingInfo()
                    .withStatus(Status.FAILED)))
            .withIsMainApplicantFlow(true);
        setReturnedCase(onboarding);

        final boolean result = conditionalEventHandler.amlFailed(execution);

        assertThat(result).isTrue();
    }

    @Test
    void policiesAccepted_termsAreNotAccepted_falseIsReturned() {
        final Onboarding onboarding = new Onboarding()
            .withMainApplicant(new Applicant()
                .withAgreements(Arrays.asList(
                    new AgreementInfo()
                        .withAccepted(true)
                        .withPolicyType(PolicyType.PRIVACY_POLICY),
                    new AgreementInfo()
                        .withAccepted(false)
                        .withPolicyType(PolicyType.TERMS_AND_CONDITIONS)
                )))
            .withIsMainApplicantFlow(true);

        setReturnedCase(onboarding);

        final boolean result = conditionalEventHandler.policiesAccepted(execution);

        assertThat(result).isFalse();
    }

    @Test
    void policiesAccepted_policiesAreAccepted_trueIsReturned() {
        final Onboarding onboarding = new Onboarding()
            .withMainApplicant(new Applicant()
                .withAgreements(Arrays.asList(
                    new AgreementInfo()
                        .withAccepted(true)
                        .withPolicyType(PolicyType.PRIVACY_POLICY),
                    new AgreementInfo()
                        .withAccepted(true)
                        .withPolicyType(PolicyType.TERMS_AND_CONDITIONS)
                )))
            .withIsMainApplicantFlow(true);
        setReturnedCase(onboarding);

        final boolean result = conditionalEventHandler.policiesAccepted(execution);

        assertThat(result).isTrue();
    }
}
