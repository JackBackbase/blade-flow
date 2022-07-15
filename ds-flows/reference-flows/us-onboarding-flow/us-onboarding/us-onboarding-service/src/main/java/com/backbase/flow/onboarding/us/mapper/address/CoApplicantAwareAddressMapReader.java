package com.backbase.flow.onboarding.us.mapper.address;

import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.mapper.JourneyReader;
import com.backbase.flow.onboarding.us.mapper.GenericCoApplicantAwareApplicantMapReader;
import com.backbase.flow.onboarding.us.util.OnboardingCaseDataUtils;
import com.backbase.flow.service.mapper.AddressConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CoApplicantAwareAddressMapReader implements JourneyReader<Map> {

    private final ObjectMapper objectMapper;
    private static final String IS_MAIN_APPLICANT_FLOW_FIELD = "isMainApplicantFlow";

    @Override
    public Map read(final String subject, final String context, final Case aCase) {
        final Onboarding onboarding = aCase.getCaseData(Onboarding.class);

        final Map<String, Object> applicantMap;
        if (OnboardingCaseDataUtils.isMainApplicantFlow(onboarding)) {
            applicantMap = GenericCoApplicantAwareApplicantMapReader.read(aCase);
        } else {
            applicantMap = objectMapper.convertValue(onboarding.getCoApplicant(), Map.class);
        }
        applicantMap.put(IS_MAIN_APPLICANT_FLOW_FIELD, onboarding.getIsMainApplicantFlow());

        return applicantMap;
    }

    @Override
    public boolean canApply(final String context, final Case aCase) {
        return context != null && context.startsWith(AddressConstants.MAPPER_BASE_CONTEXT);
    }
}
