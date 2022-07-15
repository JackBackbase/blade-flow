package com.backbase.flow.onboarding.us.interaction.resume;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.onboarding.us.util.OnboardingCaseDataUtils;
import com.backbase.flow.service.resume.rule.ResumeRule;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OtpVerificationResumeRule implements ResumeRule {

    private final ObjectMapper objectMapper;

    @Override
    public boolean validate(Map<String, Object> currentCaseData, Map<String, Object> previousCaseData) {
        final Onboarding onboarding = objectMapper.convertValue(currentCaseData, Onboarding.class);

        final Applicant applicant = OnboardingCaseDataUtils.getApplicant(onboarding);
        return applicant != null && (applicant.getPhoneNumberVerified() || applicant.getEmailVerified());
    }
}
