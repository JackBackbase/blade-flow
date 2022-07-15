package com.backbase.flow.sme.onboarding.mapper;

import com.backbase.flow.sme.onboarding.casedata.Applicant;

public interface ApplicantMapper {

    default Integer yob(Applicant applicant) {
        return applicant.getDateOfBirth() != null ? applicant.getDateOfBirth().getYear() : null;
    }

    default String fullName(Applicant applicant) {
        return String.format("%s %s", applicant.getFirstName(), applicant.getLastName());
    }
}
