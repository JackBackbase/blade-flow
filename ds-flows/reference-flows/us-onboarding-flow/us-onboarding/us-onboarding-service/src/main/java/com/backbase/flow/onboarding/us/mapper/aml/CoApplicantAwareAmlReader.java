package com.backbase.flow.onboarding.us.mapper.aml;

import static com.backbase.flow.onboarding.us.mapper.aml.AmlJourneyContext.PERSON_CONTEXT;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.application.uso.casedata.OnboardingAntiMoneyLaunderingInfo;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.mapper.JourneyReader;
import com.backbase.flow.onboarding.us.util.OnboardingCaseDataUtils;
import com.backbase.flow.service.aml.casedata.AntiMoneyLaunderingInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CoApplicantAwareAmlReader implements JourneyReader<AntiMoneyLaunderingInfo> {

    @Override
    public AntiMoneyLaunderingInfo read(String subject, String context, Case aCase) {
        log.debug("Reading AML journey data for subject: {} and context: {}", subject, context);

        final var caseData = aCase.getCaseData(Onboarding.class);
        final OnboardingAntiMoneyLaunderingInfo onboardingAmlInfo;

        final Applicant applicant;
        if (OnboardingCaseDataUtils.isMainApplicantFlow(caseData)) {
            applicant = caseData.getMainApplicant();
        } else {
            applicant = ObjectUtils.defaultIfNull(caseData.getCoApplicant(), new Applicant());
        }
        onboardingAmlInfo = applicant.getAntiMoneyLaunderingInfo();

        return AmlInfoMapper.INSTANCE
            .map(ObjectUtils.defaultIfNull(onboardingAmlInfo, new OnboardingAntiMoneyLaunderingInfo()));
    }

    @Override
    public boolean canApply(String context, Case aCase) {
        return PERSON_CONTEXT.equalsIgnoreCase(context);
    }
}
