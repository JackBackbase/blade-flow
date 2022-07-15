package com.backbase.flow.sme.onboarding.interaction.model;

import javax.validation.constraints.AssertTrue;
import lombok.Value;

@Value
public class TermsConditionsRequestDto {

    @AssertTrue
    Boolean accepted;
}
