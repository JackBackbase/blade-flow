package com.backbase.flow.onboarding.us.interaction.dto;

import javax.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PoliciesAgreementDto {

    @AssertTrue
    private boolean agreedToTerms;

}
