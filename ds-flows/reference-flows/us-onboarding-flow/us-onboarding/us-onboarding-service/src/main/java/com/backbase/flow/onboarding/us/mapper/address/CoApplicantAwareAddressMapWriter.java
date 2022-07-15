package com.backbase.flow.onboarding.us.mapper.address;

import com.backbase.flow.application.uso.casedata.Address;
import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.mapper.JourneyWriter;
import com.backbase.flow.onboarding.us.util.OnboardingCaseDataUtils;
import com.backbase.flow.service.mapper.AddressConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

@RequiredArgsConstructor
public class CoApplicantAwareAddressMapWriter implements JourneyWriter<Map> {

    private final ObjectMapper objectMapper;
    private final CaseDataService caseDataService;
    private static final String CO_APPLICANT_ADDRESS_STEP = "co-applicant-address";

    @Override
    public Case write(final Map journeyData, final String subject, final String context, final Case aCase) {
        final Onboarding caseData = aCase.getCaseData(Onboarding.class);
        final Address address = objectMapper.convertValue(journeyData, Address.class);

        final Applicant applicant;
        if (!OnboardingCaseDataUtils.isMainApplicantFlow(caseData) || context.endsWith(CO_APPLICANT_ADDRESS_STEP)) {
            applicant = ObjectUtils.defaultIfNull(caseData.getCoApplicant(), new Applicant());
            caseData.setCoApplicant(applicant);
        } else {
            applicant = caseData.getMainApplicant();
        }
        applicant.setAddress(address);

        aCase.setCaseData(caseData);
        return caseDataService.updateCase(aCase);
    }

    @Override
    public boolean canApply(final String context, final Case aCase) {
        return context != null && context.startsWith(AddressConstants.MAPPER_BASE_CONTEXT);
    }
}
