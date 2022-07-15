package com.backbase.flow.onboarding.us.mapper.idv;

import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.mapper.JourneyReader;
import com.backbase.flow.onboarding.us.mapper.GenericCoApplicantAwareApplicantMapReader;
import com.backbase.flow.service.dto.IdentityVerificationContext;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CoApplicantAwareGenericMapIdentityVerificationReader implements JourneyReader<Map> {

    @Override
    public Map<String, Object> read(String subject, String context, Case aCase) {
        return GenericCoApplicantAwareApplicantMapReader.read(aCase);
    }

    @Override
    public boolean canApply(String context, Case aCase) {
        return IdentityVerificationContext.CASE.equals(context);
    }
}
