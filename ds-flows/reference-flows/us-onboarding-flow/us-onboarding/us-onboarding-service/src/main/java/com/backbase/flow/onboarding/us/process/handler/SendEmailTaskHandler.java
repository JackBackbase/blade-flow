package com.backbase.flow.onboarding.us.process.handler;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.onboarding.us.util.CaseKeyUtils;
import com.backbase.flow.onboarding.us.util.OnboardingCaseDataUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class SendEmailTaskHandler implements JavaDelegate {

    private final CaseDataService caseDataService;

    @Override
    public void execute(DelegateExecution execution) {
        final Onboarding caseData = caseDataService
            .getCaseByKey(CaseKeyUtils.getCaseKey(execution))
            .getCaseData(Onboarding.class);
        final Applicant applicant = OnboardingCaseDataUtils.getApplicant(caseData);
        log.info("Here, an email will be sent to the customer {} {}",
            applicant.getFirstName(), applicant.getLastName());
    }
}
