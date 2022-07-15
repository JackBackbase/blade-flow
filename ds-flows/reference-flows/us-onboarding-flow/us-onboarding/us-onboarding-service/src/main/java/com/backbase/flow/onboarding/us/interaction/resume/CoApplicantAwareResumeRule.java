package com.backbase.flow.onboarding.us.interaction.resume;

import java.util.Map;

import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.onboarding.us.util.OnboardingCaseDataUtils;
import com.backbase.flow.service.resume.rule.ResumeRule;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CoApplicantAwareResumeRule implements ResumeRule {

    private final ObjectMapper objectMapper;

    @Override
    public boolean validate(Map<String, Object> currentCaseData, Map<String, Object> previousCaseData) {
        final Onboarding current = objectMapper.convertValue(currentCaseData, Onboarding.class);
        final Onboarding previous = objectMapper.convertValue(previousCaseData, Onboarding.class);

        return OnboardingCaseDataUtils.isMainApplicantFlow(current) && OnboardingCaseDataUtils.isMainApplicantFlow(previous);
    }

}
