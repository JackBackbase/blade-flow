package com.backbase.flow.onboarding.us.mapper.idv;

import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.mapper.JourneyReader;
import com.backbase.flow.onboarding.us.mapper.GenericCoApplicantAwareApplicantMapReader;
import com.backbase.flow.service.dto.IdentityVerificationContext;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CoApplicantAwareIdentityVerificationResultReader implements JourneyReader<Map> {

    private static final String FILESET_NAME_SUFFIX = "filesetNameSuffix";
    static final String FIRST_NAME = "firstName";
    static final String LAST_NAME = "lastName";

    @Override
    public Map<String, Object> read(String subject, String context, Case aCase) {
        final Map<String, Object> applicant = GenericCoApplicantAwareApplicantMapReader.read(aCase);

        final Map<String, Object> applicantResult = (Map<String, Object>) applicant
            .getOrDefault(IdentityVerificationContext.RESULT, Map.of());
        final Map<String, Object> result = new HashMap<>(applicantResult);
        result.computeIfAbsent(FILESET_NAME_SUFFIX, key -> applicant.get(FIRST_NAME) + " " + applicant.get(LAST_NAME));

        return result;
    }

    @Override
    public boolean canApply(String context, Case aCase) {
        return IdentityVerificationContext.RESULT.equals(context);
    }
}
