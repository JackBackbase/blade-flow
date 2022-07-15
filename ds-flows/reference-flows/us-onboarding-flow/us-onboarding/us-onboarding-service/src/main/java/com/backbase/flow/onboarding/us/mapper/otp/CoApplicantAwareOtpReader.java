package com.backbase.flow.onboarding.us.mapper.otp;

import static com.backbase.flow.service.ServiceItemOneTimePasswordAutoconfiguration.OTP_MAPPER_CONTEXT;

import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.mapper.JourneyReader;
import com.backbase.flow.onboarding.us.mapper.GenericCoApplicantAwareApplicantMapReader;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CoApplicantAwareOtpReader implements JourneyReader<Map> {

    @Override
    public Map read(final String subject, final String context, final Case aCase) {
        return GenericCoApplicantAwareApplicantMapReader.read(aCase);
    }

    @Override
    public boolean canApply(final String context, final Case aCase) {
        return context.equals(OTP_MAPPER_CONTEXT);
    }
}
