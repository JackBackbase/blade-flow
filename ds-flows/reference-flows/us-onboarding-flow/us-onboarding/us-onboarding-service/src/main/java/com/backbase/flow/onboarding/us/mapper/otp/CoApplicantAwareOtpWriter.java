package com.backbase.flow.onboarding.us.mapper.otp;

import static com.backbase.flow.service.ServiceItemOneTimePasswordAutoconfiguration.OTP_MAPPER_CONTEXT;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.mapper.JourneyWriter;
import com.backbase.flow.onboarding.us.util.OnboardingCaseDataUtils;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.lang.Nullable;

@RequiredArgsConstructor
public class CoApplicantAwareOtpWriter implements JourneyWriter<Map> {

    private final CaseDataService caseDataService;

    @Override
    public Case write(final Map journeyData, final String subject, final String context, final Case aCase) {
        final Onboarding caseData = aCase.getCaseData(Onboarding.class);

        final Onboarding updatedData = ObjectUtils.defaultIfNull(caseData, new Onboarding());
        final Applicant applicant = OnboardingCaseDataUtils.getApplicant(updatedData);
        final Applicant updatedApplicant = writeOtp(applicant, journeyData);
        OnboardingCaseDataUtils.setApplicant(updatedData, updatedApplicant);
        aCase.setCaseData(updatedData);
        return caseDataService.updateCase(aCase);
    }

    private Applicant writeOtp(@Nullable Applicant applicant, final Map<String, Object> journeyData) {
        applicant = ObjectUtils.defaultIfNull(applicant, new Applicant());

        if (Boolean.TRUE.equals(journeyData.get("phoneNumberVerified"))) {
            applicant.setPhoneNumber((String) journeyData.get("phoneNumber"));
            applicant.setPhoneNumberVerified((Boolean) journeyData.get("phoneNumberVerified"));
        }

        if (Boolean.TRUE.equals(journeyData.get("emailVerified"))) {
            applicant.setEmailVerified((Boolean) journeyData.get("emailVerified"));
            applicant.setEmail((String) journeyData.get("email"));
        }

        return applicant;
    }

    @Override
    public boolean canApply(final String context, final Case aCase) {
        return context.equalsIgnoreCase(OTP_MAPPER_CONTEXT);
    }
}
