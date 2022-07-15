package com.backbase.flow.onboarding.us.process;

import com.backbase.flow.application.uso.casedata.AgreementInfo;
import com.backbase.flow.application.uso.casedata.AgreementInfo.PolicyType;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.application.uso.casedata.OnboardingAntiMoneyLaunderingInfo.Status;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.onboarding.us.util.CaseKeyUtils;
import com.backbase.flow.onboarding.us.util.OnboardingCaseDataUtils;
import com.backbase.flow.process.annotation.ProcessBean;
import java.util.EnumSet;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessBean("conditionalEventHandler")
public class ConditionalEventHandler {

    private final JourneyMapper<Onboarding> onboardingMapper;

    public boolean amlWasSuccessful(final DelegateExecution delegateExecution) {
        final Onboarding caseData = getOnboarding(delegateExecution);

        return OnboardingCaseDataUtils.getApplicant(caseData).getAntiMoneyLaunderingInfo().getStatus().equals(Status.SUCCESS);
    }

    public boolean amlFailed(final DelegateExecution delegateExecution) {
        final Onboarding caseData = getOnboarding(delegateExecution);

        return OnboardingCaseDataUtils.getApplicant(caseData).getAntiMoneyLaunderingInfo().getStatus().equals(Status.FAILED);
    }

    public boolean policiesAccepted(final DelegateExecution delegateExecution) {
        final Onboarding onboarding = getOnboarding(delegateExecution);
        return OnboardingCaseDataUtils.getApplicant(onboarding).getAgreements().stream()
            .filter(agreementInfo -> Boolean.TRUE.equals(agreementInfo.getAccepted()))
            .map(AgreementInfo::getPolicyType)
            .collect(Collectors.toUnmodifiableSet())
            .containsAll(EnumSet.allOf(PolicyType.class));
    }

    private Onboarding getOnboarding(DelegateExecution execution) {
        final String caseKey = CaseKeyUtils.getCaseKey(execution).toString();
        return onboardingMapper.read(null, null, caseKey);
    }
}
