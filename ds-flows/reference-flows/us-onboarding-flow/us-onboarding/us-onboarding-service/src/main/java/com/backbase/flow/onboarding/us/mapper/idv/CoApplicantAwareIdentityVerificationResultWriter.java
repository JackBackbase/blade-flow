package com.backbase.flow.onboarding.us.mapper.idv;

import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.mapper.JourneyWriter;
import com.backbase.flow.onboarding.us.util.OnboardingCaseDataUtils;
import com.backbase.flow.service.dto.IdentityVerificationContext;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CoApplicantAwareIdentityVerificationResultWriter implements JourneyWriter<Map> {

    private final CaseDataService caseDataService;

    @Override
    public Case write(Map journeyData, String subject, String context, Case aCase) {
        final Map<String, Object> caseData = aCase.getCaseData(Map.class);

        final Map<String, Object> applicant = OnboardingCaseDataUtils.getApplicant(caseData);
        applicant.put(IdentityVerificationContext.RESULT, journeyData);
        OnboardingCaseDataUtils.setApplicant(caseData, applicant);
        aCase.setCaseData(caseData);

        return caseDataService.updateCase(aCase);
    }

    @Override
    public boolean canApply(String context, Case aCase) {
        return IdentityVerificationContext.RESULT.equals(context);
    }
}
