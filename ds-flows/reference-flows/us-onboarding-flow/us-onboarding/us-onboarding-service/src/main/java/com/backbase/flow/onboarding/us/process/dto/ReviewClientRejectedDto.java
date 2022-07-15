package com.backbase.flow.onboarding.us.process.dto;

import com.backbase.flow.application.uso.casedata.Answer;
import com.backbase.flow.application.uso.casedata.OnboardingAntiMoneyLaunderingInfo;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewClientRejectedDto {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String rejectionCode;
    private List<List<Answer>> kycInformation;
    private OnboardingAntiMoneyLaunderingInfo antiMoneyLaunderingInfo;

}
