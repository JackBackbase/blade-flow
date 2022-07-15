package com.backbase.flow.onboarding.us.process.handler;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.onboarding.us.exception.HandlerException;
import com.backbase.flow.onboarding.us.service.coapplicant.CoApplicantNotificationService;
import com.backbase.flow.onboarding.us.util.CaseKeyUtils;
import com.backbase.flow.process.annotation.ProcessBean;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@ProcessBean("coApplicantIdHandler")
@Component
public class CoApplicantIdTaskHandler implements JavaDelegate {

    private final CaseDataService caseDataService;
    private final CoApplicantNotificationService coApplicantNotificationService;

    @Override
    public void execute(DelegateExecution execution) throws HandlerException {
        final var aCase = caseDataService
            .getCaseByKey(CaseKeyUtils.getCaseKey(execution));
        final var onboarding = aCase.getCaseData(Onboarding.class);

        setCoApplicantId(onboarding);
        aCase.setCaseData(onboarding);
        caseDataService.updateCase(aCase);
        coApplicantNotificationService.sendEmail(onboarding);
    }

    private void setCoApplicantId(Onboarding onboarding) {
        final var coApplicantId = UUID.randomUUID().toString();
        onboarding.setCoApplicantId(coApplicantId);
        if (Objects.isNull(onboarding.getCoApplicant())) {
            onboarding.setCoApplicant(new Applicant());
        }

        log.info("Setting coApplicantId to {}", coApplicantId);
    }
}
