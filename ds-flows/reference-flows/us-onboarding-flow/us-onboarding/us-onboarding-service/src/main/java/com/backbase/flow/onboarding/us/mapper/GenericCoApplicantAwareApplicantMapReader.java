package com.backbase.flow.onboarding.us.mapper;

import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.onboarding.us.util.OnboardingCaseDataUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GenericCoApplicantAwareApplicantMapReader {

    private static final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    private static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE = new TypeReference<>() {
    };

    public static Map<String, Object> read(Case aCase) {
        final Onboarding onboarding = aCase.getCaseData(Onboarding.class);
        final Map<String, Object> result;
        if (OnboardingCaseDataUtils.isMainApplicantFlow(onboarding)) {
            result = objectMapper.convertValue(onboarding.getMainApplicant(), MAP_TYPE_REFERENCE);
        } else {
            result = objectMapper.convertValue(onboarding.getCoApplicant(), MAP_TYPE_REFERENCE);
        }

        return result;
    }
}
