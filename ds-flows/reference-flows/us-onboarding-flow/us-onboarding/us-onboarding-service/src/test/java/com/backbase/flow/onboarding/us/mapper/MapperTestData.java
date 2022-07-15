package com.backbase.flow.onboarding.us.mapper;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.cases.Case;

public class MapperTestData {

    public static Case sampleCaseNoMainApplicantCompletionId() {
        final Case aCase = new Case();
        final Onboarding onboarding = new Onboarding();
        onboarding.setMainApplicant(sampleMainApplicant());
        onboarding.setIsMainApplicantFlow(true);
        aCase.setCaseData(onboarding);
        return aCase;
    }

    public static Case sampleCaseWithMainApplicantCompletionId() {
        final Case aCase = new Case();
        final Onboarding onboarding = new Onboarding();
        onboarding.setMainApplicant(sampleMainApplicant());
        onboarding.setCoApplicantId("Hello there");
        onboarding.setCoApplicant(sampleCoApplicant());
        onboarding.setIsMainApplicantFlow(false);

        aCase.setCaseData(onboarding);
        return aCase;
    }

    private static Applicant sampleMainApplicant() {
        final Applicant applicant = new Applicant();
        applicant.setFirstName("main-applicant");
        applicant.setLastName("main-applicant-last-name");
        applicant.setDateOfBirth("2000-01-01");

        return applicant;
    }

    private static Applicant sampleCoApplicant() {
        final Applicant applicant = new Applicant();
        applicant.setFirstName("co-applicant");
        applicant.setLastName("co-applicant-last-name");
        applicant.setDateOfBirth("1000-01-01");

        return applicant;
    }

}
