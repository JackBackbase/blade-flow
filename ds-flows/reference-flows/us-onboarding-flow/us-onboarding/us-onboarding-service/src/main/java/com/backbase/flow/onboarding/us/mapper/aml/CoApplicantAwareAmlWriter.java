package com.backbase.flow.onboarding.us.mapper.aml;

import static com.backbase.flow.onboarding.us.mapper.aml.AmlJourneyContext.PERSON_CONTEXT;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.mapper.JourneyWriter;
import com.backbase.flow.onboarding.us.util.OnboardingCaseDataUtils;
import com.backbase.flow.service.aml.casedata.AntiMoneyLaunderingInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class CoApplicantAwareAmlWriter implements JourneyWriter<AntiMoneyLaunderingInfo> {

    private final CaseDataService caseDataService;

    @Override
    public Case write(AntiMoneyLaunderingInfo journeyData, String subject, String context, Case aCase) {
        log.debug("Writing AML journey data for subject: {} and context: {}", subject, context);

        var onboarding = aCase.getCaseData(Onboarding.class);

        final Applicant applicant;
        if (OnboardingCaseDataUtils.isMainApplicantFlow(onboarding)) {
            applicant = onboarding.getMainApplicant();
        } else {
            applicant = ObjectUtils.defaultIfNull(onboarding.getCoApplicant(), new Applicant());
            onboarding.setCoApplicant(applicant);
        }
        applicant.setAntiMoneyLaunderingInfo(AmlInfoMapper.INSTANCE.map(journeyData));

        aCase.setCaseData(onboarding);

        return caseDataService.updateCase(aCase);
    }

    @Override
    public boolean canApply(String context, Case aCase) {
        return PERSON_CONTEXT.equalsIgnoreCase(context);
    }
}
