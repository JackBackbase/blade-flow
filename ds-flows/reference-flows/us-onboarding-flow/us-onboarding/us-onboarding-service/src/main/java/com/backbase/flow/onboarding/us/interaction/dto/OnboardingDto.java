package com.backbase.flow.onboarding.us.interaction.dto;

import com.backbase.flow.application.uso.casedata.Address;
import com.backbase.flow.application.uso.casedata.CitizenshipInfo.CitizenshipType;
import com.backbase.flow.application.uso.casedata.CitizenshipReview;
import com.backbase.flow.application.uso.casedata.IdentityVerificationResult;
import com.backbase.flow.application.uso.casedata.OnboardingAntiMoneyLaunderingInfo;
import lombok.Data;

@Data
public class OnboardingDto {

    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String phoneNumber;
    private String email;
    private Address address;
    private OnboardingAntiMoneyLaunderingInfo antiMoneyLaunderingInfo;
    private Boolean isJointAccount;
    private Boolean isMainApplicantFlow;
    private IdentityVerificationResult identityVerificationResult;
    private CitizenshipReview citizenshipReview;
    private CitizenshipType citizenshipType;
    private String ssn;
}
