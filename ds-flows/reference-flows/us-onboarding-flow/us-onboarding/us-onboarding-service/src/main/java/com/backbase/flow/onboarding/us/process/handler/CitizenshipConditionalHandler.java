package com.backbase.flow.onboarding.us.process.handler;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.iam.annotation.RunWithoutAuthorization;
import com.backbase.flow.onboarding.us.util.CaseKeyUtils;
import com.backbase.flow.process.annotation.ProcessBean;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@ProcessBean("citizenshipConditionalHandler")
public class CitizenshipConditionalHandler {

    private final JourneyMapper<Applicant> mapper;

    @RunWithoutAuthorization
    public boolean citizenshipDataReadyForReview(DelegateExecution execution) {
        UUID caseKey = CaseKeyUtils.getCaseKey(execution);
        final Applicant applicant = mapper.read(null, null, caseKey.toString());

        return applicant.getCitizenship() != null
            && applicant.getCitizenship().getCitizenshipReview() != null
            && applicant.getCitizenship().getCitizenshipReview().getNeeded() != null;
    }

    @RunWithoutAuthorization
    public boolean isReviewNeeded(DelegateExecution execution) {
        UUID caseKey = CaseKeyUtils.getCaseKey(execution);
        final Applicant applicant = mapper.read(null, null, caseKey.toString());

        return citizenshipDataReadyForReview(execution)
            && Boolean.TRUE.equals(applicant.getCitizenship().getCitizenshipReview().getNeeded());
    }
}
