package com.backbase.flow.onboarding.us.mapper.aml;

import static com.backbase.flow.onboarding.us.mapper.aml.AmlJourneyContext.PERSON_CONTEXT;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.mapper.JourneyReader;
import com.backbase.flow.onboarding.us.util.OnboardingCaseDataUtils;
import com.backbase.flow.service.aml.casedata.AmlPersonApplicant;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CoApplicantAwareAmlPersonReader implements JourneyReader<AmlPersonApplicant> {

    @Override
    public AmlPersonApplicant read(String subject, String context, Case aCase) {
        log.debug("Reading Person Applicant data for subject: {} and context: {}", subject, context);

        var caseData = aCase.getCaseData(Onboarding.class);
        final Applicant applicant;
        if (OnboardingCaseDataUtils.isMainApplicantFlow(caseData)) {
            applicant = caseData.getMainApplicant();
        } else {
            applicant = ObjectUtils.defaultIfNull(caseData.getCoApplicant(), new Applicant());
        }

        return new AmlPersonApplicant()
            .withFullName(applicant.getFirstName() + " " + applicant.getLastName())
            .withYearOfBirth(getYearOfBirth(applicant.getDateOfBirth()));
    }

    private int getYearOfBirth(final String dob) {
        return LocalDate.parse(dob).getYear();
    }

    @Override
    public boolean canApply(String context, Case aCase) {
        return PERSON_CONTEXT.equalsIgnoreCase(context);
    }
}
